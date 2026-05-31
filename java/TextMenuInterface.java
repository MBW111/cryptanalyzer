import java.util.*;

public class TextMenuInterface {
    private final CaesarCipher cipher;
    private final FileHandler fileHandler;
    private final BruteForceAnalyzer bruteForceAnalyzer;
    private final StatisticalAnalyzer statisticalAnalyzer;
    private final Scanner scanner;

    public TextMenuInterface() {
        this.cipher = new CaesarCipher();
        this.fileHandler = new FileHandler();
        this.bruteForceAnalyzer = new BruteForceAnalyzer();
        this.statisticalAnalyzer = new StatisticalAnalyzer();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("==========================================================");
        System.out.println("|          Caesar Cipher Tool v2.0                        |");
        System.out.println("|       Шифрование и дешифровка шифром Цезаря             |");
        System.out.println("===========================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    encryptTextMenu();
                    break;
                case "2":
                    decryptWithKeyMenu();
                    break;
                case "3":
                    bruteForceMenu();
                    break;
                case "4":
                    statisticalAnalysisMenu();
                    break;
                case "5":
                    encryptFileMenu();
                    break;
                case "6":
                    decryptFileMenu();
                    break;
                case "7":
                    running = false;
                    System.out.println("\nСпасибо за использование программы! До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите опцию от 1 до 7.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n" + "─".repeat(50));
        System.out.println("ГЛАВНОЕ МЕНЮ");
        System.out.println("─".repeat(50));
        System.out.println("1. Шифрование текста");
        System.out.println("2. Расшифровка текста с известным ключом");
        System.out.println("3. Расшифровка методом Brute Force (перебор всех ключей)");
        System.out.println("4. Расшифровка методом статистического анализа");
        System.out.println("5. Шифрование файла");
        System.out.println("6. Расшифровка файла");
        System.out.println("7. Выход");
        System.out.println("─".repeat(50));
        System.out.print("Ваш выбор: ");
    }

    private void encryptTextMenu() {
        System.out.println("\n--- Шифрование текста ---");
        System.out.print("Введите текст для шифрования: ");
        String text = scanner.nextLine();

        if (!ValidationUtils.validateTextNotEmpty(text)) return;

        int key = getKeyFromUser();
        if (!ValidationUtils.validateKey(key)) return;

        String encrypted = cipher.encrypt(text, key);
        System.out.println("\nРезультат шифрования:");
        System.out.println("─".repeat(50));
        System.out.println("Исходный текст: " + text);
        System.out.println("Ключ: " + key);
        System.out.println("Зашифрованный текст: " + encrypted);
        System.out.println("─".repeat(50));

        saveResult(encrypted);
    }

    private void decryptWithKeyMenu() {
        System.out.println("\n--- Расшифровка с известным ключом ---");
        System.out.print("Введите текст для расшифровки: ");
        String text = scanner.nextLine();

        if (!ValidationUtils.validateTextNotEmpty(text)) return;

        int key = getKeyFromUser();

        String decrypted = cipher.decrypt(text, key);
        System.out.println("\nРезультат расшифровки:");
        System.out.println("─".repeat(50));
        System.out.println("Зашифрованный текст: " + text);
        System.out.println("Ключ: " + key);
        System.out.println("Расшифрованный текст: " + decrypted);
        System.out.println("─".repeat(50));

        saveResult(decrypted);
    }

    private void bruteForceMenu() {
        System.out.println("\n--- Brute Force дешифровка ---");
        System.out.print("Введите текст для анализа: ");
        String text = scanner.nextLine();

        if (!ValidationUtils.validateTextNotEmpty(text)) return;

        bruteForceAnalyzer.printAllResults(text);

        saveBruteForceResults(text);
    }

    private void statisticalAnalysisMenu() {
        System.out.println("\n--- Статистическая дешифровка ---");
        System.out.print("Введите текст для анализа: ");
        String text = scanner.nextLine();

        if (!ValidationUtils.validateTextNotEmpty(text)) return;

        statisticalAnalyzer.printFrequencyAnalysis(text);

        int bestShiftFreq = statisticalAnalyzer.findBestShiftByFrequency(text);
        int bestShiftChi = statisticalAnalyzer.findBestShiftByChiSquare(text);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("РЕЗУЛЬТАТЫ СТАТИСТИЧЕСКОЙ ДЕШИФРОВКИ");
        System.out.println("=".repeat(50));
        System.out.println("Метод наиболее частой буквы:");
        System.out.println("  Предполагаемый сдвиг: " + bestShiftFreq);
        System.out.println("  Расшифрованный текст: " + cipher.decrypt(text, bestShiftFreq));
        System.out.println("\nМетод хи-квадрат (более точный):");
        System.out.println("  Предполагаемый сдвиг: " + bestShiftChi);
        System.out.println("  Расшифрованный текст: " + cipher.decrypt(text, bestShiftChi));
        System.out.println("=".repeat(50));

        System.out.print("\nСохранить результат? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            saveToFile(cipher.decrypt(text, bestShiftChi));
        }
    }

    private void encryptFileMenu() {
        System.out.println("\n--- Шифрование файла ---");
        System.out.print("Введите путь к входному файлу: ");
        String inputPath = scanner.nextLine();

        if (!ValidationUtils.validateFilePath(inputPath, true)) return;

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine();

        int key = getKeyFromUser();
        if (!ValidationUtils.validateKey(key)) return;

        try {
            System.out.println("Обработка файла...");
            long startTime = System.currentTimeMillis();

            cipher.encryptFile(inputPath, outputPath, key);

            long endTime = System.currentTimeMillis();
            System.out.println("\nФайл успешно зашифрован!");
            System.out.println("  Входной файл: " + inputPath);
            System.out.println("  Выходной файл: " + outputPath);
            System.out.println("  Размер: " + fileHandler.getFileSize(outputPath) + " байт");
            System.out.println("  Время: " + (endTime - startTime) + " мс");

        } catch (Exception e) {
            System.err.println("✗ Ошибка при шифровании файла: " + e.getMessage());
        }
    }

    private void decryptFileMenu() {
        System.out.println("\n--- Расшифровка файла ---");
        System.out.print("Введите путь к входному файлу: ");
        String inputPath = scanner.nextLine();

        if (!ValidationUtils.validateFilePath(inputPath, true)) return;

        System.out.print("Введите путь для сохранения результата: ");
        String outputPath = scanner.nextLine();

        int key = getKeyFromUser();

        try {
            System.out.println("Обработка файла...");
            long startTime = System.currentTimeMillis();

            cipher.decryptFile(inputPath, outputPath, key);

            long endTime = System.currentTimeMillis();
            System.out.println("\nФайл успешно расшифрован!");
            System.out.println("  Входной файл: " + inputPath);
            System.out.println("  Выходной файл: " + outputPath);
            System.out.println("  Время: " + (endTime - startTime) + " мс");

        } catch (Exception e) {
            System.err.println("✗ Ошибка при расшифровке файла: " + e.getMessage());
        }
    }

    private int getKeyFromUser() {
        System.out.print("Введите ключ (сдвиг от 0 до 25): ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите целое число");
            return -1;
        }
    }

    private void saveResult(String result) {
        System.out.print("\nСохранить результат в файл? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("y") || answer.equals("yes")) {
            saveToFile(result);
        }
    }

    private void saveBruteForceResults(String originalText) {
        System.out.print("\nСохранить лучший результат brute force в файл? (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("y") || answer.equals("yes")) {
            int bestShift = bruteForceAnalyzer.findBestShift(originalText);
            String decrypted = cipher.decrypt(originalText, bestShift);
            saveToFile("Сдвиг: " + bestShift + "\n\n" + decrypted);
        }
    }

    private void saveToFile(String content) {
        System.out.print("Введите имя файла для сохранения: ");
        String fileName = scanner.nextLine().trim();

        if (fileName.isEmpty()) {
            fileName = "result.txt";
        }

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        try {
            fileHandler.writeFile(fileName, content);
            System.out.println(" Результат сохранен в файл: " + fileName);
        } catch (Exception e) {
            System.err.println(" Ошибка при сохранении файла: " + e.getMessage());
        }
    }
}