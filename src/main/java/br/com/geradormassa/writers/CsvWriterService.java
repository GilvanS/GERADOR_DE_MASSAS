package br.com.geradormassa.writers;

import br.com.geradormassa.model.Massa;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode; // Importante!

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CsvWriterService {

    public void escreverMassa(List<Massa> massas, String filePath) throws IOException {
        if (massas == null || massas.isEmpty()) {
            return;
        }

        List<Field> fields = getAnnotatedFields(Massa.class);
        String[] headers = fields.stream()
                .map(f -> f.getAnnotation(CsvColumn.class).header())
                .toArray(String[]::new);

        File arquivoCsv = new File(filePath);
        boolean escreverCabecalho = !arquivoCsv.exists() || arquivoCsv.length() == 0;

        // --- PONTO CRÍTICO DA CORREÇÃO ---
        // Este formato instrui a biblioteca a:
        // 1. Usar ';' como delimitador.
        // 2. Colocar aspas (") APENAS quando for estritamente necessário
        //    (por exemplo, se o texto tiver o próprio delimitador ';').
        // Isso elimina o ="..." e as aspas duplas desnecessárias.
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setQuoteMode(QuoteMode.MINIMAL)
                .build();

        // FileWriter em modo 'append' (true) garante que o arquivo não seja sobrescrito.
        try (
                FileWriter out = new FileWriter(arquivoCsv, StandardCharsets.UTF_8, true);
                CSVPrinter printer = new CSVPrinter(out, csvFormat)
        ) {
            if (escreverCabecalho) {
                printer.printRecord((Object[]) headers);
            }

            for (Massa massa : massas) {
                // Usamos uma lista de 'Object' para manter os tipos originais.
                List<Object> record = new ArrayList<>();
                for (Field field : fields) {
                    try {
                        Object parentObject = getParentObject(massa, field);
                        field.setAccessible(true);
                        Object value = field.get(parentObject);

                        // --- A MUDANÇA MAIS IMPORTANTE ---
                        // Removemos QUALQUER chamada a métodos de formatação manual.
                        // Entregamos o valor bruto (String, Integer, etc.) para a lista.
                        // A biblioteca `CSVPrinter` fará a formatação correta ao chamar `printRecord`.
                        record.add(value);

                    } catch (IllegalAccessException e) {
                        record.add("");
                    }
                }
                printer.printRecord(record); // A mágica acontece aqui!
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
            // Verifica se o campo é um dos seus modelos (Usuario, Produto, Artigo)
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