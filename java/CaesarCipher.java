import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CaesarCipher {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALPHABET_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int ALPHABET_SIZE = 26;


    // Шифрование текста с заданным сдвигом

    public String encrypt(String text, int shift) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        shift = normalizeShift(shift);
        return processText(text, shift);
    }


    //Расшифровка текста с заданным сдвигом

    public String decrypt(String text, int shift) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        shift = normalizeShift(-shift);
        return processText(text, shift);
    }


    // Обработка текста (шифрование/дешифрование)

    private String processText(String text, int shift) {
        StringBuilder result = new StringBuilder(text.length());

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (Character.isLowerCase(c)) {
                int index = ALPHABET.indexOf(c);
                if (index != -1) {
                    int newIndex = (index + shift) % ALPHABET_SIZE;
                    if (newIndex < 0) {
                        newIndex += ALPHABET_SIZE;
                    }
                    result.append(ALPHABET.charAt(newIndex));
                } else {
                    result.append(c);
                }
            } else if (Character.isUpperCase(c)) {
                int index = ALPHABET_UPPER.indexOf(c);
                if (index != -1) {
                    int newIndex = (index + shift) % ALPHABET_SIZE;
                    if (newIndex < 0) {
                        newIndex += ALPHABET_SIZE;
                    }
                    result.append(ALPHABET_UPPER.charAt(newIndex));
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }


    //Нормализация сдвига (приведение к диапазону 0-25)

    private int normalizeShift(int shift) {
        shift = shift % ALPHABET_SIZE;
        if (shift < 0) {
            shift += ALPHABET_SIZE;
        }
        return shift;
    }


    //Проверка валидности ключа

    public boolean isValidShift(int shift) {
        return shift >= 0 && shift < ALPHABET_SIZE;
    }


    // Шифрование большого файла (оптимизировано для производительности)

    public void encryptFile(String inputPath, String outputPath, int shift) throws IOException {
        shift = normalizeShift(shift);
        processLargeFile(inputPath, outputPath, shift, true);
    }


    //Расшифровка большого файла (оптимизировано для производительности)

    public void decryptFile(String inputPath, String outputPath, int shift) throws IOException {
        shift = normalizeShift(-shift);
        processLargeFile(inputPath, outputPath, shift, false);
    }


    //Обработка больших файлов с буферизацией для производительности

    private void processLargeFile(String inputPath, String outputPath, int shift, boolean isEncrypt) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {

            String line;
            int linesProcessed = 0;
            long startTime = System.currentTimeMillis();

            while ((line = reader.readLine()) != null) {
                String processedLine = processText(line, shift);
                writer.println(processedLine);
                linesProcessed++;

                // Прогресс каждые 1000 строк
                if (linesProcessed % 1000 == 0) {
                    System.out.println("Обработано строк: " + linesProcessed);
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Обработка завершена. Всего строк: " + linesProcessed);
            System.out.println("Время выполнения: " + (endTime - startTime) + " мс");
        }
    }
}