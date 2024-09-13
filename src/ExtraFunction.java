import java.util.Scanner;

public class ExtraFunction {

    // Validate Each Menu Input
    public static int menuInput(int endNum) {
        boolean continueLoop = true;
        int inputOfMenu = 0;
        Scanner sc = new Scanner(System.in);

        do {
            try {
                System.out.print("Enter your choice: ");
                inputOfMenu = sc.nextInt();
                sc.nextLine();
                if ((inputOfMenu < 1) || (inputOfMenu > endNum)) {
                    System.out.println("Only enter numbers between 1 and " + endNum + ".");
                }
                else {
                    continueLoop = false;
                }
            } catch (Exception e) {
                System.out.println("Invalid Input.");
                System.out.println("Only enter integer!");
                sc.nextLine();
            }
        } while (continueLoop);

        return inputOfMenu;
    }

    public static boolean isValidDate(String datePart) {
        int year = Integer.parseInt(datePart.substring(0, 2));
        int month = Integer.parseInt(datePart.substring(2, 4));
        int day = Integer.parseInt(datePart.substring(4, 6));

        // Convert two-digit year to four-digit year
        year += (year >= 0 && year <= 23) ? 2000 : 1900;

        if (month < 1 || month > 12) {
            return false;
        }

        int maxDay = 31;
        switch (month) {
            case 4: case 6: case 9: case 11:
                maxDay = 30;
                break;
            case 2:
                maxDay = (isLeapYear(year)) ? 29 : 28;
                break;
        }

        return day >= 1 && day <= maxDay;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static boolean checkPattern(String str, String pattern) {
        return str.matches(pattern);
    }

}

