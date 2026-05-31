import java.util.*;

public class StatisticalAnalyzer {
    // Частотность букв в английском языке (%)
    private static final double[] ENGLISH_FREQ = {
            8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966,
            0.153, 0.772, 4.025, 2.406, 6.749, 7.507, 1.929, 0.095, 5.987,
            6.327, 9.056, 2.758, 0.978, 2.360, 0.150, 1.974, 0.074
    };


    //Поиск сдвига на основе частотного анализа (метод наиболее частой буквы)

    public int findBestShiftByFrequency(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> frequency = calculateFrequency(encryptedText);
        if (frequency.isEmpty()) {
            return 0;
        }

        // Находим самую частую букву
        char mostFrequent = Collections.max(frequency.entrySet(),
                Map.Entry.comparingByValue()).getKey();

        // В английском самая частая буква - 'e'
        int shift = (mostFrequent - 'e' + 26) % 26;

        return shift;
    }


    //Поиск сдвига методом хи-квадрат (более точный)

    public int findBestShiftByChiSquare(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return 0;
        }

        int bestShift = 0;
        double bestScore = Double.MAX_VALUE;

        for (int shift = 0; shift < 26; shift++) {
            String decrypted = decryptWithShift(encryptedText, shift);
            double chiSquare = calculateChiSquare(decrypted);

            if (chiSquare < bestScore) {
                bestScore = chiSquare;
                bestShift = shift;
            }
        }

        return bestShift;
    }


    //Расшифровка с заданным сдвигом

    private String decryptWithShift(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char shifted = (char) ((c - base - shift + 26) % 26 + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    // Вычисление критерия хи-квадрат

    private double calculateChiSquare(String text) {
        if (text.isEmpty()) {
            return Double.MAX_VALUE;
        }

        int[] counts = new int[26];
        int total = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char lower = Character.toLowerCase(c);
                counts[lower - 'a']++;
                total++;
            }
        }

        if (total == 0) {
            return Double.MAX_VALUE;
        }

        double chiSquare = 0.0;
        for (int i = 0; i < 26; i++) {
            double expected = (ENGLISH_FREQ[i] / 100.0) * total;
            if (expected > 0) {
                double observed = counts[i];
                chiSquare += Math.pow(observed - expected, 2) / expected;
            }
        }

        return chiSquare;
    }


    // Вычисление частоты букв в тексте

    private Map<Character, Integer> calculateFrequency(String text) {
        Map<Character, Integer> freq = new HashMap<>();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char lower = Character.toLowerCase(c);
                freq.put(lower, freq.getOrDefault(lower, 0) + 1);
            }
        }

        return freq;
    }


    // Вывод статистического анализа

    public void printFrequencyAnalysis(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Текст пуст");
            return;
        }

        Map<Character, Integer> frequency = calculateFrequency(text);
        int total = frequency.values().stream().mapToInt(Integer::intValue).sum();

        if (total == 0) {
            System.out.println("В тексте нет букв");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("СТАТИСТИЧЕСКИЙ АНАЛИЗ ЧАСТОТЫ БУКВ");
        System.out.println("=".repeat(70));
        System.out.printf("%-6s | %-8s | %-8s | %-8s%n", "Буква", "Частота", "Процент", "Ожидаемый %");
        System.out.println("-".repeat(70));

        List<Map.Entry<Character, Integer>> sorted = new ArrayList<>(frequency.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<Character, Integer> entry : sorted) {
            char c = entry.getKey();
            int count = entry.getValue();
            double percentage = (count * 100.0) / total;
            double expected = ENGLISH_FREQ[c - 'a'];
            String indicator = percentage > expected ? "↑" : (percentage < expected ? "↓" : "=");

            System.out.printf("  %c    | %7d  | %6.2f%% %s | %6.2f%%%n",
                    c, count, percentage, indicator, expected);
        }

        System.out.println("=".repeat(70));
    }
}