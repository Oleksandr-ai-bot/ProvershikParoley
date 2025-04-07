import java.util.Scanner;

public class Main {
    private static String[] names = new String[15];
    private static String[] passw = new String[15];
    private static String[] banPassw = new String[50];
    private static int count = 0;
    private static int banCount = 0;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean True = true;

        addBan("admin");
        addBan("pass");
        addBan("password");
        addBan("qwerty");
        addBan("ytrewq");

        while (True) {
            showMenu();
            int choice = cHoice(scan, "Виберіть дію: ");

            try {
                switch (choice) {
                    case 1:
                        addUser(scan);
                        break;
                    case 2:
                        delUser(scan);
                        break;
                    case 3:
                        importUser(scan);
                        break;
                    case 4:
                        addBanPass(scan);
                        break;
                    case 0:
                        True = false;
                        break;
                    default:
                        System.out.println("Неправильний вибір.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }

        scan.close();
    }

    private static void showMenu() {
        System.out.println("Менюшка ༼ つ ◕_◕ ༽つ");
        System.out.println("1. Додати користувача");
        System.out.println("2. Видалити користувача");
        System.out.println("3. Увійти в систему");
        System.out.println("4. Додати заборонений пароль");
        System.out.println("0. Вихід");
    }

    private static int cHoice(Scanner scan, String text) {
        while (true) {
            try {
                System.out.print(text);
                return Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введіть число");
            }
        }
    }

    private static String namePas(Scanner scan, String text) {
        System.out.print(text);
        return scan.nextLine();
    }

    private static void addUser(Scanner scan) {
        String name = namePas(scan, "Введіть ім'я:");
        String pass = namePas(scan, "Введіть пароль:");

        reg(name, pass);
        System.out.println("Користувач доданий");
    }

    private static void delUser(Scanner scan) {
        String name = namePas(scan, "Введіть ім'я для видалення:");

        del(name);
        System.out.println("Користувач видалений");
    }

    private static void importUser(Scanner scan) {
        String name = namePas(scan, "Введіть ім'я:");
        String pass = namePas(scan, "Введіть пароль:");

        if (login(name, pass)) {
            System.out.println("Вхід виконано успішно");
        } else {
            System.out.println("Неправильне ім'я або пароль");
        }
    }

    private static void addBanPass(Scanner scan) {
        String pass = namePas(scan, "Введіть заборонений пароль:");
        addBan(pass);
        System.out.println("Заборонений пароль додано");
    }

    public static void addBan(String pass) {
        if (banCount < banPassw.length) {
            banPassw[banCount] = pass;
            banCount++;
        }
    }

    private static void checkName(String name) {
        if (name == null) {
            throw new NullPointerException("Ім'я не може бути null. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        if (name.length() < 5) {
            throw new IllegalArgumentException("Ім'я має бути не менше 5 символів. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                throw new IllegalArgumentException("Ім'я не повинно містити пробілів. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
            }
        }
    }

    private static void checkPass(String pass) {
        if (pass == null) {
            throw new NullPointerException("Пароль не може бути null. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        if (pass.length() < 10) {
            throw new IllegalArgumentException("Пароль має бути не менше 10 символів. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        boolean hasSpec = false;
        int digits = 0;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);

            if (c == ' ') {
                throw new IllegalArgumentException("Пароль не повинен містити пробілів. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
            }

            if (c >= '0' && c <= '9') {
                digits++;
            }

            if (!((c >= 'a') && (c <= 'z'))) {
                if (!((c >= 'A') && (c <= 'Z'))) {
                    if (!((c >= '0') && (c <= '9'))) {
                        hasSpec = true;
                    }
                }
            }
        }

        if (!hasSpec) {
            throw new IllegalArgumentException("Пароль повинен містити хоча б 1 спецсимвол. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        if (digits < 3) {
            throw new IllegalArgumentException("Пароль повинен містити хоча б 3 цифри. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        for (int i = 0; i < banCount; i++) {
            if (banPassw[i] != null) {
                if (pass.equals(banPassw[i])) {
                    throw new IllegalArgumentException("Пароль заборонений. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
                }
            }
        }

        String lowerPass = pass.toLowerCase();
        for (int i = 0; i < banCount; i++) {
            if (banPassw[i] != null) {
                String lowerBanPass = banPassw[i].toLowerCase();
                if (lowerPass.contains(lowerBanPass)) {
                    throw new IllegalArgumentException("Пароль містить заборонене слово: " + banPassw[i] + ". Код помилки: https://qr-code.click/i/p/67f398dc3a179");
                }
            }
        }
    }

    private static int find(String name) {
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                if (names[i].equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void reg(String name, String pass) {
        checkName(name);
        checkPass(pass);

        if (find(name) != -1) {
            throw new IndexOutOfBoundsException("Користувач \"" + name + "\" вже існує. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        boolean added = false;
        for (int i = 0; i < names.length; i++) {
            if (names[i] == null) {
                names[i] = name;
                passw[i] = pass;
                count++;
                added = true;
                break;
            }
        }

        if (!added) {
            throw new IndexOutOfBoundsException("Ліміт користувачів 15. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }
    }

    public static void del(String name) {
        int index = find(name);
        if (index == -1) {
            throw new IndexOutOfBoundsException("Користувач \"" + name + "\" не знайдено. Код помилки: https://qr-code.click/i/p/67f398dc3a179");
        }

        names[index] = null;
        passw[index] = null;
        count--;
    }

    public static boolean login(String name, String pass) {
        int index = find(name);
        if (index == -1) {
            return false;
        }
        if (passw[index].equals(pass)) {
            return true;
        }
        return false;
    }
}
