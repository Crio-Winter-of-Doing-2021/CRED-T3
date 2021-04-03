import axios from 'axios';
import authHeader from './auth-header';
import API_URL from './auth.service';

class CardService {

    async getAllCardsByUsername(username) {
        const response = await axios
            .get('http://localhost:5000/cards/get/' + username, { 
                headers: authHeader()
            });
            console.log(response);
        return response.data.cards;
    }
}

export default new CardService();