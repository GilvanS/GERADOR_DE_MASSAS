package br.com.geradormassa.utils;

import java.text.Normalizer;

/**
 * Classe utilitária para operações comuns com Strings.
 */
public final class StringUtils {

    private StringUtils() {
        // Classe utilitária não deve ser instanciada
    }

    /**
     * Remove acentos e outros caracteres diacríticos de uma string, preservando a capitalização.
     * Exemplo: "Olá, Célia!" se torna "Ola, Celia!".
     * @param input A string a ser limpa.
     * @return A string sem acentos.
     */
    public static String removerAcentos(String input) {
        if (input == null) {
            return null;
        }
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    /**
     * NOVO MÉTODO: Limpa uma string para ser usada como um nome simples.
     * Remove acentos, substitui apóstrofos e hífens por espaços e elimina
     * quaisquer outros caracteres que não sejam letras ou espaços.
     * Exemplo: "Célia O'Hara-Smith" se torna "Celia O Hara Smith".
     *
     * @param input A string de nome a ser limpa.
     * @return A string limpa, contendo apenas letras e espaços.
     */
    public static String limparParaNomeSimples(String input) {
        if (input == null) {
            return null;
        }
        // 1. Remove acentos
        String semAcentos = removerAcentos(input);
        // 2. Substitui hífens e apóstrofos por espaço para não juntar palavras (ex: O'Hara -> O Hara)
        String comEspacos = semAcentos.replaceAll("[-']", " ");
        // 3. Remove qualquer coisa que não seja letra ou espaço e normaliza espaços múltiplos
        return comEspacos.replaceAll("[^\\p{L}\\s]", "").replaceAll("\\s+", " ").trim();
    }

    /**
     * Formata um valor para ser tratado como texto explícito pelo Excel ao abrir um CSV.
     * Envolve o valor na fórmula `="<valor>"`, o que preserva zeros à esquerda e evita notação científica.
     * @param valor O objeto a ser formatado.
     * @return A string formatada para o CSV.
     */
    public static String formatarParaTextoCsv(Object valor) {
        if (valor == null) {
            return "";
        }
        // A fórmula `="<valor>"` força o Excel a tratar o conteúdo como uma string.
        // Também escapa aspas duplas dentro do valor, se houver.
        return "=\"" + valor.toString().replace("\"", "\"\"") + "\"";
    }
}