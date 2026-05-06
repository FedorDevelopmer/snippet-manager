import axios, { type AxiosResponse } from "axios";
import type { SnippetEntity } from "../model/snippet";
import type { Criteria } from "../model/criteria";

const url = "http://localhost:9091/api/v1/snippets"

export function getSnippetById(id: string): Promise<AxiosResponse<any, any, {}>> {
    return axios.get(url + "/" + id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function getSnippets(idx: number, size: number, criteria: Criteria): Promise<AxiosResponse<any, any, {}>> {
    const langTags = criteria.tags ? Array.from(criteria.tags) : []
    return axios.get(url, {
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
    return axios.post(url, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function updateSnippet(snippet: SnippetEntity): Promise<AxiosResponse<any, any, {}>> {
    return axios.patch(url + "/" + snippet.id, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function deleteSnippet(snippet: SnippetEntity): Promise<AxiosResponse<any, any, {}>> {
    return axios.delete(url + "/" + snippet.id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}