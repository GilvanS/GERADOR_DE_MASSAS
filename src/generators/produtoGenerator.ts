import { faker } from '@faker-js/faker';
import { Produto } from '../models/Produto';

const NOMES_PRODUTO = [
    "Smartphone", "Notebook", "Fone de Ouvido", "Smartwatch", "Tablet", "Camera Digital",
    "Teclado Mecanico", "Mouse Gamer", "Monitor 4K", "Cadeira Gamer", "Impressora 3D",
    "Projetor HD", "SSD Externo", "Power Bank", "Roteador Wi-Fi 6", "Placa de Video",
    "Processador", "Memoria RAM", "Cooler CPU", "Gabinete ATX", "Webcam Full HD",
    "Microfone Condensador", "Mesa Digitalizadora", "Drone", "Soundbar", "Smart TV 8K",
    "Console de Videogame", "Oculos de Realidade Virtual", "Leitor de E-book", "HD Externo"
];

const CATEGORIAS = [
    "Eletronicos", "Informatica", "Acessorios", "Fotografia", "Perifericos", "Games",
    "Hardware", "Redes e Conectividade", "Audio e Video", "Componentes", "Casa Inteligente",
    "Dispositivos Moveis", "Armazenamento", "Energia", "Impressao"
];

const ADJETIVOS = [
    "Moderno", "Potente", "Sem Fio", "Ultra-fino", "Profissional", "RGB", "Ergonomica",
    "Compacto", "Resistente", "Silencioso", "Veloz", "Inteligente", "Premium", "Essencial",
    "Versatil", "Sustentavel", "Customizavel", "Portatil", "Imersivo", "Conectado"
];

function removerAcentos(texto: string): string {
    return texto.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
}

export function gerarProduto(): Produto {
    const categoria = faker.helpers.arrayElement(CATEGORIAS);
    const nomeBase = faker.helpers.arrayElement(NOMES_PRODUTO);
    const adjetivo = faker.helpers.arrayElement(ADJETIVOS);
    const nomeProduto = `${nomeBase} ${adjetivo}`;

    const descricao = `Um excelente ${nomeBase.toLowerCase()} ${adjetivo.toLowerCase()} da categoria ${categoria}, ideal para suas necessidades.`;
    const preco = faker.commerce.price({ min: 50, max: 4999, dec: 2 });
    const quantidade = faker.number.int({ min: 1, max: 100 });
    const descricaoCategoria = `Descricao para a categoria ${categoria}`;

    return {
        nomeProduto: removerAcentos(nomeProduto),
        preco: preco,
        descricaoProduto: removerAcentos(descricao),
        quantidade: quantidade,
        nomeCategoria: removerAcentos(categoria),
        descricaoCategoria: removerAcentos(descricaoCategoria),
    };
}
