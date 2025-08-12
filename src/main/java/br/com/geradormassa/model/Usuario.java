package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Usuario {

    @CsvColumn(header = "Nome", order = 1)
    private String nome;

    @CsvColumn(header = "Sobrenome", order = 2)
    private String sobrenome;

    @CsvColumn(header = "NomeCompleto", order = 3)
    private String nomeCompleto;

    @CsvColumn(header = "NomeUsuario", order = 4)
    private String nomeUsuario;

    @CsvColumn(header = "Email", order = 5)
    private String email;

    @CsvColumn(header = "Senha", order = 6)
    private String senha;

    @CsvColumn(header = "Administrador", order = 7)
    private String administrador;

    @CsvColumn(header = "CPF", order = 8)
    private String cpf;

    @CsvColumn(header = "Telefone", order = 9)
    private String telefone;

    @CsvColumn(header = "RazaoSocial", order = 10)
    private String razaoSocial;

    @CsvColumn(header = "CNPJ", order = 11)
    private String cnpj;

    @CsvColumn(header = "Endereco", order = 12)
    private String addressLine;

    // ===== MUDANÇA 3: NOVA COLUNA ADICIONADA =====
    @CsvColumn(header = "NumeroEndereco", order = 13)
    private String numeroEndereco;

    // ===== MUDANÇA 4: ORDENS ATUALIZADAS (incrementadas em 1) =====
    @CsvColumn(header = "Cidade", order = 14)
    private String city;

    @CsvColumn(header = "Estado", order = 15)
    private String stateRegion;

    @CsvColumn(header = "CEP", order = 16)
    private String zipCode;

    @CsvColumn(header = "Pais", order = 17)
    private String country;

    @CsvColumn(header = "NumeroCartao", order = 18)
    private String cardNumber;

    @CsvColumn(header = "ValidadeCartao", order = 19)
    private String expiryDate;
}