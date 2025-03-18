import java.util.Scanner;

public class Main {
    public static final int maxU = 15;
    public static String[] users = new String[maxU];
    public static String[] passes = new String[maxU];
    public static String[] banPass = {"admin", "pass", "password", "qwerty", "ytrewq"};
    public static int uCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ch = 0;
        while (true) {
            // меню
            System.out.println("\n==== МЕНЮ ====");
            System.out.println("1 - Додати користувача");
            System.out.println("2 - Видалити користувача");
            System.out.println("3 - Аутентифікація користувача");
            System.out.println("4 - Додати заборонений пароль");
            System.out.println("0 - Вихід");
            System.out.print("Виберіть дію: ");
            String chStr = sc.nextLine();
            try {
                ch = Integer.parseInt(chStr);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: Введіть числове значення для вибору меню.");
                continue;
            }

            try {
                if (ch == 1) {
                    addU(sc);
                } else if (ch == 2) {
                    delU(sc);
                } else if (ch == 3) {
                    auth(sc);
                } else if (ch == 4) {
                    addBan(sc);
                } else if (ch == 0) {
                    System.out.println("Програма завершена.");
                    sc.close();
                    return;
                } else {
                    System.out.println("Помилка: Невірний вибір. Спробуйте ще раз.");
                }
            } catch (Auth e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }

    public static void addU(Scanner sc) throws Auth {
        if (uCount >= maxU) {
            throw new MaxUser("Не можна додати більше користувачів. Максимальна кількість - " + maxU);
        }

        System.out.print("Введіть ім'я користувача: ");
        String user = sc.nextLine();

        // Перевірка імені
        if (user.length() < 5) {
            throw new BadName("Ім'я користувача має складатись не менше ніж з 5 символів");
        }

        if (user.contains(" ")) {
            throw new BadName("Ім'я користувача не має містити пробіли");
        }

        // Перевірка користувача
        for (int i = 0; i < uCount; i++) {
            if (user.equals(users[i])) {
                throw new DupUser("Користувач з таким ім'ям вже існує");
            }
        }

        System.out.print("Введіть пароль: ");
        String pass = sc.nextLine();
        checkPass(pass);
        int slot = findSlot();
        users[slot] = user;
        passes[slot] = pass;
        uCount++;

        System.out.println("Користувача успішно додано.");
    }

    private static int findSlot() {
        for (int i = 0; i < maxU; i++) {
            if (users[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private static void checkPass(String pass) throws Pass {
        // Перевірка довжини
        if (pass.length() < 10) {
            throw new BadPass("Довжина паролю має бути не менше 10 символів");
        }

        // Перевірка пробілів
        if (pass.contains(" ")) {
            throw new BadPass("Пароль не має містити пробілів");
        }

        // Перевірка на спеціальні символи
        boolean hasSpec = false;
        // Перевірка на цифри
        int digits = 0;
        // Перевірка на латинські символи
        boolean nonLatin = false;

        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);

            if (Character.isDigit(c)) {
                digits++;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpec = true;
            } else if (Character.isLetter(c) && !isLatin(c)) {
                nonLatin = true;
            }
        }

        if (!hasSpec) {
            throw new BadPass("Пароль має містити хоча б 1 спеціальний символ");
        }

        if (digits < 3) {
            throw new BadPass("Пароль має містити хоча б 3 цифри");
        }

        if (nonLatin) {
            throw new BadPass("Пароль має складатись виключно з латинських символів, спеціальних символів, символів чисел");
        }

        // Перевірка на заборонені паролі
        for (String ban : banPass) {
            if (ban != null && pass.toLowerCase().contains(ban.toLowerCase())) {
                throw new BanPass("Пароль не може містити заборонене слово: " + ban);
            }
        }
    }

    private static boolean isLatin(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static void delU(Scanner sc) throws Auth {
        System.out.print("Введіть ім'я користувача для видалення: ");
        String user = sc.nextLine();

        int idx = -1;
        for (int i = 0; i < maxU; i++) {
            if (user.equals(users[i])) {
                idx = i;
                break;
            }
        }

        if (idx == -1) {
            throw new NoUser("Користувача з ім'ям '" + user + "' не знайдено");
        }

        users[idx] = null;
        passes[idx] = null;
        uCount--;

        System.out.println("Користувача " + user + " успішно видалено");
    }

    public static void auth(Scanner sc) throws Auth {
        System.out.print("Введіть ім'я користувача: ");
        String user = sc.nextLine();

        System.out.print("Введіть пароль: ");
        String pass = sc.nextLine();

        boolean ok = false;
        for (int i = 0; i < maxU; i++) {
            if (user.equals(users[i]) && pass.equals(passes[i])) {
                ok = true;
                break;
            }
        }

        if (!ok) {
            throw new Login("Неправильне ім'я користувача або пароль");
        }

        System.out.println("Користувача " + user + " успішно аутентифіковано");
    }

    public static void addBan(Scanner sc) throws Auth {
        System.out.print("Введіть заборонений пароль для додавання: ");
        String newBan = sc.nextLine();

        if (newBan == null || newBan.isEmpty()) {
            throw new BadBan("Заборонений пароль не може бути порожнім");
        }

        // Перевірка чи вже існує такий заборонений пароль
        for (String ban : banPass) {
            if (newBan.equals(ban)) {
                throw new BadBan("Пароль '" + newBan + "' вже знаходиться в списку заборонених");
            }
        }

        String[] newBans = new String[banPass.length + 1];
        for (int i = 0; i < banPass.length; i++) {
            newBans[i] = banPass[i];
        }

        newBans[banPass.length] = newBan;
        banPass = newBans;

        System.out.println("Заборонений пароль '" + newBan + "' успішно додано");
    }
}

// База
class Auth extends Exception {
    public Auth(String message) {
        super(message);
    }
}

class User extends Auth {
    public User(String message) {
        super(message);
    }
}

// для некоректного імені користувача
class BadName extends User {
    public BadName(String message) {
        super(message);
    }
}

// для дублікату імені користувача
class DupUser extends User {
    public DupUser(String message) {
        super(message);
    }
}

// для винятків, пов'язаних з паролем
class Pass extends Auth {
    public Pass(String message) {
        super(message);
    }
}

// для некоректного паролю
class BadPass extends Pass {
    public BadPass(String message) {
        super(message);
    }
}

// для забороненого паролю
class BanPass extends Pass {
    public BanPass(String message) {
        super(message);
    }
}

// для помилки при додаванні забороненого паролю
class BadBan extends Pass {
    public BadBan(String message) {
        super(message);
    }
}

// для випадку, коли користувача не знайдено
class NoUser extends Auth {
    public NoUser(String message) {
        super(message);
    }
}

// для помилки автентифікації
class Login extends Auth {
    public Login(String message) {
        super(message);
    }
}

// для перевищення максимальної кількості користувачів
class MaxUser extends Auth {
    public MaxUser(String message) {
        super(message);
    }
}
