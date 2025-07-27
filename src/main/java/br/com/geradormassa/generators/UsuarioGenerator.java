package br.com.geradormassa.generators;

import br.com.geradormassa.model.Usuario;
import br.com.geradormassa.utils.StringUtils;

import java.text.Normalizer;

public class UsuarioGenerator implements Gerador<Usuario> {

    private final FakerApiData dadosApi;

    // O gerador agora recebe os dados da API para não precisar fazer uma nova chamada.
    // Isso melhora o desempenho e a coesão.
    public UsuarioGenerator(FakerApiData dadosApi) {
        this.dadosApi = dadosApi;
    }

    @Override
    public Usuario gerar() {
        String firstName = dadosApi.getFirstName();
        String lastName = dadosApi.getLastName();
        String nomeCompleto = firstName + " " + lastName;

        String nomeUsuario = normalizeString(firstName) + "." + normalizeString(lastName);
        String email = normalizeString(lastName) + "." + normalizeString(firstName) + "@gmail.com";

        String cpf = DocumentosGenerator.gerarCpf(false);
        String cnpj = DocumentosGenerator.gerarCnpj(false);
        String razaoSocial = StringUtils.removerAcentos(nomeCompleto) + " LTDA";
        String senha = PasswordGenerator.gerarSenhaCustomizada(nomeCompleto, cpf);
        String telefone = dadosApi.getPhoneNumber().replaceAll("[^\\d]", "");

        return Usuario.builder()
                .nomeCompleto(StringUtils.removerAcentos(nomeCompleto))
                .nomeUsuario(nomeUsuario)
                .email(email)
                .senha(senha)
                .administrador("true")
                .cpf(cpf)
                .telefone(telefone)
                .razaoSocial(razaoSocial)
                .cnpj(cnpj)
                .addressLine(StringUtils.removerAcentos(dadosApi.getAddressLine()))
                .city(StringUtils.removerAcentos(dadosApi.getCity()))
                .stateRegion(StringUtils.removerAcentos(dadosApi.getStateRegion()))
                .zipCode(DocumentosGenerator.gerarCep(false))
                .country(StringUtils.removerAcentos(dadosApi.getCountry()))
                .cardNumber(dadosApi.getCardNumber())
                .expiryDate(dadosApi.getExpiryDate())
                .build();
    }

    private String normalizeString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String normalized = Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}