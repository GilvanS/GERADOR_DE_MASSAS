import * as fs from 'fs';
import * as path from 'path';
import { gerarListaDeMassa } from './services/geradorService';
import { escreverMassaEmCsv } from './writers/csvWriterService';

const QUANTIDADE_REGISTROS = 50;
const DIRETORIO_SAIDA = 'output';
const NOME_ARQUIVO = 'massaDeDados.csv';

async function main() {
    console.log("Iniciando a geração de massa de dados...");

    // Garante que o diretório de saída exista
    if (!fs.existsSync(DIRETORIO_SAIDA)) {
        fs.mkdirSync(DIRETORIO_SAIDA, { recursive: true });
    }

    const caminhoCompleto = path.join(DIRETORIO_SAIDA, NOME_ARQUIVO);

    try {
        // 1. Gera a lista de dados
        const massas = await gerarListaDeMassa(QUANTIDADE_REGISTROS);

        // 2. Escreve os dados no arquivo CSV
        await escreverMassaEmCsv(massas, caminhoCompleto);

        console.log(`\nProcesso de geração de massa finalizado com sucesso!`);
        console.log(`Arquivo salvo em: ${path.resolve(caminhoCompleto)}`);

    } catch (error) {
        console.error("\nOcorreu um erro durante o processo de geração de massa:", error);
        process.exit(1); // Encerra o processo com código de erro
    }
}

main();
