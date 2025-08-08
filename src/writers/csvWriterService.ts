import { createObjectCsvWriter } from 'csv-writer';
import { parse } from 'csv-parse/sync';
import { Massa } from '../models/Massa';
import * as path from 'path';
import * as fs from 'fs';

const csvHeader = [
    // --- Cabeçalhos de Usuário ---
    { id: 'usuario.nomeCompleto', title: 'Nome Completo' },
    { id: 'usuario.nomeUsuario', title: 'Nome de Usuario' },
    { id: 'usuario.email', title: 'Email' },
    { id: 'usuario.senha', title: 'Senha' },
    { id: 'usuario.administrador', title: 'Administrador' },
    { id: 'usuario.cpf', title: 'CPF' },
    { id: 'usuario.telefone', title: 'Telefone' },
    { id: 'usuario.razaoSocial', title: 'Razao Social' },
    { id: 'usuario.cnpj', title: 'CNPJ' },
    { id: 'usuario.addressLine', title: 'Endereco' },
    { id: 'usuario.city', title: 'Cidade' },
    { id: 'usuario.stateRegion', title: 'Estado' },
    { id: 'usuario.zipCode', title: 'CEP' },
    { id: 'usuario.country', title: 'Pais' },
    { id: 'usuario.cardNumber', title: 'Numero do Cartao' },
    { id: 'usuario.expiryDate', title: 'Validade do Cartao' },
    // --- Cabeçalhos de Produto ---
    { id: 'produto.nomeProduto', title: 'Produto - Nome' },
    { id: 'produto.preco', title: 'Produto - Preco' },
    { id: 'produto.descricaoProduto', title: 'Produto - Descricao' },
    { id: 'produto.quantidade', title: 'Produto - Quantidade' },
    { id: 'produto.nomeCategoria', title: 'Produto - Categoria' },
    { id: 'produto.descricaoCategoria', title: 'Produto - Descricao da Categoria' },
    // --- Cabeçalhos de Artigo ---
    { id: 'artigo.tituloArtigo', title: 'Artigo - Titulo' },
    { id: 'artigo.conteudoArtigo', title: 'Artigo - Conteudo' },
    { id: 'artigo.nomeAutor', title: 'Artigo - Autor' },
    { id: 'artigo.dataPublicacao', title: 'Artigo - Data de Publicacao' },
];

export async function escreverMassaEmCsv(massas: Massa[], filePath: string): Promise<void> {
    if (!massas || massas.length === 0) {
        console.log("Nenhuma massa de dados para escrever.");
        return;
    }

    let registrosExistentes: any[] = [];
    const colunasParaFormatarComoTexto = [
        'CPF', 'CNPJ', 'Telefone', 'CEP', 'Numero do Cartao', 'Validade do Cartao'
    ];

    // 1. Lê os dados existentes do CSV, se houver
    if (fs.existsSync(filePath) && fs.statSync(filePath).size > 0) {
        console.log("\nLendo dados existentes do arquivo CSV...");
        const conteudoArquivo = fs.readFileSync(filePath, 'utf-8');
        registrosExistentes = parse(conteudoArquivo, {
            columns: true,
            delimiter: ';',
            skip_empty_lines: true,
            trim: true,
            // Cast para remover o wrapper de fórmula do Excel e trabalhar com o dado bruto
            cast: (value, context) => {
                if (colunasParaFormatarComoTexto.includes(context.column as string)) {
                    if (typeof value === 'string' && value.startsWith('="') && value.endsWith('"')) {
                        return value.substring(2, value.length - 1);
                    }
                }
                return value;
            }
        });
    }

    const novosRegistros = massas.map(massa => ({
        'Nome Completo': massa.usuario.nomeCompleto,
        'Nome de Usuario': massa.usuario.nomeUsuario,
        'Email': massa.usuario.email,
        'Senha': massa.usuario.senha,
        'Administrador': massa.usuario.administrador,
        'CPF': massa.usuario.cpf,
        'Telefone': massa.usuario.telefone,
        'Razao Social': massa.usuario.razaoSocial,
        'CNPJ': massa.usuario.cnpj,
        'Endereco': massa.usuario.addressLine,
        'Cidade': massa.usuario.city,
        'Estado': massa.usuario.stateRegion,
        'CEP': massa.usuario.zipCode,
        'Pais': massa.usuario.country,
        'Numero do Cartao': massa.usuario.cardNumber,
        'Validade do Cartao': massa.usuario.expiryDate,
        'Produto - Nome': massa.produto.nomeProduto,
        'Produto - Preco': massa.produto.preco,
        'Produto - Descricao': massa.produto.descricaoProduto,
        'Produto - Quantidade': massa.produto.quantidade,
        'Produto - Categoria': massa.produto.nomeCategoria,
        'Produto - Descricao da Categoria': massa.produto.descricaoCategoria,
        'Artigo - Titulo': massa.artigo.tituloArtigo,
        'Artigo - Conteudo': massa.artigo.conteudoArtigo,
        'Artigo - Autor': massa.artigo.nomeAutor,
        'Artigo - Data de Publicacao': massa.artigo.dataPublicacao,
    }));

    // 2. Combina os registros existentes com os novos
    const todosOsRegistros = [...registrosExistentes, ...novosRegistros];

    // 3. Ordena a lista completa pelo nome
    console.log("Ordenando a lista completa de dados...");
    todosOsRegistros.sort((a, b) => {
        const nameA = a['Nome Completo'] || '';
        const nameB = b['Nome Completo'] || '';
        return nameA.localeCompare(nameB);
    });

    // 4. Formata as colunas numéricas como texto para o Excel ANTES de escrever
    const registrosFormatados = todosOsRegistros.map(registro => {
        const registroFormatado = { ...registro };
        for (const coluna of colunasParaFormatarComoTexto) {
            if (registroFormatado[coluna]) {
                // Aplica o wrapper para forçar o Excel a tratar como texto
                registroFormatado[coluna] = `="${registroFormatado[coluna]}"`;
            }
        }
        return registroFormatado;
    });

    // 5. Reescreve o arquivo inteiro com os dados combinados e ordenados
    const csvWriter = createObjectCsvWriter({
        path: filePath,
        header: csvHeader.map(h => ({ id: h.title, title: h.title })),
        fieldDelimiter: ';',
    });

    try {
        console.log(`Escrevendo ${registrosFormatados.length} registros ordenados em ${path.resolve(filePath)}...`);
        await csvWriter.writeRecords(registrosFormatados); // Usa os registros já formatados
        console.log("Arquivo CSV atualizado com sucesso.");
    } catch (error) {
        console.error("Erro ao escrever o arquivo CSV:", error);
        throw error;
    }
}