import {useQuery} from "@tanstack/react-query";
import {getContentByToken, getLatestContentMetadata, updateTokenLocation} from "../api/contentapi.ts";
import {useEffect, useState} from "react";
import {ContentType, isLocationPopUpRequestedFromSessionStorage} from "../utils/utils.ts";
import {CircularProgress, Box} from "@mui/material";
import {createFFmpeg, fetchFile} from "@ffmpeg/ffmpeg";
import {useParams} from "react-router-dom";
import PDFViewer from "./PDFVIewer.tsx";
import {Coordinates} from "../api/types.ts";


function Body() {
    const {id} = useParams();

    const [imageSrc, setImageSrc] = useState<string | null>(null);
    const [textSrc, setTextSrc] = useState<string | null>(null);
    const [linkSrc, setLinkSrc] = useState<string | null>(null);
    const [videoSrc, setVideoSrc] = useState<string | null>(null);
    const [pdfSrc, setPdfSrc] = useState<string | null>(null);
    const [videoFormat, setVideoFormat] = useState<string>("video/mp4");
    const [contentType, setContentType] = useState<ContentType>(ContentType.Unknown);

    const {data: contentMetadata, error, isError} = useQuery({
        queryKey: ["latest-content-metadata"],
        queryFn: () => getLatestContentMetadata(id),
        staleTime: Infinity,
        refetchOnWindowFocus: false,
        refetchOnMount: false,
        refetchOnReconnect: false,

    });

    useEffect(() => {
        if (isLocationPopUpRequestedFromSessionStorage()) {
            const sessionCoords = sessionStorage.getItem('sessionCoords');
            if (sessionCoords) {
                try {
                    let coords: Coordinates = JSON.parse(sessionCoords);
                    updateTokenLocation(id, coords.lat, coords.lon);
                } catch (error) {
                    console.error('Failed to parse coordinates from session storage:', error);
                }
            }
        }
    }, []);

    const {data: contentPayload, isSuccess} = useQuery({
        queryKey: ["latest-content-payload", contentMetadata?.publicToken],
        queryFn: () => getContentByToken(contentMetadata?.publicToken),
        enabled: !isError && !!contentMetadata?.publicToken,
    })

    const ffmpeg = createFFmpeg({
        corePath: "/src/assets/ffmpeg-core/ffmpeg-core.js", // Point to a locally hosted file
        log: true
    });
    const fetchAndConvertVideo = async (videoFormat: string) => {
        await ffmpeg.load();
        ffmpeg.FS("writeFile", "input.mov", await fetchFile(contentPayload!));
        await ffmpeg.run("-i", "input.mov", "-vf",
            "scale='if(gte(iw,ih),720,-2)':'if(gte(ih,iw),720,-2)'", "output.mp4");
        const data = ffmpeg.FS("readFile", "output.mp4");
        const mp4Blob = new Blob([data.buffer], {type: videoFormat});

        const videoUrl = URL.createObjectURL(mp4Blob);
        setVideoSrc(videoUrl);
    }


    useEffect(() => {
            if (isSuccess && contentPayload) {

                if (contentMetadata?.type.startsWith("image/")) {
                    setContentType(ContentType.Image);
                    const imageUrl = URL.createObjectURL(contentPayload);
                    setImageSrc(imageUrl);
                    return () => {
                        URL.revokeObjectURL(imageUrl);
                    }

                } else if (contentMetadata?.type.startsWith("text/plain")) {
                    setContentType(ContentType.Text);
                    contentPayload.text().then((text) => {
                        setTextSrc(text);
                    });

                } else if (contentMetadata?.type.startsWith("text/html")) {
                    setContentType(ContentType.Link);
                    contentPayload.text().then((text) => {
                        setLinkSrc(text);
                    });
                } else if (contentMetadata?.type.startsWith("video/quicktime")) {
                    setContentType(ContentType.Video);
                    let mp4Format = "video/mp4";
                    fetchAndConvertVideo(mp4Format);
                    setVideoFormat(mp4Format);

                    return () => {
                        URL.revokeObjectURL(videoSrc || "");
                    }
                } else if (contentMetadata?.type.startsWith("video/mp4")) {
                    setContentType(ContentType.Video);
                    const videoUrl = URL.createObjectURL(contentPayload);
                    setVideoSrc(videoUrl);

                    return () => {
                        URL.revokeObjectURL(videoSrc || "");
                    }
                } else if (contentMetadata?.type.startsWith("application/pdf")) {
                    setContentType(ContentType.Pdf);
                    const pdfUrl = URL.createObjectURL(contentPayload);
                    setPdfSrc(pdfUrl);

                    return () => {
                        URL.revokeObjectURL(pdfSrc || "");
                    }
                } else {
                    setContentType(ContentType.Unknown);
                }
            }
        }, [isSuccess, contentPayload]
    );

    // let contentType = queryClient.getQueryData<ContentType>(["content-type"]);

    // CHECK IF ERROR RECEIVED AS RESPONSE. most probably token is invalid.
    if (isError) {
        if (error instanceof Error) {
            const statusCode = (error as any).response?.status;
            const message = (error as any).response?.message;
            console.log('Response status:', statusCode);
            console.log('Response message:', message);
            return (
                <Box sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh'}}>
                    Invalid token.
                </Box>
            )
        }
    } else if (contentType == ContentType.Unknown) { // Token is valid. start rendering response.
        return (<Box>Unknown content type</Box>);
    } else if (contentType == ContentType.Text) {
        return (<Box>{textSrc}</Box>);
    } else if (contentType == ContentType.Image) {
        return (<Box>
            {imageSrc ? (
                <img
                    src={imageSrc}
                    alt="Loaded from Blob"
                    style={{maxWidth: "100%"}}
                />
            ) : (
                "Loading..."
            )}
        </Box>);
    } else if (contentType == ContentType.Link) {
        return (<Box>
            <a href={linkSrc ? linkSrc : "#"} target="_blank" rel="noreferrer">
                {linkSrc}
            </a>
        </Box>)

    } else if (contentType == ContentType.Video) {
        return (
            <Box sx={{maxWidth: '100vw', display: 'flex', justifyContent: 'center', alignItems: 'center',}}>
                <video controls autoPlay loop style={{maxWidth: '100%'}}>
                    <source src={videoSrc || ""} type={videoFormat}/>
                </video>
            </Box>
        )
    } else if (contentType == ContentType.Pdf) {
        console.log("AAAAA:", pdfSrc);
        return (
            <Box sx={{maxWidth: '100vw', display: 'flex', justifyContent: 'center', alignItems: 'center',}}>
                <PDFViewer pdfUrl={pdfSrc || ""}/>
            </Box>
        )
    } else {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh'}}>
                <CircularProgress/>
            </Box>
        )
    }
}

export default Body;