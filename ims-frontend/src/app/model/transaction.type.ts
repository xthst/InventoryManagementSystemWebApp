import { Product } from "./product.model";

export enum TransactionType {
    Inbound = 'INBOUND',
    Outbound = 'OUTBOUND'
}

interface BaseTransaction {
    transactionDate: Date;
    transactionType: TransactionType;
    quantity: number;
    reference: string;
}

export interface TransactionRequest extends BaseTransaction {
    productId: number;
}

export interface Transaction extends TransactionRequest {
    id: number;
    product: Product;
    transactionTime: Date;
}