export interface ProductRequest{
    name: string;
    description: string;
    unitOfMeasure: string;
    reorderPoint: number;
    quantityOnHand: number;   
}

export interface Product extends ProductRequest{
    id: number
}