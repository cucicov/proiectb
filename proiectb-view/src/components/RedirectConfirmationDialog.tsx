import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@mui/material";

interface RedirectConfirmationDialogProps {
    url: string;
    onAccept: () => void;
    onReject: () => void;
    open: boolean;
    onClose: () => void;
}

function RedirectConfirmationDialog({ url, onAccept, onReject, open, onClose }: RedirectConfirmationDialogProps) {
    return (
        <Dialog
            open={open}
            onClose={onClose}
            aria-labelledby="redirect-confirmation-dialog"
        >
            <DialogTitle id="redirect-confirmation-dialog">
                External Link Redirection
            </DialogTitle>
            <DialogContent>
                <Typography gutterBottom>
                    You are about to be redirected to an external website:
                </Typography>
                <Typography variant="body2" color="primary" sx={{ wordBreak: 'break-all' }}>
                    {url}
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
                    Do you want to continue to this website?
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onReject} color="error">
                    Cancel
                </Button>
                <Button onClick={onAccept} variant="contained" color="primary">
                    Continue
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default RedirectConfirmationDialog;