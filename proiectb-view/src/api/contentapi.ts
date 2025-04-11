import axios from "axios";
import {ContentMetadataResponse, Input} from "./types.ts";


export const getLatestContentMetadata =
    async (token: string | undefined): Promise<ContentMetadataResponse> => {
        const response =
            await axios.get(`${import.meta.env.VITE_PROIECTB_URL}/recentcontent/metadata/${token}`);
        return response.data;
    }

export const updateTokenLocation=
    async (token: string | undefined, latitude: number, longitude: number ): Promise<ContentMetadataResponse> => {
        if (!token) {
            throw new Error('Token is required');
        }

        const response =
            await axios.post(`${import.meta.env.VITE_PROIECTB_URL}/recentcontent/metadata/${token}`, {
                latitude,
                longitude
            });
        return response.data;
    }

export const getContentByToken =
    async (token: string | undefined): Promise<Blob> => {
        const response =
            await axios.get(`${import.meta.env.VITE_PROIECTB_URL}/content/${token}`,
                {
                    responseType: 'blob'
                });
        return response.data;
    }

export const uploadInput =
    async (input: Input) => {
        if (!input) {
            throw new Error('Token is required');
        }
    }
