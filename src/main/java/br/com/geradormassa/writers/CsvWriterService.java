package br.com.geradormassa.writers;

import br.com.geradormassa.model.Artigo;
import br.com.geradormassa.model.Massa;
import br.com.geradormassa.model.Produto;
import br.com.geradormassa.model.Usuario;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável por escrever objetos Massa em um arquivo CSV.
 * Esta implementação utiliza reflection para ler as anotações @CsvColumn
 * e formata todos os campos como texto para garantir a integridade no Excel.
 */
public class CsvWriterService {

    /**
     * Escreve ou anexa uma lista de objetos Massa a um arquivo CSV.
     * A ordem e os cabeçalhos das colunas são definidos pelas anotações @CsvColumn.
     *
     * @param massas   A lista de dados a serem escritos.
     * @param filePath O caminho completo para o arquivo CSV de saída.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    public void escreverMassa(List<Massa> massas, String filePath) throws IOException {
        if (massas == null || massas.isEmpty()) {
            return;
        }

        List<Field> sortedFields = getSortedCsvFields();
        String[] headers = getHeadersFromFields(sortedFields);

        File arquivo = new File(filePath);
        boolean arquivoExiste = arquivo.exists() && arquivo.length() > 0;

        // Configura o formato do CSV para não adicionar aspas automaticamente,
        // pois nossa formatação manual já cuida disso.
        CSVFormat.Builder formatBuilder = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setQuoteMode(QuoteMode.NONE) // Desativa as aspas automáticas
                .setEscape('\\')              // Define um caractere de escape para validação
                .setRecordSeparator("\r\n")   // Garante a quebra de linha padrão do Windows
                .setHeader(headers);

        if (arquivoExiste) {
            formatBuilder.setHeader((String[]) null);
        }

        CSVFormat csvFormat = formatBuilder.build();

        try (
                FileOutputStream fos = new FileOutputStream(arquivo, true);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                BufferedWriter writer = new BufferedWriter(osw);
                CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)
        ) {
            for (Massa massa : massas) {
                List<String> record = new ArrayList<>();
                for (Field field : sortedFields) {
                    try {
                        Object rawValue = getFieldValue(massa, field);
                        // Formata cada valor para ser tratado como texto no Excel.
                        record.add(formatValueAsText(rawValue));
                    } catch (Exception e) {
                        record.add(formatValueAsText("")); // Adiciona um valor vazio formatado
                    }
                }
                csvPrinter.printRecord(record);
            }
            csvPrinter.flush();
        }
    }

    /**
     * Formata um valor para ser explicitamente tratado como texto pelo Excel.
     * Exemplo: 123 se torna "=\"123\""
     */
    private String formatValueAsText(Object value) {
        if (value == null) {
            return "=\"\""; // Representa uma célula de texto vazia
        }
        // Escapa quaisquer aspas duplas dentro do valor para não quebrar o formato
        String stringValue = value.toString().replace("\"", "\"\"");
        return "=\"" + stringValue + "\"";
    }

    private List<Field> getSortedCsvFields() {
        List<Field> allFields = new ArrayList<>();
        allFields.addAll(getAnnotatedFields(Usuario.class));
        allFields.addAll(getAnnotatedFields(Produto.class));
        allFields.addAll(getAnnotatedFields(Artigo.class));
        allFields.sort(Comparator.comparingInt(f -> f.getAnnotation(CsvColumn.class).order()));
        return allFields;
    }

    private List<Field> getAnnotatedFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .collect(Collectors.toList());
    }

    private String[] getHeadersFromFields(List<Field> fields) {
        return fields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).header())
                .toArray(String[]::new);
    }

    private Object getFieldValue(Massa massa, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Class<?> declaringClass = field.getDeclaringClass();

        if (declaringClass.equals(Usuario.class)) {
            return field.get(massa.getUsuario());
        }
        if (declaringClass.equals(Produto.class)) {
            return field.get(massa.getProduto());
        }
        if (declaringClass.equals(Artigo.class)) {
            return field.get(massa.getArtigo());
        }
        return null;
    }
}