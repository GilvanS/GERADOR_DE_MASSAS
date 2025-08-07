import { gerarUsuario } from '../generators/usuarioGenerator';
import { FakerApiData } from '../generators/fakerApi';
import { Usuario } from '../models/Usuario';

describe('Gerador de Usuário', () => {
  it('deve gerar um usuário com dados válidos', () => {
    // 1. Cria um objeto de dados mockado, simulando a resposta da API
    const mockApiData: FakerApiData = {
      firstName: 'João',
      lastName: 'Silva',
      phoneNumber: '99999-9999',
      addressLine: 'Rua Teste',
      city: 'Cidade Teste',
      stateRegion: 'Estado Teste',
      country: 'País Teste',
      cardNumber: '1234-5678-9012-3456',
      expiryDate: '12/29'
    };

    // 2. Gera um usuário usando a função e os dados mockados
    const usuario: Usuario = gerarUsuario(mockApiData);

    // 3. Verifica se o objeto retornado e suas propriedades principais existem
    expect(usuario).toBeDefined();
    expect(usuario.nomeCompleto).toBeDefined();
    expect(usuario.email).toBeDefined();
    expect(usuario.senha).toBeDefined();
    expect(usuario.cpf).toBeDefined();
    expect(usuario.cnpj).toBeDefined();

    // 4. Valida o tipo e se não estão vazios
    expect(typeof usuario.nomeCompleto).toBe('string');
    expect(usuario.nomeCompleto.length).toBeGreaterThan(0);
    expect(typeof usuario.email).toBe('string');
    expect(usuario.email).toContain('@');
    expect(typeof usuario.senha).toBe('string');
    expect(usuario.senha.length).toBeGreaterThan(0);
    expect(typeof usuario.cpf).toBe('string');
    expect(usuario.cpf.length).toBe(11); // CPF deve ter 11 dígitos
    expect(typeof usuario.cnpj).toBe('string');
    expect(usuario.cnpj.length).toBe(14); // CNPJ deve ter 14 dígitos
  });
});
