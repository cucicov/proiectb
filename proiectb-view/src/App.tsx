import {Container, createTheme, ThemeProvider, CssBaseline} from "@mui/material";
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import Header from "./components/Header.tsx";
import Body from "./components/Body.tsx";
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Input from "./components/Input.tsx";
// import Location from "./components/Location.tsx";



const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: Infinity,  // Data will never become stale automatically
            refetchOnWindowFocus: false,
            refetchOnReconnect: false,
            refetchOnMount: false,
            retry: false,
        },
    },
    logger: {
        log: () => {},
        warn: () => {},
        error: () => {},
    }
});


const customTheme = createTheme({
    palette: {
        primary: {
            main: '#333333', // Purple as the primary main color
            light: '#3f3f3f', // Lighter shade of primary
            dark: '#212121', // Darker shade of primary
        },
        secondary: {
            main: '#7a7a7a', // Teal as secondary color
        },
        info: {
            main: '#5a5a5a', // Default blue for "info"
        },
        background: {
            default: '#f5f5f5', // Custom background color
            paper: '#ffffff', // Paper component color
        },
    },
    typography: {
        fontFamily: "'Roboto', 'Arial', sans-serif", // Custom font-family
        h1: {
            fontSize: '2.5rem', // Customize h1 size
            fontWeight: 700, // Make it bold
        },
        button: {
            textTransform: 'none', // Disable ALL CAPS for buttons
        },
    },
    spacing: 8, // Default spacing unit (8px)

});


function App() {

    return (
        <Container maxWidth="xl">
            <ThemeProvider theme={customTheme}>
                <CssBaseline/>
                <QueryClientProvider client={queryClient}>
                    <BrowserRouter>
                        <Routes>
                            <Route path="/:id" element={
                                <>
                                    <Header locationEnabled infoEnabled/>
                                    <Body/>
                                </>
                            }></Route>
                            <Route path="/input/:id" element={
                                <>
                                    <Header locationEnabled={false} infoEnabled/>
                                    <Input/>
                                </>
                            }></Route>
                        </Routes>
                    </BrowserRouter>
                </QueryClientProvider>
            </ThemeProvider>
        </Container>
    )
}

export default App
