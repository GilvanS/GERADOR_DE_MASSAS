package org.br.com.test.utils.massas; // Use o seu pacote de utilidades

/**
 * Contém métodos úteis para manipulação de arquivos CSV,
 * especialmente para limpar formatos específicos para Excel.
 */
public class CsvUtils {

    /**
     * Limpa um valor que foi formatado com ="valor" para ser lido como texto pelo Excel.
     * Este método remove a formatação e retorna o dado puro.
     *
     * @param valorSujo O valor lido diretamente da célula do CSV (ex: ="adrianna.o'hara").
     * @return O valor limpo e puro (ex: adrianna.o'hara).
     */
    public static String limparValorFormatadoParaExcel(String valorSujo) {
        if (valorSujo == null) {
            return null;
        }

        // Verifica se o valor está no formato ="..."
        if (valorSujo.startsWith("=\"") && valorSujo.endsWith("\"")) {
            // Remove o =" do início (2 caracteres) e o " do final (1 caractere).
            String valorLimpo = valorSujo.substring(2, valorSujo.length() - 1);

            // Restaura as aspas duplas que podem ter sido escapadas para o Excel.
            // O formato do Excel para uma aspa interna é colocar duas: "".
            return valorLimpo.replace("\"\"", "\"");
        }

        // Se o valor não estiver no formato esperado, retorna como está para evitar quebrar a lógica.
        return valorSujo;
    }
}