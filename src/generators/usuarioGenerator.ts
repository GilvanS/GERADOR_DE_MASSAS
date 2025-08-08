import { faker } from '@faker-js/faker';
import { Usuario } from '../models/Usuario';
import { FakerApiData } from './fakerApi';

const DOMINIOS_EMAIL = [
    "@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com",
    "@icloud.com", "@aol.com", "@protonmail.com", "@zoho.com",
    "@yandex.com", "@gmx.com", "@mail.com", "@live.com",
    "@msn.com", "@me.com", "@uol.com.br", "@bol.com.br",
    "@terra.com.br", "@ig.com.br", "@inbox.com", "@fastmail.com"
];

function removerAcentos(texto: string): string {
    return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

// Funções para gerar documentos brasileiros (substituindo a lógica Java)
function gerarCpf(): string {
    const n = Array.from({ length: 9 }, () => Math.floor(Math.random() * 10));
    const d1 = n.reduce((acc, val, i) => acc + val * (10 - i), 0) % 11;
    n.push(d1 < 2 ? 0 : 11 - d1);
    const d2 = n.reduce((acc, val, i) => acc + val * (11 - i), 0) % 11;
    n.push(d2 < 2 ? 0 : 11 - d2);
    return n.join('');
}

function gerarCnpj(): string {
    const n = Array.from({ length: 8 }, () => Math.floor(Math.random() * 10));
    n.push(0, 0, 0, 1); // Base do CNPJ
    const d1 = n.reduce((acc, val, i) => acc + val * (i < 4 ? 5 - i : 13 - i), 0) % 11;
    n.push(d1 < 2 ? 0 : 11 - d1);
    const d2 = n.reduce((acc, val, i) => acc + val * (i < 5 ? 6 - i : 14 - i), 0) % 11;
    n.push(d2 < 2 ? 0 : 11 - d2);
    return n.join('');
}

function gerarCep(): string {
    return faker.location.zipCode('#####-###');
}

function gerarTelefoneBr(): string {
    const ddd = faker.helpers.arrayElement(['11', '21', '31', '41', '51', '61', '71', '81', '91']);
    const numero = `9${faker.string.numeric(8)}`;
    return `${ddd}${numero}`;
}

export function gerarUsuario(dadosApi: FakerApiData): Usuario {
    const { firstName, lastName, addressLine, city, stateRegion, country } = dadosApi;
    const nomeCompleto = `${firstName} ${lastName}`;

    const nomeUsuario = `${removerAcentos(firstName)}.${removerAcentos(lastName)}`.toLowerCase();
    const dominioAleatorio = faker.helpers.arrayElement(DOMINIOS_EMAIL);
    const email = `${removerAcentos(lastName)}.${removerAcentos(firstName)}${dominioAleatorio}`.toLowerCase();

    const cpf = gerarCpf();
    const cnpj = gerarCnpj();
    const razaoSocial = `${removerAcentos(nomeCompleto)} LTDA`;
    const senha = faker.internet.password({ length: 12, memorable: false, pattern: /\w/ });
    const telefone = gerarTelefoneBr();
    const cardNumber = faker.finance.creditCardNumber().replace(/\D/g, '');
    const expiryDate = faker.date.future({ years: 3 }).toLocaleDateString('pt-BR');

    return {
        nomeCompleto: removerAcentos(nomeCompleto),
        nomeUsuario: nomeUsuario,
        email: email,
        senha: senha,
        administrador: "true",
        cpf: cpf,
        telefone: telefone,
        razaoSocial: razaoSocial,
        cnpj: cnpj,
        addressLine: removerAcentos(addressLine),
        city: removerAcentos(city),
        stateRegion: removerAcentos(stateRegion),
        zipCode: gerarCep(),
        country: removerAcentos(country),
        cardNumber: cardNumber,
        expiryDate: expiryDate,
    };
}
