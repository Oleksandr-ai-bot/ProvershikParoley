import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuthSystem auth = new AuthSystem();
        Scanner scan = new Scanner(System.in);
        boolean True = true;

        while (True) {
            showMenu();
            int choice = cHoice(scan, "Выберите действие: ");

            try {
                switch (choice) {
                    case 1:
                        addUser(scan, auth);
                        break;
                    case 2:
                        delUser(scan, auth);
                        break;
                    case 3:
                        importUser(scan, auth);
                        break;
                    case 4:
                        addBanPass(scan, auth);
                        break;
                    case 0:
                        True = false;
                        break;
                    default:
                        System.out.println("Неправильній вибір.");
                }
            } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
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

    private static int cHoice(java.util.Scanner scan, String text) {
        while (true) {
            try {
                System.out.print(text);
                return Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введіть число");
            }
        }
    }

    private static String namePas(java.util.Scanner scan, String text) {
        System.out.print(text);
        return scan.nextLine();
    }

    private static void addUser(java.util.Scanner scan, AuthSystem auth) {
        String name = namePas(scan, "Введіть ім'я:");
        String pass = namePas(scan, "Введіть пароль:");

        auth.reg(name, pass);
        System.out.println("Користувач доданий");
    }

    private static void delUser(java.util.Scanner scan, AuthSystem auth) {
        String name = namePas(scan, "Введіть ім'я для видалення:");

        auth.del(name);
        System.out.println("Пользователь удален");
    }

    private static void importUser(java.util.Scanner scan, AuthSystem auth) {
        String name = namePas(scan, "Введіть ім'я:");
        String pass = namePas(scan, "Введіть пароль:");

        if (auth.login(name, pass)) {
            System.out.println("Вхід виконано успішно");
        } else {
            System.out.println("Неправильне ім'я або пароль");
        }
    }

    private static void addBanPass(java.util.Scanner scan, AuthSystem auth) {
        String pass = namePas(scan, "Введіть заборонений пароль:");
        auth.addBan(pass);
        System.out.println("Заборонений пароль додано");
    }
}

class AuthSystem {
    private String[] names = new String[15];
    private String[] passw = new String[15];
    private String[] banPassw = new String[50];
    private int count = 0;
    private int banCount = 0;

    public AuthSystem() {
        addBan("admin");
        addBan("pass");
        addBan("password");
        addBan("qwerty");
        addBan("ytrewq");
    }

    public void addBan(String pass) {
        if (banCount < banPassw.length) {
            banPassw[banCount] = pass;
            banCount++;
        }
    }

    private void checkName(String name) {
        if (name == null) {
            throw new NullPointerException("Ім'я не може бути null");
        }

        if (name.length() < 5) {
            throw new IllegalArgumentException("Ім'я має бути не менше 5 символів");
        }

        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ' ') {
                throw new IllegalArgumentException("Ім'я не повинно містити пробілів");
            }
        }
    }

    private void checkPass(String pass) {
        if (pass == null) {
            throw new NullPointerException("Пароль не може бути null");
        }

        if (pass.length() < 10) {
            throw new IllegalArgumentException("Пароль має бути не менше 10 символів");
        }

        boolean hasSpec = false;
        int digits = 0;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);

            if (c == ' ') {
                throw new IllegalArgumentException("Пароль не повинен містити пробілів");
            }

            if (c >= '0' && c <= '9') {
                digits++;
            }

            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))) {
                hasSpec = true;
            }
        }

        if (!hasSpec) {
            throw new IllegalArgumentException("Пароль повинен містити хоча б 1 спецсимвол");
        }

        if (digits < 3) {
            throw new IllegalArgumentException("Пароль повинен містити хоча б 3 цифри");
        }

        String lowerPass = pass.toLowerCase();
        for (int i = 0; i < banCount; i++) {
            if (banPassw[i] != null && lowerPass.contains(banPassw[i])) {
                throw new IllegalArgumentException("Пароль містить заборонене слово: " + banPassw[i]);
            }
        }
    }

    private int find(String name) {
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null && names[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public void reg(String name, String pass) {
        checkName(name);
        checkPass(pass);

        if (find(name) != -1) {
            throw new IndexOutOfBoundsException("Користувач \"" + name + "\" вже існує");
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
            throw new IndexOutOfBoundsException("Ліміт користувачів 15");
        }
    }

    public void del(String name) {
        int index = find(name);
        if (index == -1) {
            throw new IndexOutOfBoundsException("Користувач \"" + name + "\" не знайдено");
        }

        names[index] = null;
        passw[index] = null;
        count--;
    }

    public boolean login(String name, String pass) {
        int index = find(name);
        return (index != -1 && passw[index].equals(pass));
    }
}
