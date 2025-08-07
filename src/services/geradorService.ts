import { Massa } from '../models/Massa';
import { gerarDadosDaApi } from '../generators/fakerApi';
import { gerarUsuario } from '../generators/usuarioGenerator';
import { gerarProduto } from '../generators/produtoGenerator';
import { gerarArtigo } from '../generators/artigoGenerator';

/**
 * Orquestra a geração completa de um objeto de Massa.
 * Busca dados da API e depois chama os geradores específicos.
 */
export async function gerarMassa(): Promise<Massa> {
    // 1. Busca os dados base da API
    const dadosApi = await gerarDadosDaApi();

    // 2. Gera cada parte da massa de dados
    const usuario = gerarUsuario(dadosApi);
    const produto = gerarProduto();
    const artigo = gerarArtigo(dadosApi);

    // 3. Combina tudo em um único objeto
    return {
        usuario,
        produto,
        artigo,
    };
}

/**
 * Gera uma lista de objetos de Massa.
 * @param quantidade O número de registros a serem gerados.
 * @returns Uma promessa que resolve para uma lista de Massas.
 */
export async function gerarListaDeMassa(quantidade: number): Promise<Massa[]> {
    const massas: Massa[] = [];
    console.log(`Iniciando a geração de ${quantidade} registros...`);

    for (let i = 0; i < quantidade; i++) {
        const massa = await gerarMassa();
        massas.push(massa);
        process.stdout.write(`\rRegistros gerados: ${i + 1}/${quantidade}`);
    }

    console.log('\n\nGeração concluída.');
    return massas;
}
