import axios from "axios"
import toast from "react-hot-toast"

const tagsAPI = axios.create({ baseURL: "http://localhost:9091/api/v1/tags" });
const snippetsAPI = axios.create({ baseURL: "http://localhost:9091/api/v1/snippets" });

let connectionAborted = false;

function handleErrors(error: any) {
    if (!error.response && !connectionAborted) {
        toast.error("No connection with a server. Check your internet connection");
        connectionAborted = true;
    }
    if (error.response?.status === 500) {
        toast.error("A server error occured. Try again later.");
    }
    if (error.response?.status === 503) {
        toast.error("Server face with heavy workload. Wait for several minutes and try again.");
    }
    if (error.response?.status === 404) {
        toast.error("Not found. Please, check requested page or data.");
    }
    return Promise.reject(error);
}

tagsAPI.interceptors.response.use(
    (response) => {
        connectionAborted = false;
        return response
    },
    (error) => handleErrors(error)
)

snippetsAPI.interceptors.response.use(
    (response) => {
        connectionAborted = false;
        return response
    },
    (error) => handleErrors(error)
)




export { tagsAPI, snippetsAPI };