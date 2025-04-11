import {useGeolocated} from "react-geolocated";
import {
    Box,
    Typography,
    CircularProgress,
    Alert,
    Button,
} from '@mui/material';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import {useState, useEffect} from 'react';
import LocationPermissionDialog from "./LocationPermissionDialog.tsx";
import {useQueryClient} from "@tanstack/react-query";
import {Coordinates} from "../api/types.ts";
import {updateTokenLocation} from "../api/contentapi.ts";
import {useParams} from "react-router-dom";
import {isLocationPopUpRequestedFromSessionStorage} from "../utils/utils.ts";


function Location() {
    const { id } = useParams();

    const [showPermissionDialog, setShowPermissionDialog] = useState(false);
    const [fetchingLocation, setFetchingLocation] = useState(false);
    const [coordinates, setCoordinates] = useState<Coordinates | null>(() => {
        const sessionCoords = sessionStorage.getItem('sessionCoords');
        if (sessionCoords) {
            try {
                return JSON.parse(sessionCoords);
            } catch (error) {
                console.error('Failed to parse coordinates from session storage:', error);
                return null;
            }
        }

    });

    const queryClient = useQueryClient();

    const {isGeolocationAvailable, isGeolocationEnabled, getPosition} = useGeolocated({
        positionOptions: {
            enableHighAccuracy: true,
        },
        userDecisionTimeout: 5000,
        suppressLocationOnMount: true, // Don't request immediately
        onSuccess: (position) => {
            // store lat long in session
            sessionStorage.setItem('sessionCoords', JSON.stringify({
                lat: position.coords.latitude,
                lon: position.coords.longitude
            }));
            setCoordinates({lat: position.coords.latitude, lon: position.coords.longitude});
            setShowPermissionDialog(false);
            setFetchingLocation(false);

            // call api to insert the geolocation to the token.
            updateTokenLocation(
                id,
                position.coords.latitude,
                position.coords.longitude)
        }
    });

    useEffect(() => {
        // Check geolocation permission.
        navigator.permissions?.query({name: 'geolocation'})
            .then((permissionStatus) => {
                // If permission is already granted, get position
                if (permissionStatus.state === 'granted') {
                    getPosition();
                } else if (permissionStatus.state === 'prompt' && !showPermissionDialog) {
                    setShowPermissionDialog(true);
                }
            });
    }, [queryClient.getQueryState(["latest-content-metadata"])]);


    const handleRequestLocation = () => {
        setShowPermissionDialog(false);
        setFetchingLocation(true);
        getPosition();
        sessionStorage.setItem('locationPopUpRequested', 'true');
    };

    const handleDenyLocation = () => {
        setShowPermissionDialog(false);
        sessionStorage.setItem('locationPopUpRequested', 'true');
    };

    if (!isGeolocationAvailable) {
        return (
            <Alert severity="error">
                Your browser does not support Geolocation
            </Alert>
        );
    }

    if (!isGeolocationEnabled) {
        return (
            <Alert severity="warning">
                Please enable location services in your device settings
            </Alert>
        );
    }

    if (!coordinates) {
        if (!fetchingLocation) {
            return (
                <>
                    <LocationPermissionDialog
                        onDeny={handleDenyLocation}
                        onAllow={handleRequestLocation}
                        open={showPermissionDialog && !isLocationPopUpRequestedFromSessionStorage()}
                        onClose={() => setShowPermissionDialog(false)}/>

                    <Box display="flex" flexDirection="column" alignItems="center" gap={2} sx={{p: 3, maxWidth: 400}}>
                        <LocationOnIcon color="primary" sx={{fontSize: 40}}/>
                        <Typography variant="h6" textAlign="center">
                            Location Access Required
                        </Typography>
                        <Button
                            variant="contained"
                            onClick={handleRequestLocation}
                            startIcon={<LocationOnIcon/>}
                        >
                            Share Location
                        </Button>
                    </Box>
                </>
            )
        }
        if (fetchingLocation) {
            return (
                <Box display="flex" alignItems="center" gap={2} p={2} sx={{p: 3, maxWidth: 400}}>
                    <CircularProgress size={20}/>
                    <Typography>Getting location data...</Typography>
                </Box>
            )
        }
    } else {
        return (
            <Box sx={{p: 3, maxWidth: 400}}>
                <Box display="flex" alignItems="center" gap={1} mb={2}>
                    <LocationOnIcon color="primary"/>
                    <Typography variant="h6">Location Details</Typography>
                </Box>
                <Box>
                    <Typography><strong>Latitude:</strong> {coordinates.lat}</Typography>
                    <Typography><strong>Longitude:</strong> {coordinates.lon}</Typography>
                </Box>
            </Box>
        )
    }

}


export default Location;