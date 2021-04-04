const prod = {
  url: {
    API_URL: 'http://credbackend-env.eba-i66gtx8n.ap-south-1.elasticbeanstalk.com',
    CARD_API_URL: 'http://credbackend-env.eba-i66gtx8n.ap-south-1.elasticbeanstalk.com/cards/'
  }
}

const dev = {
  url: {
    API_URL: 'http://localhost:5000',
    CARD_API_URL: 'http://localhost:5000/cards/'
  }
}

export const config = process.env.NODE_ENV === "development" ? dev : prod;
