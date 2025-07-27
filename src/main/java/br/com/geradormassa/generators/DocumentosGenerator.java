package br.com.geradormassa.generators;

import java.util.Random;

/**
 * Gera documentos brasileiros como CPF e CNPJ com números válidos.
 * A lógica foi consolidada e refinada a partir de implementações
 * de outros projetos, garantindo clareza e correção.
 */
public class DocumentosGenerator {

    private static final Random random = new Random();

    /**
     * Gera um número de CPF válido.
     * @param comPontuacao Se true, formata o CPF com pontos e traço (###.###.###-##).
     * @return O número do CPF gerado.
     */
    public static String gerarCpf(boolean comPontuacao) {
        int[] cpf = new int[11];
        // 1. Gera os 9 primeiros dígitos aleatoriamente
        for (int i = 0; i < 9; i++) {
            cpf[i] = random.nextInt(10);
        }

        // 2. Calcula os 2 dígitos verificadores
        cpf[9] = calcularDigitoVerificadorCpf(cpf, 9);
        cpf[10] = calcularDigitoVerificadorCpf(cpf, 10);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(cpf[i]);
        }

        if (comPontuacao) {
            return formatarCpf(sb.toString());
        }
        return sb.toString();
    }

    /**
     * Gera um número de CNPJ válido.
     * @param comPontuacao Se true, formata o CNPJ com pontos, barra e traço (##.###.###/####-##).
     * @return O número do CNPJ gerado.
     */
    public static String gerarCnpj(boolean comPontuacao) {
        int[] cnpj = new int[14];
        // 1. Gera os 8 primeiros dígitos (número da empresa)
        for (int i = 0; i < 8; i++) {
            cnpj[i] = random.nextInt(10);
        }
        // 2. Define o milhar como 0001 (padrão para empresa matriz)
        cnpj[8] = 0;
        cnpj[9] = 0;
        cnpj[10] = 0;
        cnpj[11] = 1;

        // 3. Calcula os 2 dígitos verificadores
        cnpj[12] = calcularDigitoVerificadorCnpj(cnpj, 12);
        cnpj[13] = calcularDigitoVerificadorCnpj(cnpj, 13);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            sb.append(cnpj[i]);
        }

        if (comPontuacao) {
            return formatarCnpj(sb.toString());
        }
        return sb.toString();
    }

    /**
     * Calcula um dígito verificador de CPF.
     * O algoritmo multiplica os dígitos por pesos decrescentes (10, 9, 8... para o 1º dígito; 11, 10, 9... para o 2º).
     */
    private static int calcularDigitoVerificadorCpf(int[] cpf, int length) {
        int soma = 0;
        for (int i = 0; i < length; i++) {
            soma += cpf[i] * (length + 1 - i);
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    /**
     * Calcula um dígito verificador de CNPJ.
     * O algoritmo multiplica os dígitos por pesos (5,4,3,2,9,8,7,6,5,4,3,2 para o 1º dígito; 6,5,4,3,2,9,8,7,6,5,4,3,2 para o 2º).
     */
    private static int calcularDigitoVerificadorCnpj(int[] cnpj, int length) {
        int soma = 0;
        int peso = 2;
        for (int i = length - 1; i >= 0; i--) {
            soma += cnpj[i] * peso;
            peso++;
            if (peso > 9) {
                peso = 2;
            }
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    /**
     * Formata uma string de CPF (11 dígitos) com a pontuação padrão.
     * @param cpfSemPontuacao O CPF contendo apenas os 11 dígitos.
     * @return O CPF formatado (###.###.###-##) ou a string original se for inválida.
     */
    public static String formatarCpf(String cpfSemPontuacao) {
        if (cpfSemPontuacao == null || cpfSemPontuacao.length() != 11) {
            return cpfSemPontuacao; // Retorna original se não tiver 11 dígitos
        }
        return cpfSemPontuacao.substring(0, 3) + "." + cpfSemPontuacao.substring(3, 6) + "." + cpfSemPontuacao.substring(6, 9) + "-" + cpfSemPontuacao.substring(9, 11);
    }

    /**
     * Formata uma string de CNPJ (14 dígitos) com a pontuação padrão.
     * @param cnpjSemPontuacao O CNPJ contendo apenas os 14 dígitos.
     * @return O CNPJ formatado (##.###.###/####-##) ou a string original se for inválida.
     */
    public static String formatarCnpj(String cnpjSemPontuacao) {
        if (cnpjSemPontuacao == null || cnpjSemPontuacao.length() != 14) {
            return cnpjSemPontuacao;
        }
        return cnpjSemPontuacao.substring(0, 2) + "." + cnpjSemPontuacao.substring(2, 5) + "." + cnpjSemPontuacao.substring(5, 8) + "/" + cnpjSemPontuacao.substring(8, 12) + "-" + cnpjSemPontuacao.substring(12, 14);
    }

    /**
     * Gera um número de CEP válido com 8 dígitos.
     * @param comPontuacao Se true, formata o CEP com traço (#####-###).
     * @return O número do CEP gerado.
     */
    public static String gerarCep(boolean comPontuacao) {
        // Gera um número aleatório de 8 dígitos, garantindo que tenha 8 casas
        int numero = random.nextInt(99999999);
        String cep = String.format("%08d", numero); // %08d garante 8 dígitos, preenchendo com zeros à esquerda

        if (comPontuacao) {
            return cep.substring(0, 5) + "-" + cep.substring(5, 8);
        }
        return cep;
    }
}
