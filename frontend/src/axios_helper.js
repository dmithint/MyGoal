import axios from "axios";
import Cookies from "js-cookie";

axios.defaults.baseURL = process.env.REACT_APP_API_URL;
export const baseUrl = process.env.REACT_APP_API_URL;
axios.defaults.headers.post["Content-Type"] = "application/json";

const getAuthToken = () => Cookies.get("token");

export const setAuthHeader = (token) => {
    if (token) {
        Cookies.set("token", token, { expires: 7 });
    } else {
        Cookies.remove("token");
    }
};

axios.interceptors.request.use((config) => {
    const token = getAuthToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export const request = (method, url, data) => {
    return axios({
        method,
        url,
        data,
        withCredentials: true,
        headers: {
            "Content-Type": "application/json",
        },
    });
};
