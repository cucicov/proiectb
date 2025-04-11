import {AppBar, Box, Button, Dialog, DialogActions, DialogTitle, Toolbar, Typography} from "@mui/material";
import {useQuery} from "@tanstack/react-query";
import {getLatestContentMetadata} from "../api/contentapi.ts";
import {useEffect, useState} from "react";
import {formatDate} from "../utils/utils.ts";
import InfoIcon from '@mui/icons-material/Info';
import ProiectbDialogContent from "./ProiectbDialogContent.tsx";
import {useParams} from "react-router-dom";
import LocationOnIcon from "@mui/icons-material/LocationOn";
import Location from "./Location.tsx";
// import { ErrorBoundary } from 'react-error-boundary';
// import { Suspense } from 'react';


function Header({locationEnabled = false, infoEnabled = false}) {
    const {id} = useParams();

    // const queryClient = useQueryClient();
    const [headerText, setHeaderText] = useState<string>("");
    const [openInfo, setOpenInfo] = useState(false);
    const [openLocation, setOpenLocation] = useState(false);


    const {data: contentMetadata, isSuccess, error, isError} = useQuery({
        queryKey: ["latest-content-metadata"],
        queryFn: () => getLatestContentMetadata(id),
    });


    // set the header text and the current content type.
    useEffect(() => {
        if (isSuccess && contentMetadata) {
            const formattedDate = formatDate(contentMetadata?.activationTimestamp);
            setHeaderText(formattedDate);
        }

    }, [isSuccess, contentMetadata]);

    const handleOpenInfo = () => {
        setOpenInfo(!openInfo);
    }

    const handleCloseInfo = () => {
        setOpenInfo(false);
    }

    const handleOpenLocation = () => {
        setOpenLocation(!openLocation);
    }

    const handleCloseLocation = () => {
        setOpenLocation(false);
    }


    if (isError) {
        if (error instanceof Error) {
            const statusCode = (error as any).response?.status;
            const message = (error as any).response?.message;
            console.log('Response status:', statusCode);
            console.log('Response message:', message);
            return (
                <></>
            )
        }
    } else {
        return (
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6">
                        {headerText}
                    </Typography>

                    <Box sx={{marginLeft: "auto"}}>
                        {locationEnabled && <>
                            <Button onClick={handleOpenLocation}
                                    sx={{
                                        marginLeft: "1em", // Align to the right
                                        color: "#fff", // Ensure the icon is visible
                                        backgroundColor: "info.main", // Info-themed color
                                        "&:hover": {
                                            backgroundColor: "info.dark", // Darker on hover
                                        },
                                        minWidth: 0, // Remove default padding
                                        width: "40px", // Make it circular (width = height)
                                        height: "40px", // Set height
                                        borderRadius: "50%", // Fully round
                                    }}
                                    variant="contained">
                                <LocationOnIcon/>
                            </Button>
                            <Dialog open={openLocation} onClose={handleCloseLocation}>
                                <Location/>
                                <DialogActions>
                                    <Button onClick={handleCloseLocation}>Close</Button>
                                </DialogActions>
                            </Dialog></>
                        }

                        {infoEnabled && <>
                            <Button onClick={handleOpenInfo}
                                    sx={{
                                        marginLeft: "1em", // Align to the right
                                        color: "#fff", // Ensure the icon is visible
                                        backgroundColor: "info.main", // Info-themed color
                                        "&:hover": {
                                            backgroundColor: "info.dark", // Darker on hover
                                        },
                                        minWidth: 0, // Remove default padding
                                        width: "40px", // Make it circular (width = height)
                                        height: "40px", // Set height
                                        borderRadius: "50%", // Fully round
                                    }}
                                    variant="contained">
                                <InfoIcon/>
                            </Button>

                            <Dialog open={openInfo} onClose={handleCloseInfo}>
                                <DialogTitle>Proiect B</DialogTitle>
                                <ProiectbDialogContent/>
                                <DialogActions>
                                    <Button onClick={handleCloseInfo}>Close</Button>
                                </DialogActions>
                            </Dialog>
                        </>}
                    </Box>

                </Toolbar>
            </AppBar>
        )
    }
}

export default Header;