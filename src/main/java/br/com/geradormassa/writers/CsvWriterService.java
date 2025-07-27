package br.com.geradormassa.writers;

import br.com.geradormassa.model.Massa;
import br.com.geradormassa.utils.StringUtils;
import br.com.geradormassa.writers.CsvColumn;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvWriterService {

    public void escreverMassa(List<Massa> massas, String filePath) throws IOException {
        if (massas == null || massas.isEmpty()) {
            return;
        }

        // 1. Descobre os campos e cabeçalhos dinamicamente a partir das anotações
        List<Field> fields = getAnnotatedFields(Massa.class);
        String[] headers = fields.stream()
                .map(f -> f.getAnnotation(CsvColumn.class).header())
                .toArray(String[]::new);

        File arquivoCsv = new File(filePath);
        boolean escreverCabecalho = !arquivoCsv.exists() || arquivoCsv.length() == 0;

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .build();

        try (
                FileWriter out = new FileWriter(arquivoCsv, StandardCharsets.UTF_8, true);
                CSVPrinter printer = new CSVPrinter(out, csvFormat)
        ) {
            if (escreverCabecalho) {
                printer.printRecord((Object[]) headers);
            }

            for (Massa massa : massas) {
                List<String> record = new ArrayList<>();
                for (Field field : fields) {
                    try {
                        // 2. Navega nos objetos aninhados para pegar o valor correto
                        Object parentObject = getParentObject(massa, field);
                        field.setAccessible(true);
                        Object value = field.get(parentObject);
                        record.add(StringUtils.formatarParaTextoCsv(value));
                    } catch (IllegalAccessException e) {
                        record.add(""); // Adiciona vazio em caso de erro
                    }
                }
                printer.printRecord(record);
            }
        }
    }

    // Método auxiliar para encontrar o objeto pai de um campo (ex: o objeto 'usuario' para o campo 'nomeCompleto')
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

    // Método para obter todos os campos anotados com @CsvColumn, em ordem.
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();
        for (Field parentField : clazz.getDeclaredFields()) {
            for (Field childField : parentField.getType().getDeclaredFields()) {
                if (childField.isAnnotationPresent(CsvColumn.class)) {
                    allFields.add(childField);
                }
            }
        }
        allFields.sort(Comparator.comparingInt(f -> f.getAnnotation(CsvColumn.class).order()));
        return allFields;
    }
}