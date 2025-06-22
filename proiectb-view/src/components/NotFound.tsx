// components/NotFound.tsx
import { Box, Typography, Container } from '@mui/material';

const NotFound = () => {

    return (
        <Container maxWidth="md">
            <Box
                display="flex"
                flexDirection="column"
                alignItems="center"
                justifyContent="center"
                minHeight="60vh"
                textAlign="center"
            >
                <Typography variant="h1" color="primary" gutterBottom>
                    404
                </Typography>
                <Typography variant="h4" gutterBottom>
                    Page Not Found
                </Typography>
                <Typography variant="body1" color="textSecondary">
                    The page you're looking for doesn't exist or has been moved.
                </Typography>
                <Typography variant="body2" color="textSecondary">
                    You must use a UUID to access a specific content.
                </Typography>
            </Box>
        </Container>
    );
};

export default NotFound;