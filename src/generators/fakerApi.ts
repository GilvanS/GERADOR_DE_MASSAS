import axios from 'axios';
import { faker } from '@faker-js/faker';

const API_URL = 'https://fakerapi.it/api/v1/custom?_quantity=1&_locale=pt_BR';

export interface FakerApiData {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    addressLine: string;
    city: string;
    stateRegion: string;
    country: string;
    cardNumber: string;
    expiryDate: string;
}

function getMockData(): FakerApiData {
    const firstName = faker.person.firstName();
    const lastName = faker.person.lastName();
    return {
        firstName: firstName,
        lastName: lastName,
        phoneNumber: faker.phone.number(),
        addressLine: faker.location.streetAddress(),
        city: faker.location.city(),
        stateRegion: faker.location.state(),
        country: 'Brasil',
        cardNumber: faker.finance.creditCardNumber(),
        expiryDate: `${faker.number.int({ min: 1, max: 12 }).toString().padStart(2, '0')}/${faker.number.int({ min: 25, max: 30 })}`,
    };
}

export async function gerarDadosDaApi(): Promise<FakerApiData> {
    const params = {
        firstName: 'firstName',
        lastName: 'lastName',
        phoneNumber: 'phone',
        addressLine: 'streetAddress',
        city: 'city',
        stateRegion: 'state',
        country: 'country',
        cardNumber: 'creditCardNumber',
        expiryDate: 'creditCardExpDate',
    };

    try {
        const response = await axios.get(API_URL, { params });
        const apiData = response.data.data[0];

        if (!apiData) {
            throw new Error("A resposta da API não contém dados.");
        }

        return {
            firstName: apiData.firstName,
            lastName: apiData.lastName,
            phoneNumber: apiData.phoneNumber,
            addressLine: apiData.addressLine,
            city: apiData.city,
            stateRegion: apiData.stateRegion,
            country: apiData.country,
            cardNumber: apiData.cardNumber,
            expiryDate: apiData.expiryDate,
        };
    } catch (error) {
        console.warn("\nAVISO: Falha ao buscar dados da API externa. Usando dados de fallback locais.");
        return getMockData(); // Retorna dados mockados em caso de erro
    }
}
