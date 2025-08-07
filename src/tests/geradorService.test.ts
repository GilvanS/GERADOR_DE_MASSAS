import * as fs from 'fs';
import * as path from 'path';
import { gerarListaDeMassa } from '../services/geradorService';
import { escreverMassaEmCsv } from '../writers/csvWriterService';
import * as fakerApi from '../generators/fakerApi'; // Importa o módulo para mockar
import { FakerApiData } from '../generators/fakerApi';

// Define o diretório e o nome do arquivo de teste
const TEST_DIR = path.join(__dirname);
const TEST_FILE = path.join(TEST_DIR, 'massaDeTeste.csv');

// Mock da função gerarDadosDaApi
jest.mock('../generators/fakerApi');

describe('Processo de Geração de Massa e Escrita em CSV', () => {

  beforeEach(() => {
    // Define o que a função mockada deve retornar
    const mockApiData: FakerApiData = {
      firstName: 'João',
      lastName: 'Silva',
      phoneNumber: '99999-9999',
      addressLine: 'Rua Teste Mockado',
      city: 'Cidade Mockada',
      stateRegion: 'Estado Mockado',
      country: 'País Mockado',
      cardNumber: '1234-5678-9012-3456',
      expiryDate: '12/29'
    };
    // Configura o mock para retornar os dados de exemplo
    (fakerApi.gerarDadosDaApi as jest.Mock).mockResolvedValue(mockApiData);
  });

  afterAll(() => {
    if (fs.existsSync(TEST_FILE)) {
      fs.unlinkSync(TEST_FILE);
    }
  });

  it('deve gerar uma lista de massa e escrevê-la em um arquivo CSV', async () => {
    const quantidadeRegistros = 2;

    const massas = await gerarListaDeMassa(quantidadeRegistros);

    expect(massas).toHaveLength(quantidadeRegistros);
    expect(massas[0].usuario.nomeCompleto).toBe('Joao Silva'); // Nome sem acento, conforme a regra
    expect(massas[0].produto.nomeProduto).toBeDefined();
    expect(massas[0].artigo.tituloArtigo).toBeDefined();

    await escreverMassaEmCsv(massas, TEST_FILE);

    const arquivoExiste = fs.existsSync(TEST_FILE);
    expect(arquivoExiste).toBe(true);

    const conteudoCsv = fs.readFileSync(TEST_FILE, 'utf-8');
    const linhas = conteudoCsv.trim().split('\n');

    expect(linhas).toHaveLength(quantidadeRegistros + 1);

    const cabecalho = linhas[0];
    expect(cabecalho).toContain('NomeCompleto');
    expect(cabecalho).toContain('NomeProduto');
    expect(cabecalho).toContain('TituloArtigo');
  });
});