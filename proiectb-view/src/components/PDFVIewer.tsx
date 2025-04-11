import { Box} from '@mui/material';

interface PDFViewerProps {
    pdfUrl: string;
}

function PDFViewer({ pdfUrl }: PDFViewerProps) {
    return (
        <Box sx={{
            width: '100%',
            height: '80vh',
            overflow: 'hidden',
            border: '1px solid #ccc',
            borderRadius: '4px',
            '& iframe': {
                width: '100%',
                height: '100%',
                border: 'none',
                display: 'block'
            }
        }}>
            <iframe
                src={`${pdfUrl}#toolbar=1&navpanes=1&scrollbar=1`}
            />
        </Box>
    );

}

export default PDFViewer;
