package br.com.geradormassa.generators;

import br.com.geradormassa.model.Usuario;
import br.com.geradormassa.utils.StringUtils;

import java.text.Normalizer;
import java.util.Random;

public class UsuarioGenerator implements Gerador<Usuario> {

    private final FakerApiData dadosApi;
    private static final Random random = new Random();

    // ... (código dos domínios de email permanece o mesmo)
    private static final String[] DOMINIOS_EMAIL = {
            // Populares Globais
            "@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com",
            "@icloud.com", "@aol.com", "@protonmail.com", "@zoho.com",
            "@yandex.com", "@gmx.com", "@mail.com", "@live.com",
            "@msn.com", "@me.com",

            // Populares no Brasil
            "@uol.com.br", "@bol.com.br", "@terra.com.br", "@ig.com.br",

            // Outros
            "@inbox.com", "@fastmail.com"
    };


    public UsuarioGenerator(FakerApiData dadosApi) {
        this.dadosApi = dadosApi;
    }

    @Override
    public Usuario gerar() {
        String firstName = dadosApi.getFirstName();
        String lastName = dadosApi.getLastName();
        String nomeCompleto = firstName + " " + lastName;

        String nomeLimpo = StringUtils.limparParaNomeSimples(firstName);
        String sobrenomeLimpo = StringUtils.limparParaNomeSimples(lastName);
        String nomeCompletoLimpo = StringUtils.limparParaNomeSimples(nomeCompleto);

        String nomeUsuario = normalizeString(firstName) + "." + normalizeString(lastName);
        String dominioAleatorio = DOMINIOS_EMAIL[random.nextInt(DOMINIOS_EMAIL.length)];
        String email = normalizeString(lastName) + "." + normalizeString(firstName) + dominioAleatorio;

        String cpf = DocumentosGenerator.gerarCpf(false);
        String cnpj = DocumentosGenerator.gerarCnpj(false);
        String razaoSocial = nomeCompletoLimpo + " LTDA";
        String senha = PasswordGenerator.gerarSenhaCustomizada(nomeCompletoLimpo, cpf);
        String telefone = dadosApi.getPhoneNumber().replaceAll("[^\\d]", "");

        return Usuario.builder()
                .nome(nomeLimpo)
                .sobrenome(sobrenomeLimpo)
                .nomeCompleto(nomeCompletoLimpo)
                .nomeUsuario(nomeUsuario)
                .email(email)
                .senha(senha)
                .administrador("true")
                .cpf(cpf)
                .telefone(telefone)
                .razaoSocial(razaoSocial)
                .cnpj(cnpj)
                .addressLine(StringUtils.removerAcentos(dadosApi.getAddressLine()))
                // ===== MUDANÇA 5: PREENCHENDO O NOVO CAMPO =====
                .numeroEndereco(dadosApi.getBuildingNumber())
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
        String semAcentos = StringUtils.removerAcentos(input.toLowerCase());
        return semAcentos.replaceAll("[^a-z0-9]", "");
    }
}