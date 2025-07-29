package br.com.geradormassa.writers;

import br.com.geradormassa.model.Massa;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.util.*;

public class CsvWriterService {

    public void escreverMassa(List<Massa> massas, String filePath) throws IOException {
        if (massas == null || massas.isEmpty()) {
            return;
        }

        // A ordenação alfabética continua correta
        Collator collator = Collator.getInstance(new Locale("pt", "BR"));
        collator.setStrength(Collator.PRIMARY);
        massas.sort(Comparator.comparing(massa -> massa.getUsuario().getNomeCompleto(), collator));

        List<Field> fields = getAnnotatedFields(Massa.class);
        String[] headers = fields.stream().map(f -> f.getAnnotation(CsvColumn.class).header()).toArray(String[]::new);

        File arquivoCsv = new File(filePath);
        boolean isNewFile = !arquivoCsv.exists() || arquivoCsv.length() == 0;

        // Formatação que usa ';' como delimitador e nos deixa controlar a formatação.
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setQuoteMode(QuoteMode.NONE)
                .setEscape('\\')
                .build();

        try (
                // O Writer agora é o FileWriter diretamente, sem a linha extra
                Writer out = new FileWriter(arquivoCsv, StandardCharsets.UTF_8, true);
                CSVPrinter printer = new CSVPrinter(out, csvFormat)
        ) {
            if (isNewFile) {
                // --- A LINHA "sep=;" FOI REMOVIDA DAQUI ---
                printer.printRecord((Object[]) headers);
            }

            for (Massa massa : massas) {
                List<String> record = new ArrayList<>();
                for (Field field : fields) {
                    try {
                        Object parentObject = getParentObject(massa, field);
                        field.setAccessible(true);
                        Object valueObject = field.get(parentObject);

                        String value = (valueObject == null) ? "" : String.valueOf(valueObject);

                        // A formatação ="valor" para forçar o tipo TEXTO no Excel continua
                        String formattedValue = "=\"" + value.replace("\"", "\"\"") + "\"";
                        record.add(formattedValue);

                    } catch (IllegalAccessException e) {
                        record.add("\"\"");
                    }
                }
                printer.printRecord(record);
            }
        }
    }

    private Object getParentObject(Massa massa, Field field) {
        String parentClassName = field.getDeclaringClass().getSimpleName().toLowerCase();
        try {
            Field parentField = Massa.class.getDeclaredField(parentClassName);
            parentField.setAccessible(true);
            return parentField.get(massa);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Não foi possível encontrar o objeto pai para o campo: " + field.getName(), e);
        }
    }

    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        for (Field parentField : clazz.getDeclaredFields()) {
            if (parentField.getType().getName().startsWith("br.com.geradormassa.model")) {
                for (Field childField : parentField.getType().getDeclaredFields()) {
                    if (childField.isAnnotationPresent(CsvColumn.class)) {
                        allFields.add(childField);
                    }
                }
            }
        }
        allFields.sort(Comparator.comparingInt(f -> f.getAnnotation(CsvColumn.class).order()));
        return allFields;
    }
}