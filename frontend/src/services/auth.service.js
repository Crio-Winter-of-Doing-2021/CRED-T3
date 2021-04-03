import axios from 'axios';
import authHeader from './auth-header';

// http://credbackend-env.eba-i66gtx8n.ap-south-1.elasticbeanstalk.com

export const API_URL = "http://localhost:5000";

class AuthService {

    async login(username, password) {
        return axios
            .post(API_URL + '/login', {
                password,
                username
            })
            .then(response => {
                sessionStorage.setItem('authToken', response.headers.authorization);
                console.log(sessionStorage.getItem('authToken'));
                sessionStorage.setItem('username', username);
                console.log(sessionStorage.getItem('username'));
                return response.headers.authorization;
            });
    }

    logout() {
        sessionStorage.removeItem('username');
        sessionStorage.removeItem('userId');
        sessionStorage.removeItem('authToken');
    }

    getAuthToken() {
        return sessionStorage.getItem('authToken');
    }

    getCurrentUser() {
        return sessionStorage.getItem('username');
    }

    async getUserIdByUsername(username) {
        await axios.get(API_URL + "/getUserId/" + username, {
            headers: authHeader()
        }).then(response => {
            sessionStorage.setItem('userId', response.data.data);
            return response.data.data;
        });
    }
}
export default new AuthService();
