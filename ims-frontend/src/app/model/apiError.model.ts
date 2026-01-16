export interface ApiError {
    status: number;
    message: string;
    timestamp: Date;
    errors: Map<string, string>;
}
