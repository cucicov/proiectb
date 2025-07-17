// import {useParams} from "react-router-dom";
import {Box, Button, Stack, TextField} from "@mui/material";
import {useState, useEffect} from "react";
import Location from "./Location.tsx";
import {useParams} from "react-router-dom";
import {uploadInput} from "../api/contentapi.ts";
import {Coordinates} from "../api/types.ts";
import {InputContentType} from "../utils/utils.ts";


function Input() {
    const {id} = useParams();
    const options = ['Text', 'PDF', 'Link', 'Image', 'Video'];

    const [contentTypeSelected, setContentTypeSelected] = useState(options[0]);
    const [textValue, setTextValue] = useState('');
    const [urlValue, setUrlValue] = useState('');
    const [isUrlValid, setIsUrlValid] = useState(false);
    const [urlError, setUrlError] = useState('');

    // Validate URL format
    const validateUrl = (url: string) => { //TODO: maybe move this to external file?
        // Empty URL is considered invalid but without error message
        if (!url.trim()) {
            setIsUrlValid(false);
            setUrlError('');
            return;
        }

        // URL validation regex pattern
        const urlPattern = /^(https?:\/\/)?(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)$/;

        if (urlPattern.test(url)) {
            setIsUrlValid(true);
            setUrlError('');
        } else {
            setIsUrlValid(false);
            setUrlError('Please enter a valid URL (e.g., https://example.com)');
        }
    };

    // Validate URL when it changes
    useEffect(() => {
        if (contentTypeSelected === options[2]) { // Link option
            validateUrl(urlValue);
        }
    }, [urlValue, contentTypeSelected]);


    return (
        <Box sx={{
            mt: 1,
            display: 'flex',
            gap: 2,
            flexDirection: {
                xs: 'column',
                md: 'row'
            },
            justifyContent: 'center',
            alignItems: {
                xs: 'center',    // Centers content when in column direction
                md: 'stretch'    // Default behavior for row direction
            }

        }}>

            <Box sx={{
                order: {
                    xs: 1,  // second on mobile
                    md: 2   // first on desktop
                }
            }}>
                <Location/>
            </Box>

            <Box sx={{
                flexGrow: 1,
                order: {
                    xs: 2,  // second on mobile
                    md: 1   // first on desktop
                },
            }}>
                <h3>Choose one type of the content to upload</h3>
                <Stack direction="row" spacing={1}>
                    {options.map((option) => (
                        <Button
                            key={option}
                            variant={contentTypeSelected === option ? "contained" : "outlined"}
                            onClick={() => {
                                setContentTypeSelected(option)
                            }}
                            color="secondary"
                        >
                            {option}
                        </Button>
                    ))}
                </Stack>

                {/******INPUT TEXT******/}
                {contentTypeSelected == options[0] && <>
                    <TextField
                        fullWidth
                        label="Enter text here"
                        variant="outlined"
                        multiline
                        rows={4}
                        value={textValue}
                        onChange={(e) => setTextValue(e.target.value)}
                        sx={{mb: 2, mt: 2}}
                    />
                    <Button
                        key='submit-text'
                        variant='contained'
                        color='primary'
                        onClick={() => {
                            let item = sessionStorage.getItem('sessionCoords');
                            let coords: Coordinates | null = item ? JSON.parse(item) : null;

                            const byteArray = Array.from(new TextEncoder().encode(textValue));

                            uploadInput({
                                type: InputContentType.TEXT,
                                publicToken: id!,
                                latitude: coords?.lat,
                                longitude: coords?.lon,
                                data: byteArray
                            });
                        }}>
                        Submit
                    </Button>
                </>}

                {/******INPUT LINK******/}
                {contentTypeSelected == options[2] && <>
                    <TextField
                        fullWidth
                        label="Enter URL here"
                        variant="outlined"
                        value={urlValue}
                        onChange={(e) => setUrlValue(e.target.value)}
                        error={!!urlError}
                        helperText={urlError}
                        sx={{mb: 2, mt: 2}}
                    />
                    <Button
                        key='submit-link'
                        variant='contained'
                        color='primary'
                        disabled={!isUrlValid}
                        onClick={() => {
                            let item = sessionStorage.getItem('sessionCoords');
                            let coords: Coordinates | null = item ? JSON.parse(item) : null;

                            const byteArray = Array.from(new TextEncoder().encode(urlValue));

                            uploadInput({
                                type: InputContentType.LINK,
                                publicToken: id!,
                                latitude: coords?.lat,
                                longitude: coords?.lon,
                                data: byteArray
                            });
                        }}>
                        Submit
                    </Button>
                </>}
            </Box>

        </Box>

    )
}

export default Input;
