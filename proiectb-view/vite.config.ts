import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import fs from 'node:fs';

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        https: {
            key: fs.readFileSync('certs/key.pem'),
            cert: fs.readFileSync('certs/cert.pem'),
        },
        headers: {
            'Cross-Origin-Embedder-Policy': 'require-corp',
            'Cross-Origin-Opener-Policy': 'same-origin',
        },
        host: '192.168.1.221',  // Ensure it's accessible over WiFi
        strictPort: true,
        port: 5173,
    },
    preview: {
        https: {
            key: fs.readFileSync('certs/key.pem'),
            cert: fs.readFileSync('certs/cert.pem'),
        },
        headers: {
            'Cross-Origin-Embedder-Policy': 'require-corp',
            'Cross-Origin-Opener-Policy': 'same-origin',
        },
        host: '192.168.1.221',  // Ensure it's accessible over WiFi
        strictPort: true,
        port: 5173, // customize preview server port
    }

})
