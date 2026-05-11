import { type AxiosResponse } from "axios";
import type { LangTagEntity } from "../model/tag";
import { tagsAPI } from "./global-api";


export function getTagById(id: string): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.get("/" + id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function getTagByName(name: string): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.get("/name", {
        headers: {
            "Content-Type": "application/json"
        },
        params: {
            name: name
        }
    });
}

export function getTags(idx: number, size: number): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.get('', {
        headers: {
            "Content-Type": "application/json"
        },
        params: {
            pageNumber: idx,
            size: size,
        }
    });
}


export function addTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.post('', JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function updateTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.patch("/" + snippet.id, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}


export function deleteTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return tagsAPI.delete("/" + snippet.id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}