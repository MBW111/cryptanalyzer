import java.io.*;
import java.nio.file.*;

public class ValidationUtils {


    //Валидация ключа шифрования

    public static boolean validateKey(int key) {
        if (key < 0 || key > 25) {
            System.err.println("Ошибка: Ключ должен быть в диапазоне от 0 до 25");
            return false;
        }
        return true;
    }


    // Валидация пути к файлу

    public static boolean validateFilePath(String filePath, boolean mustExist) {
        if (filePath == null || filePath.trim().isEmpty()) {
            System.err.println("Ошибка: Путь к файлу не может быть пустым");
            return false;
        }

        Path path = Paths.get(filePath);

        if (mustExist) {
            if (!Files.exists(path)) {
                System.err.println("Ошибка: Файл не существует: " + filePath);
                return false;
            }
            if (!Files.isReadable(path)) {
                System.err.println("Ошибка: Нет прав на чтение файла: " + filePath);
                return false;
            }
            if (Files.isDirectory(path)) {
                System.err.println("Ошибка: Указан путь к директории, а не к файлу: " + filePath);
                return false;
            }
        } else {
            // Проверка возможности создания директории
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                try {
                    Files.createDirectories(parent);
                } catch (IOException e) {
                    System.err.println("Ошибка: Невозможно создать директорию: " + parent);
                    return false;
                }
            }
        }

        return true;
    }


    // Валидация текста

    public static boolean validateTextNotEmpty(String text) {
        if (text == null || text.trim().isEmpty()) {
            System.err.println("Ошибка: Текст не может быть пустым");
            return false;
        }
        return true;
    }


    //Валидация целого числа

    public static boolean validateInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: Введите корректное целое число");
            return false;
        }
    }
}