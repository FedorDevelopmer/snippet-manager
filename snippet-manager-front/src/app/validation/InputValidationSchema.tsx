import * as zod from 'zod';
export const ValidationSchema = zod.object({
    title: zod
        .string()
        .min(3, "Snippet title must be at list 3 characters")
        .max(100, "Snippet title shouln't be longer than 100 characters"),
    code: zod
        .string()
        .min(20, "Code length must be at least 20 characters")
        .max(2000, "Code length should't be longer than 2000 characters")
    
})