import java.util.*;

public class BruteForceAnalyzer {
    private final CaesarCipher cipher;
    private static final Set<String> COMMON_WORDS = new HashSet<>(Arrays.asList(
            "the", "and", "for", "that", "this", "with", "from", "have", "are",
            "was", "were", "but", "not", "you", "your", "can", "will", "all",
            "one", "two", "three", "four", "five", "hello", "world", "test",
            "message", "cipher", "caesar", "encrypt", "decrypt", "secret"
    ));

    private static final String[] COMMON_PATTERNS = {
            "the ", "and ", "ing ", "tion ", "that ", "with ", "for ", "are "
    };

    public BruteForceAnalyzer() {
        this.cipher = new CaesarCipher();
    }


    //Перебор всех возможных сдвигов (0-25)

    public Map<Integer, String> tryAllShifts(String encryptedText) {
        Map<Integer, String> results = new LinkedHashMap<>();

        if (encryptedText == null || encryptedText.isEmpty()) {
            return results;
        }

        for (int shift = 0; shift < 26; shift++) {
            String decrypted = cipher.decrypt(encryptedText, shift);
            results.put(shift, decrypted);
        }

        return results;
    }


    // Поиск наиболее вероятного сдвига на основе частоты слов

    public int findBestShift(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return 0;
        }

        int bestShift = 0;
        int maxScore = -1;

        for (int shift = 0; shift < 26; shift++) {
            String decrypted = cipher.decrypt(encryptedText, shift);
            int score = calculateScore(decrypted);

            if (score > maxScore) {
                maxScore = score;
                bestShift = shift;
            }
        }

        return bestShift;
    }


    // Оценка качества расшифрованного текста

    private int calculateScore(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        int score = 0;
        String lowerText = text.toLowerCase();

        // Подсчет распространенных английских слов
        String[] words = lowerText.split("[^a-zA-Z]+");
        for (String word : words) {
            if (COMMON_WORDS.contains(word) && word.length() > 2) {
                score += word.length() * 2;
            }
        }

        // Подсчет распространенных паттернов
        for (String pattern : COMMON_PATTERNS) {
            int index = 0;
            while ((index = lowerText.indexOf(pattern, index)) != -1) {
                score += pattern.length();
                index++;
            }
        }

        // Проверка наличия пробелов (хороший признак текста)
        if (text.contains(" ")) {
            score += text.split(" ").length;
        }

        return score;
    }


    //Вывод всех результатов brute force

    public void printAllResults(String encryptedText) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("РЕЗУЛЬТАТЫ BRUTE FORCE ДЕШИФРОВКИ");
        System.out.println("=".repeat(60));

        Map<Integer, String> results = tryAllShifts(encryptedText);
        List<Map.Entry<Integer, Integer>> scores = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : results.entrySet()) {
            int score = calculateScore(entry.getValue());
            scores.add(new AbstractMap.SimpleEntry<>(entry.getKey(), score));
        }

        // Сортировка по убыванию оценки
        scores.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("\nТоп-5 наиболее вероятных сдвигов:");
        System.out.println("-".repeat(60));

        for (int i = 0; i < Math.min(5, scores.size()); i++) {
            int shift = scores.get(i).getKey();
            int score = scores.get(i).getValue();
            String decrypted = results.get(shift);

            System.out.printf("%d. Сдвиг: %2d | Оценка: %3d%n", i + 1, shift, score);
            System.out.println("   Текст: " + getPreview(decrypted, 100));
            System.out.println();
        }

        int bestShift = scores.get(0).getKey();
        System.out.println("=".repeat(60));
        System.out.println(" РЕКОМЕНДУЕМЫЙ СДВИГ: " + bestShift + " ");
        System.out.println("=".repeat(60));
        System.out.println("\nПолный расшифрованный текст:");
        System.out.println(results.get(bestShift));
    }


    //Получение превью текста

    private String getPreview(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}