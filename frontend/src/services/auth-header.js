import AuthService from './auth.service';

export default function authHeader() {
      return { Authorization:  AuthService.getAuthToken()};
  }