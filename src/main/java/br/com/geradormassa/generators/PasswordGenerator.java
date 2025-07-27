package br.com.geradormassa.generators;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Gera senhas com base em regras de negócio.
 */
public class PasswordGenerator {

    private static final Pattern NON_DIGITS = Pattern.compile("[^\\d]");
    private static final Random random = new Random();

    /**
     * Gera uma senha customizada no formato: (3 primeiras letras do último nome) +
     * (2 primeiras letras do primeiro nome) + (2 últimos dígitos do CPF).
     * Exemplo: "Kiara Donnelly" com CPF "...69" se torna "DonKi69".
     *
     * @param nomeCompleto O nome completo do usuário.
     * @param cpf O CPF do usuário (pode estar formatado ou não).
     * @return A senha gerada.
     */
    public static String gerarSenhaCustomizada(String nomeCompleto, String cpf) {
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty() || cpf == null) {
            return "SenhaInvalida";
        }

        String[] nomes = nomeCompleto.trim().split("\\s+");
        String primeiroNome = nomes[0];
        // Caso o nome tenha apenas uma palavra, usa ela como primeiro e último nome.
        String ultimoNome = nomes.length > 1 ? nomes[nomes.length - 1] : primeiroNome;

        // Parte 1: 3 letras do último nome, com a primeira maiúscula (Ex: Don)
        String parte1 = capitalizar(ultimoNome, 3);

        // Parte 2: 2 letras do primeiro nome, com a primeira maiúscula (Ex: Ki)
        String parte2 = capitalizar(primeiroNome, 2);

        // Parte 3: 2 últimos dígitos do CPF (Ex: 69)
        String cpfNumeros = NON_DIGITS.matcher(cpf).replaceAll("");
        String parte3 = cpfNumeros.length() >= 2 ? cpfNumeros.substring(cpfNumeros.length() - 2) : cpfNumeros;

        return parte1 + parte2 + parte3;
    }

    /**
     * Método auxiliar para pegar um trecho de uma palavra e capitalizar a primeira letra.
     */
    private static String capitalizar(String palavra, int tamanho) {
        if (palavra.length() < tamanho) {
            tamanho = palavra.length();
        }
        String trecho = palavra.substring(0, tamanho);
        return trecho.substring(0, 1).toUpperCase() + trecho.substring(1).toLowerCase();
    }

    /**
     * Gera uma senha padronizada para testes no formato "PasswordXXX".
     *
     * @deprecated A geração de senha customizada é o novo padrão. Use {@link #gerarSenhaCustomizada(String, String)}
     * @return A senha gerada no padrão "PasswordXXX".
     */
    @Deprecated
    public static String gerarSenhaPadrao() {
        int numeroAleatorio = random.nextInt(1000);
        String sufixoNumerico = String.format("%03d", numeroAleatorio);
        return "Password" + sufixoNumerico;
    }
}