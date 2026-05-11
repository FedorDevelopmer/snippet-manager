import axios, { type AxiosResponse } from "axios";
import type { SnippetEntity } from "../model/snippet";
import type { Criteria } from "../model/criteria";
import { snippetsAPI } from "./global-api";


export function getSnippetById(id: string): Promise<AxiosResponse<any, any, {}>> {
    return snippetsAPI.get("/" + id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function getSnippets(idx: number, size: number, criteria: Criteria): Promise<AxiosResponse<any, any, {}>> {
    const langTags = criteria.tags ? Array.from(criteria.tags) : []
    return snippetsAPI.get("", {
        headers: {
            "Content-Type": "application/json"
        },
        params: {
            pageNumber: idx,
            size: size,
            title: criteria.title,
            code: criteria.code,
            langTags: langTags
        }
    });
}


export function addSnippet(snippet: SnippetEntity): Promise<AxiosResponse<any, any, {}>> {
    return snippetsAPI.post("", JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function updateSnippet(snippet: SnippetEntity): Promise<AxiosResponse<any, any, {}>> {
    return snippetsAPI.patch("/" + snippet.id, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function deleteSnippet(snippet: SnippetEntity): Promise<AxiosResponse<any, any, {}>> {
    return snippetsAPI.delete("/" + snippet.id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}