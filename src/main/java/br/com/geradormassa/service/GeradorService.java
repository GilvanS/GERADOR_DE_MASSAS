package br.com.geradormassa.service;

import br.com.geradormassa.generators.MassaGenerator;
import br.com.geradormassa.model.Massa;
import br.com.geradormassa.writers.CsvWriterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de serviço responsável por orquestrar o processo de geração e escrita de dados.
 */
public class GeradorService {

    private final CsvWriterService csvWriterService;
    private final MassaGenerator massaGenerator;

    public GeradorService() {
        this.csvWriterService = new CsvWriterService();
        this.massaGenerator = new MassaGenerator();
    }

    /**
     * Gera um número especificado de registros de dados e os escreve em um arquivo CSV.
     *
     * @param quantidade A quantidade de registros de dados a serem gerados.
     * @param filePath   O caminho para o arquivo CSV de saída.
     * @throws IOException Se ocorrer um erro de I/O durante a escrita do arquivo.
     */
    public void gerarMassaUnificada(int quantidade, String filePath) throws IOException {
        if (quantidade <= 0) {
            System.out.println("Quantidade de registros para gerar é zero ou negativa. Nenhuma ação foi tomada.");
            return;
        }

        System.out.println("Gerando " + quantidade + " registros de massa de dados...");
        List<Massa> massas = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            massas.add(massaGenerator.gerar());
        }

        csvWriterService.escreverMassa(massas, filePath);
        System.out.println("Massa de dados gravada com sucesso em: " + filePath);
    }
}