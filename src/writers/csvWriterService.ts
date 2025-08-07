import { createObjectCsvWriter } from 'csv-writer';
import { stringify } from 'csv-stringify/sync';
import { Massa } from '../models/Massa';
import * as path from 'path';
import * as fs from 'fs';

const csvHeader = [
    // Cabeçalhos...
    { id: 'usuario.nomeCompleto', title: 'NomeCompleto' },
    { id: 'usuario.nomeUsuario', title: 'NomeUsuario' },
    { id: 'usuario.email', title: 'Email' },
    { id: 'usuario.senha', title: 'Senha' },
    { id: 'usuario.administrador', title: 'Administrador' },
    { id: 'usuario.cpf', title: 'CPF' },
    { id: 'usuario.telefone', title: 'Telefone' },
    { id: 'usuario.razaoSocial', title: 'RazaoSocial' },
    { id: 'usuario.cnpj', title: 'CNPJ' },
    { id: 'usuario.addressLine', title: 'Endereco' },
    { id: 'usuario.city', title: 'Cidade' },
    { id: 'usuario.stateRegion', title: 'Estado' },
    { id: 'usuario.zipCode', title: 'CEP' },
    { id: 'usuario.country', title: 'Pais' },
    { id: 'usuario.cardNumber', title: 'NumeroCartao' },
    { id: 'usuario.expiryDate', title: 'ValidadeCartao' },
    { id: 'produto.nomeProduto', title: 'NomeProduto' },
    { id: 'produto.preco', title: 'Preco' },
    { id: 'produto.descricaoProduto', title: 'DescricaoProduto' },
    { id: 'produto.quantidade', title: 'Quantidade' },
    { id: 'produto.nomeCategoria', title: 'NomeCategoria' },
    { id: 'produto.descricaoCategoria', title: 'DescricaoCategoria' },
    { id: 'artigo.tituloArtigo', title: 'TituloArtigo' },
    { id: 'artigo.conteudoArtigo', title: 'ConteudoArtigo' },
    { id: 'artigo.nomeAutor', title: 'NomeAutor' },
    { id: 'artigo.dataPublicacao', title: 'DataPublicacao' },
];

export async function escreverMassaEmCsv(massas: Massa[], filePath: string): Promise<void> {
    if (!massas || massas.length === 0) {
        console.log("Nenhuma massa de dados para escrever.");
        return;
    }

    const fileExists = fs.existsSync(filePath);

    const records = massas.map(massa => ({
        // Mapeamento...
        'usuario.nomeCompleto': massa.usuario.nomeCompleto,
        'usuario.nomeUsuario': massa.usuario.nomeUsuario,
        'usuario.email': massa.usuario.email,
        'usuario.senha': massa.usuario.senha,
        'usuario.administrador': massa.usuario.administrador,
        'usuario.cpf': massa.usuario.cpf,
        'usuario.telefone': massa.usuario.telefone,
        'usuario.razaoSocial': massa.usuario.razaoSocial,
        'usuario.cnpj': massa.usuario.cnpj,
        'usuario.addressLine': massa.usuario.addressLine,
        'usuario.city': massa.usuario.city,
        'usuario.stateRegion': massa.usuario.stateRegion,
        'usuario.zipCode': massa.usuario.zipCode,
        'usuario.country': massa.usuario.country,
        'usuario.cardNumber': massa.usuario.cardNumber,
        'usuario.expiryDate': massa.usuario.expiryDate,
        'produto.nomeProduto': massa.produto.nomeProduto,
        'produto.preco': massa.produto.preco,
        'produto.descricaoProduto': massa.produto.descricaoProduto,
        'produto.quantidade': massa.produto.quantidade,
        'produto.nomeCategoria': massa.produto.nomeCategoria,
        'produto.descricaoCategoria': massa.produto.descricaoCategoria,
        'artigo.tituloArtigo': massa.artigo.tituloArtigo,
        'artigo.conteudoArtigo': massa.artigo.conteudoArtigo,
        'artigo.nomeAutor': massa.artigo.nomeAutor,
        'artigo.dataPublicacao': massa.artigo.dataPublicacao,
    }));

    if (fileExists) {
        // Se o arquivo existe, converte os dados para string e anexa manualmente
        const csvString = stringify(records, {
            header: false, // Não incluir cabeçalho na string
            delimiter: ';',
            columns: csvHeader.map(h => h.id)
        });

        try {
            fs.appendFileSync(filePath, csvString);
            console.log(`Dados adicionados ao arquivo ${path.resolve(filePath)} com sucesso.`);
        } catch (error) {
            console.error("Erro ao adicionar dados ao arquivo CSV:", error);
            throw error;
        }
    } else {
        // Se o arquivo não existe, usa o csv-writer para criá-lo com cabeçalho
        const csvWriter = createObjectCsvWriter({
            path: filePath,
            header: csvHeader,
            fieldDelimiter: ';',
        });

        try {
            console.log(`Criando e escrevendo dados em ${path.resolve(filePath)}...`);
            await csvWriter.writeRecords(records);
            console.log("Arquivo CSV criado com sucesso.");
        } catch (error) {
            console.error("Erro ao criar o arquivo CSV:", error);
            throw error;
        }
    }
}
