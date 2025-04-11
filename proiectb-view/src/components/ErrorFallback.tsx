import {AppBar, Toolbar, Typography} from "@mui/material";


function ErrorFallback({ error }: { error: Error }) {

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography color="error" variant="h6">
                    {error.message || 'An error occurred while loading content'}
                </Typography>
            </Toolbar>
        </AppBar>


    );
}

export default ErrorFallback;
