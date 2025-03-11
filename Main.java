import java.util.Scanner;

public class Main {
    public static final int maxUsers = 15;

    // Масиви для 
    public static String[] usernames = new String[maxUsers];
    public static String[] passwords = new String[maxUsers];


    public static String[] bannedPasswords = {"admin", "pass", "password", "qwerty", "ytrewq"};

    public static int userCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        while (true) {
            // меню
            System.out.println("\n==== МЕНЮ ====");
            System.out.println("1 - Додати користувача");
            System.out.println("2 - Видалити користувача");
            System.out.println("3 - Аутентифікація користувача");
            System.out.println("4 - Додати заборонений пароль");
            System.out.println("0 - Вихід");
            System.out.print("Виберіть дію: ");

            String choiceStr = scanner.nextLine();

            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть числове значення для вибору меню.");
                continue;
            }

            if (choice == 1) {
                addUser(scanner);

                /// } else if (choice == 2) {
                ///                 deleteUser(scanner);
                ///             } else if (choice == 3) {
                ///                 authenticateUser(scanner);
                ///             } else if (choice == 4) {
                ///                 addBannedPassword(scanner);

            } else if (choice == 0) {
                System.out.println("Програма завершена.");
                scanner.close();
                return;
            } else {
                System.out.println("Помилка: Невірний вибір. Спробуйте ще раз.");
            }
        }
    }


    public static void addUser(Scanner scanner) {
        if (userCount >= maxUsers) {
            System.out.println("Помилка: Не можна додати більше користувачів. Максимальна кількість - " + maxUsers);
            return;
        }

        System.out.print("Введіть ім'я користувача: ");
        String username = scanner.nextLine();

        if (username.length() < 5) {
            System.out.println("Помилка: Ім'я користувача має складатись не менше ніж з 5 символів");
            return;
        }

        if (username.contains(" ")) {
            System.out.println("Помилка: Ім'я користувача не має містити пробіли");
            return;
        }

        for (int i = 0; i < maxUsers; i++) {
            if (username.equals(usernames[i])) {
                System.out.println("Помилка: Користувач з таким ім'ям вже існує");
                return;
            }
        }

        // пароль
        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();




    }
}
