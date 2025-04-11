export const formatDate = (timestamp?: Date) => {
    return new Intl.DateTimeFormat("en-US", {
        year: "numeric",
        month: "long",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    }).format(new Date(timestamp || ""));
}

export enum ContentType {
    Unknown = "UNKNOWN",
    Image = "IMAGE",
    Text = "TEXT",
    Link = "LINK",
    Video = "VIDEO",
    Pdf = "PDF",
}

export enum InputContentType {
    PNG="image/png",
    JPG="image/jpg",
    TEXT="text/plain",
    LINK="text/html",
    VIDEO_QT="video/quicktime",
    VIDEO_MP4="video/mp4",
    PDF="application/pdf"
}


export function isLocationPopUpRequestedFromSessionStorage() {
    const sessionValue = sessionStorage.getItem('locationPopUpRequested');
    return sessionValue === 'true';
}
