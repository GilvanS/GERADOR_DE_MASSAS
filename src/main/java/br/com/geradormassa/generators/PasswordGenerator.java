package br.com.geradormassa.generators;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Gera senhas com base em regras de negócio.
 */
public class PasswordGenerator {

    // Compila o padrão uma vez para otimizar a performance.
    // Este padrão remove tudo que não for um dígito de uma string.
    private static final Pattern NON_DIGITS = Pattern.compile("[^\\d]");
    private static final Random random = new Random();

    /**
     * Gera uma senha customizada no formato: (3 primeiras letras do último nome) +
     * (2 primeiras letras do primeiro nome) + (2 últimos dígitos do CPF).
     * Exemplo: "gilvan sousa" com CPF "...30" se torna "SouGi30".
     *
     * @param nomeCompleto O nome completo do usuário.
     * @param cpf O CPF do usuário (pode estar formatado ou não).
     * @return A senha gerada.
     */
    public static String gerarSenhaCustomizada(String nomeCompleto, String cpf) {
        // Validação inicial para evitar NullPointerException.
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty() || cpf == null) {
            return "SenhaInvalida";
        }

        // Divide o nome completo em partes para pegar o primeiro e o último nome.
        String[] nomes = nomeCompleto.trim().split("\\s+");
        String primeiroNome = nomes[0];
        // Garante que funciona mesmo com nomes simples (sem sobrenome).
        String ultimoNome = nomes.length > 1 ? nomes[nomes.length - 1] : primeiroNome;

        // Parte 1: 3 letras do último nome, com a primeira maiúscula (Ex: "sousa" -> "Sou")
        String parte1 = capitalizar(ultimoNome, 3);

        // Parte 2: 2 letras do primeiro nome, com a primeira maiúscula (Ex: "gilvan" -> "Gi")
        String parte2 = capitalizar(primeiroNome, 2);

        // Parte 3: 2 últimos dígitos do CPF (Ex: "...30" -> "30")
        String cpfNumeros = NON_DIGITS.matcher(cpf).replaceAll(""); // Limpa o CPF, deixando só números.
        String parte3 = cpfNumeros.length() >= 2 ? cpfNumeros.substring(cpfNumeros.length() - 2) : cpfNumeros;

        // Concatena tudo para formar a senha final.
        return parte1 + parte2 + parte3;
    }

    /**
     * Método auxiliar para pegar um trecho de uma palavra e capitalizar a primeira letra.
     * É robusto e funciona mesmo que a palavra seja menor que o tamanho desejado.
     */
    private static String capitalizar(String palavra, int tamanho) {
        // Garante que não tentaremos pegar mais letras do que a palavra tem.
        if (palavra.length() < tamanho) {
            tamanho = palavra.length();
        }
        String trecho = palavra.substring(0, tamanho);
        // Capitaliza a primeira letra e deixa o resto minúsculo.
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