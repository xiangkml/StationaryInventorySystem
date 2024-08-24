import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Staff {
    private String name, ic, id, password;

    public Staff(String name, String ic, String password) {
        this.name = name;
        this.ic = ic;
        this.password = password;
        this.id = String.format("STF%03d", countStf() + 1);
    }

    public Staff(String name, String ic, String id, String password) {
        this.name = name;
        this.ic = ic;
        this.password = password;
        this.id = id;
    }

    public Staff() {

    }

    public String getName() {
        return name;
    }

    public String getIc() {
        return ic;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public boolean login() {

        int validate = 0;
        boolean validLogin = false;

        Scanner sc = new Scanner(System.in);
        ArrayList<Staff> staffList = readStaffFile();
        String id, pw;

        System.out.println("----------- Login Page -----------");

        do {
            System.out.println("Enter '-1' to exit");
            System.out.print("Enter your staff ID [STFxxx]: ");
            id = sc.nextLine().toUpperCase();
            if (id.equals("-1")) {
              validate = -1;
            } else {
                System.out.print("Enter your Password: ");
                pw = sc.nextLine();
                if (pw.equals("-1")) {
                    validate = -1;
                } else {
                    for (Staff staff : staffList) {
                        if (staff.getId().equals(id) && staff.getPassword().equals(pw)) {
                            validate = 1;
                            System.out.println("Successfully logged in!");
                            validLogin = true;
                            break;
                        }
                    }

                     if(validate != 1) {
                        System.out.println("Invalid Username & Password");
                        System.out.println("Please Enter Again");
                    }
                }
            }

        } while (validate == 0);

        return validLogin;

    }

    public static void register() {

        String newStfIc, newStfName, newStfPw1, newStfPw2;
        boolean validPw = false;
        boolean validIc = false;
        int characterNum = 0, digitNum = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Registration Page -----------");
        System.out.println("------- [-1 for -> previous page] -------");

        System.out.println("Enter your Full Name [THAI KU LA]: ");
        newStfName = sc.nextLine().toUpperCase().trim();
        if (newStfName.equals("-1"))
            return;

        do {
            System.out.println("Enter your IC number [xxxxxx-xx-xxxx]: ");
            newStfIc = sc.nextLine().trim();
            if (newStfIc.equals("-1"))
                return;
            validIc = validateIC(newStfIc);

            if (!validIc) {
                System.out.println("Invalid IC number");
                System.out.println("Please Re-enter your Identity Card Number again!");
            }
        } while (!validIc);


        do {
            System.out.println("Setting your password [length:8-12, contains character & digit]: ");
            newStfPw1 = sc.nextLine().trim();
            if (newStfPw1.equals("-1"))
                return;
            System.out.println("Please Re-enter your password again:");
            newStfPw2 = sc.nextLine().trim();
            if (newStfPw2.equals("-1"))
                return;

            // validation password
            if (newStfPw1.length() >= 8 && newStfPw1.length() <= 12) {

                if (newStfPw1.equals(newStfPw2)) {
                    for (char ch : newStfPw1.toCharArray()) {
                        if (Character.isDigit(ch))
                            digitNum++;
                        if (Character.isLetter(ch))
                            characterNum++;
                    }

                    if ((digitNum != 0) && (characterNum != 0)) {
                        validPw = true;
                    } else {
                        System.out.println("Your password must contain at least one digit and one character!");
                    }

                } else {
                    System.out.println("Your password are not same!");
                }

            } else {
                System.out.println("Error! Your password length is not fulfill the conditions.");
            }
            if (!validPw)
                System.out.println("Please set your password again!");

        } while (!validPw);

        Staff newStf = new Staff(newStfName, newStfIc, newStfPw1);
        ArrayList<Staff> staffList = readStaffFile();
        staffList.add(newStf);
        writeStaffFile(staffList);

        System.out.println("Successfully registered!");
        System.out.println("Welcome to ThaiKuLa Company!");
        System.out.println("Hope you have a nice day!");

        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    public static ArrayList<Staff> readStaffFile() {
        String pathName = "staff.txt";

        File file = new File(pathName);
        ArrayList<Staff> staffList = new ArrayList<>();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 4) { // Ensure the line has the expected number of fields
                    String name = data[0];
                    String ic = data[1];
                    String id = data[2];
                    String password = data[3];

                    Staff staff = new Staff(name, ic, id, password);
                    staffList.add(staff);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return staffList;
    }

    public static void writeStaffFile(ArrayList<Staff> stfList) {
        String pathName = "staff.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Staff stf : stfList) {
                writer.write(stf.getName());
                writer.write('|');
                writer.write(stf.getIc());
                writer.write('|');
                writer.write(stf.getId());
                writer.write('|');
                writer.write(stf.getPassword());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

    public static boolean validateIC(String icNumber) {

        // Regular expression for IC number format
        final Pattern IC_PATTERN = Pattern.compile("^\\d{6}-\\d{2}-\\d{4}");

        if (icNumber == null || !IC_PATTERN.matcher(icNumber).matches()) {
            return false;
        }

        // Remove hyphens
        icNumber = icNumber.replace("-", "");

        String datePart = icNumber.substring(0, 6);

        // Check if all characters are digits
        if (!icNumber.matches("\\d{12}")) {
            return false;
        } else return ExtraFunction.isValidDate(datePart); // Validation Of the date
    }

    private int countStf() {
        ArrayList<Staff> stf = readStaffFile();
        return stf.size();
    }


}
