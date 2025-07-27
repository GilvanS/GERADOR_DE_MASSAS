package br.com.geradormassa.generators;

import java.util.Random;

public class GeradorDeImei {

    private GeradorDeImei() {}

    public static String generateIMEI() {
        Random random = new Random();
        int[] digits = new int[15];

        // Gerar 14 dígitos aleatórios
        for (int i = 0; i < 14; i++) {
            digits[i] = random.nextInt(10);
        }

        // Calcular o dígito verificador usando o algoritmo de Luhn
        digits[14] = calculateLuhn(digits);

        StringBuilder imei = new StringBuilder();
        for (int digit : digits) {
            imei.append(digit);
        }
        return imei.toString();
    }

    private static int calculateLuhn(int[] imei) {
        int sum = 0;
        for (int i = 0; i < 14; i++) {
            int digit = imei[i];
            if (i % 2 == 1) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }
}