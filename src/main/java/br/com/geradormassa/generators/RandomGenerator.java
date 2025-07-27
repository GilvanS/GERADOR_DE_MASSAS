package br.com.geradormassa.generators;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class RandomGenerator {

    private static final Random random = new Random();

    private RandomGenerator() {
        // Classe utilitária
    }

    /**
     * Gera um número pseudo-aleatório entre o limite inferior (inclusivo) e o limite superior (exclusivo).
     */
    public static int getInt(int limiteInferior, int limiteSuperior) {
        return limiteInferior + random.nextInt(limiteSuperior - limiteInferior);
    }

    /**
     * Gera um número aleatório de 0 a 9.
     */
    public static int fromZeroToNine() {
        return getInt(0, 10);
    }

    public static String getLowerCaseString(int length) {
        return RandomStringUtils.randomAlphabetic(length).toLowerCase();
    }

    public static String getUpperCaseString(int length) {
        return RandomStringUtils.randomAlphabetic(length).toUpperCase();
    }
}