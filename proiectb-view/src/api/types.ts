export type ContentMetadataResponse = {
    type: string;
    activationTimestamp: Date;
    expirationTimestamp: Date;
    publicToken: string;
}

export type Coordinates = {
    lat: number;
    lon: number;
}

export type Input = {
    type: string;
    publicToken: string;
    latitude: number | undefined;
    longitude: number | undefined;
    data: number[];
}