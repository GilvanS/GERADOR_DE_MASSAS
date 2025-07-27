package br.com.geradormassa.model;

import br.com.geradormassa.writers.CsvColumn;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Usuario {

    @CsvColumn(header = "NomeCompleto", order = 1)
    private String nomeCompleto;

    @CsvColumn(header = "NomeUsuario", order = 2)
    private String nomeUsuario;

    @CsvColumn(header = "Email", order = 3)
    private String email;

    @CsvColumn(header = "Senha", order = 4)
    private String senha;

    @CsvColumn(header = "Administrador", order = 5)
    private String administrador;

    @CsvColumn(header = "CPF", order = 6)
    private String cpf;

    @CsvColumn(header = "Telefone", order = 7)
    private String telefone;

    @CsvColumn(header = "RazaoSocial", order = 8)
    private String razaoSocial;

    @CsvColumn(header = "CNPJ", order = 9)
    private String cnpj;

    @CsvColumn(header = "Endereco", order = 10)
    private String addressLine;

    @CsvColumn(header = "Cidade", order = 11)
    private String city;

    @CsvColumn(header = "Estado", order = 12)
    private String stateRegion;

    @CsvColumn(header = "CEP", order = 13)
    private String zipCode;

    @CsvColumn(header = "Pais", order = 14)
    private String country;

    @CsvColumn(header = "NumeroCartao", order = 15)
    private String cardNumber;

    @CsvColumn(header = "ValidadeCartao", order = 16)
    private String expiryDate;
}