package br.com.geradormassa.generators;

public class GeradorDeTelefone {

    private GeradorDeTelefone() {}

    /**
     * @return retorna uma string representando um número de telefone no formato (11)9XXXX-XXXX
     */
    public static String getTelefoneCelularSP() {
        StringBuilder sb = new StringBuilder();
        sb.append("(11) 9");
        sb.append(RandomGenerator.getInt(1000, 9999));
        sb.append("-");
        sb.append(RandomGenerator.getInt(1000, 9999));
        return sb.toString();
    }
}