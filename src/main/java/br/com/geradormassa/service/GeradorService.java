package br.com.geradormassa.service;

import br.com.geradormassa.generators.MassaGenerator;
import br.com.geradormassa.model.Massa;
import br.com.geradormassa.writers.CsvWriterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeradorService {

    private final CsvWriterService csvWriterService;
    private final MassaGenerator massaGenerator; // Agora usamos uma instância

    public GeradorService() {
        this.csvWriterService = new CsvWriterService();
        this.massaGenerator = new MassaGenerator(); // Instanciamos o gerador principal
    }

    public void gerarMassaUnificada(int quantidade, String filePath) {
        try {
            List<Massa> massas = new ArrayList<>();
            for (int i = 0; i < quantidade; i++) {
                // Chamamos o método 'gerar()' da nossa instância
                massas.add(massaGenerator.gerar());
            }
            csvWriterService.escreverMassa(massas, filePath);
            System.out.println("Massa de dados gerada com sucesso em: " + filePath);
        } catch (IOException e) {
            System.err.println("Erro ao gerar a massa de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}