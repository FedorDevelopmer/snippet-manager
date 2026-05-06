import axios, { type AxiosResponse } from "axios";
import type { LangTagEntity } from "../model/tag";

const url = "http://localhost:9091/api/v1/tags"

export function getTagById(id: string): Promise<AxiosResponse<any, any, {}>> {
    return axios.get(url + "/" + id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function getTagByName(name: string): Promise<AxiosResponse<any, any, {}>> {
    return axios.get(url + "/name", {
        headers: {
            "Content-Type": "application/json"
        },
        params: {
            name: name
        }
    });
}

export function getTags(idx: number, size: number): Promise<AxiosResponse<any, any, {}>> {
    return axios.get(url, {
        headers: {
            "Content-Type": "application/json"
        },
        params: {
            pageNumber : idx,
            size: size,
        }
    });
}


export function addTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return axios.post(url, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}

export function updateTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return axios.patch(url + "/" + snippet.id, JSON.stringify(snippet), {
        headers: {
            "Content-Type": "application/json"
        }
    });
}


export function deleteTag(snippet: LangTagEntity): Promise<AxiosResponse<any, any, {}>> {
    return axios.delete(url + "/" + snippet.id, {
        headers: {
            "Content-Type": "application/json"
        }
    });
}