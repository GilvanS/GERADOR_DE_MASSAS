package br.com.geradormassa;

import br.com.geradormassa.service.GeradorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Ponto de entrada da aplicação para gerar massa de dados.
 */
public class Main {

    public static void main(String[] args) {
        GeradorService geradorService = new GeradorService();

        System.out.println("Iniciando a geração de massa de dados...");

        // --- LÓGICA DE CAMINHO APRIMORADA ---

        // 1. Define o nome do diretório e do arquivo de forma clara.
        String diretorioSaida = "output";
        String nomeArquivo = "massaDeDados.csv";

        // 2. Monta o caminho completo usando a API moderna do Java.
        // Isso garante que o caminho funcione em qualquer sistema operacional (Windows, Linux, etc.).
        Path caminhoCompleto = Paths.get(diretorioSaida, nomeArquivo);

        try {
            // 3. Garante que o diretório de saída exista.
            // Se a pasta "output" não existir, ela será criada automaticamente.
            Files.createDirectories(caminhoCompleto.getParent());

            int quantidadeRegistros = 25;

            System.out.println("Gerando " + quantidadeRegistros + " registros em '" + caminhoCompleto.toAbsolutePath() + "'...");
            // 4. Passa o caminho como uma String para o serviço.
            geradorService.gerarMassaUnificada(quantidadeRegistros, caminhoCompleto.toString());

            System.out.println("\nProcesso de geração de massa finalizado.");

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao criar o diretório de saída: " + e.getMessage());
            e.printStackTrace();
        }
    }
}