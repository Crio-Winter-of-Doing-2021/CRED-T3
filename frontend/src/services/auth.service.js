import axios from "axios";
import authHeader from "./auth-header";
import { config } from "../utils/constants";

var BASE_API_URL = config.url.API_URL;

class AuthService {
  async login(username, password) {
    return axios
      .post(BASE_API_URL + "/login", {
        password,
        username,
      })
      .then((response) => {
        sessionStorage.setItem("authToken", response.headers.authorization);
        console.log(sessionStorage.getItem("authToken"));
        sessionStorage.setItem("username", username);
        console.log(sessionStorage.getItem("username"));
        return response.headers.authorization;
      });
  }

  async register(username, email, password) {
    return axios.post(BASE_API_URL + "/signup", {
      username,
      email,
      password,
    });
  }

  logout() {
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("userId");
    sessionStorage.removeItem("authToken");
  }

  getAuthToken() {
    return sessionStorage.getItem("authToken");
  }

  getCurrentUser() {
    return sessionStorage.getItem("username");
  }

  async getUserIdByUsername(username) {
    await axios
      .get(BASE_API_URL + "/getUserId/" + username, {
        headers: authHeader(),
      })
      .then((response) => {
        sessionStorage.setItem("userId", response.data.data);
        return response.data.data;
      });
  }
}
export default new AuthService();
