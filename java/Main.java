import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Caesar Cipher Tool ===\n");
        System.out.println("Выберите интерфейс:");
        System.out.println("1. Текстовое меню");
        System.out.println("2. Графический интерфейс (опционально)");
        System.out.print("Ваш выбор: ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        if ("2".equals(choice)) {

            System.out.println("Графический интерфейс в разработке");
            TextMenuInterface menu = new TextMenuInterface();
            menu.start();
        } else {
            TextMenuInterface menu = new TextMenuInterface();
            menu.start();
        }

        scanner.close();
    }
}


