import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class FileHandler {


    // Чтение файла с оптимизацией для больших файлов

    public String readFile(String filePath) throws IOException {
        validateFilePath(filePath, true);

        Path path = Paths.get(filePath);
        long fileSize = Files.size(path);

        // Для файлов больше 10MB используем буферизированное чтение
        if (fileSize > 10 * 1024 * 1024) {
            System.out.println("Файл большого размера, используем буферизированное чтение...");
            return readLargeFile(filePath);
        } else {
            return readSmallFile(filePath);
        }
    }


    // Чтение маленьких файлов (до 10MB)

    private String readSmallFile(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes, StandardCharsets.UTF_8);
    }


    //Чтение больших файлов с буферизацией

    private String readLargeFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            char[] buffer = new char[8192];
            int bytesRead;
            while ((bytesRead = reader.read(buffer)) != -1) {
                content.append(buffer, 0, bytesRead);
            }
        }
        return content.toString();
    }


    //Запись файла

    public void writeFile(String filePath, String content) throws IOException {
        validateFilePath(filePath, false);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.print(content);
        } catch (IOException e) {
            throw new IOException("Ошибка записи в файл: " + filePath, e);
        }
    }


    //Валидация пути к файлу

    private void validateFilePath(String filePath, boolean mustExist) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Путь к файлу не может быть пустым");
        }

        Path path = Paths.get(filePath);

        if (mustExist) {
            if (!Files.exists(path)) {
                throw new IOException("Файл не существует: " + filePath);
            }
            if (!Files.isReadable(path)) {
                throw new IOException("Нет прав на чтение файла: " + filePath);
            }
        } else {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        }
    }

    public boolean fileExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    public long getFileSize(String filePath) throws IOException {
        if (!fileExists(filePath)) {
            throw new IOException("Файл не существует: " + filePath);
        }
        return Files.size(Paths.get(filePath));
    }


    //Проверка, является ли файл текстовым

    public boolean isTextFile(String filePath) throws IOException {
        String contentType = Files.probeContentType(Paths.get(filePath));
        return contentType != null && contentType.startsWith("text");
    }
}