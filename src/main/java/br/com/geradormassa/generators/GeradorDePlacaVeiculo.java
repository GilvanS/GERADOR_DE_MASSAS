package br.com.geradormassa.generators;

public class GeradorDePlacaVeiculo {

    private GeradorDePlacaVeiculo() {}

    public static String getPlacaMercosul() {
        StringBuilder sb = new StringBuilder();
        sb.append(RandomGenerator.getUpperCaseString(3));
        sb.append(RandomGenerator.fromZeroToNine());
        sb.append(RandomGenerator.getUpperCaseString(1));
        sb.append(RandomGenerator.fromZeroToNine());
        sb.append(RandomGenerator.fromZeroToNine());
        return sb.toString();
    }
}