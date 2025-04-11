import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@mui/material";

interface LocationPermissionDialogProps {
    onDeny: () => void;
    onAllow: () => void;
    open: boolean;
    onClose: () => void;
}


function LocationPermissionDialog({ onDeny, onAllow, open, onClose }: LocationPermissionDialogProps
) {
    return (
        <Dialog
            open={open}
            onClose={onClose}
            aria-labelledby="location-permission-dialog"
        >
            <DialogTitle id="location-permission-dialog">
                Location Access Required
            </DialogTitle>
            <DialogContent>
                <Typography gutterBottom>
                    This app needs access to your location. When prompted by your browser, please click "Allow" to share your location.
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    We use your location to provide location-based services. Your location data is only stored in your browser's session storage.
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onDeny} color="error">
                    Don't Allow
                </Button>
                <Button onClick={onAllow} variant="contained">
                    Allow Location Access
                </Button>
            </DialogActions>
        </Dialog>
    )
}

export default LocationPermissionDialog;