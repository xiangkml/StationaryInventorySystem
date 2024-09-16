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
        boolean validLogin = false, validId;

        Scanner sc = new Scanner(System.in);
        ArrayList<Staff> staffList = readStaffFile();
        String id, pw;

        Main.displayHeader();
        System.out.println("|                       Login Page                        |");
        System.out.println(" ========================================================= ");

        do {
            do {
                validId = false;
                stfIdRules();
                System.out.print("Enter your staff ID [STFxxx]: ");
                id = sc.nextLine().toUpperCase();
                if (id.equals("-1")) {
                    validate = -1;
                    validId = true;
                } else {
                    for (Staff stf : staffList) {
                        if (stf.getId().equals(id)) {
                            validId = true;
                            break;
                        }
                    }

                    if (!validId) {
                        System.out.println("\n* Invalid Staff ID!! *");
                        System.out.println("* Please Re-enter a Valid Staff ID! *\n");
                    }
                }

            } while (!validId);

            if (validate != -1) {

                stfPswdRules();
                System.out.print("Enter your Password: ");
                pw = sc.nextLine();
                if (pw.equals("-1")) {
                    validate = -1;
                } else {
                    for (Staff staff : staffList) {
                        if (staff.getId().equals(id) && staff.getPassword().equals(pw)) {
                            validate = 1;
                            System.out.println("\nSuccessfully logged in!!!");
                            validLogin = true;
                            break;
                        }
                    }

                    if (validate != 1) {
                        System.out.println("\n* Invalid Password!! *");
                        System.out.println("* Please Re-enter a Valid Password! *\n");
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

        Main.displayHeader();

        System.out.println("|                    Registration Page                    |");
        System.out.println(" ========================================================= ");

        Supplier.nameRules();
        System.out.print("Enter Your Full Name [THAI KU LA]: ");
        newStfName = sc.nextLine().toUpperCase().trim();
        if (newStfName.equals("-1"))
            return;

        do {
            stfIcRules();
            System.out.print("Enter Your IC Number: ");
            newStfIc = sc.nextLine().trim();
            if (newStfIc.equals("-1"))
                return;
            validIc = validateIC(newStfIc);

            if (!validIc) {
                System.out.println("\n* Invalid IC Number!! *");
                System.out.println("* Please Re-enter Your Identity Card Number Again! *");
            }
        } while (!validIc);


        do {
            stfPswdRules();
            System.out.print("Set Your Password: ");
            newStfPw1 = sc.nextLine().trim();
            if (newStfPw1.equals("-1"))
                return;

            // validation password
            if (newStfPw1.length() >= 8 && newStfPw1.length() <= 12) {


                for (char ch : newStfPw1.toCharArray()) {
                    if (Character.isDigit(ch))
                        digitNum++;
                    if (Character.isLetter(ch))
                        characterNum++;
                }

                if ((digitNum != 0) && (characterNum != 0)) {

                    System.out.print("Confirm Password: ");
                    newStfPw2 = sc.nextLine().trim();
                    if (newStfPw2.equals("-1"))
                        return;

                    if (!(newStfPw1.equals(newStfPw2))) {
                        System.out.println("\n* Your password is not same! *");
                    } else {
                        validPw = true;
                    }

                } else {
                    System.out.println("\n* Your password must contain at least one digit and one character! *");
                }

            } else {
                System.out.println("\n* Error! Your password length is not fulfill the conditions. *");
            }
            if (!validPw)
                System.out.println("Please Set Your Password Again!");

        } while (!validPw);


        Staff newStf = new Staff(newStfName, newStfIc, newStfPw1);
        ArrayList<Staff> staffList = readStaffFile();
        staffList.add(newStf);
        writeStaffFile(staffList);

        System.out.println("Successfully registered!");
        System.out.println(" ===================================================");
        System.out.println("|                Staff Information                  |");
        System.out.println(" ===================================================");
        System.out.println(" Staff ID: " + newStf.getId());
        System.out.println(" Staff Name: " + newStf.getName());
        System.out.println(" Staff IC: " + newStf.getIc());
        System.out.println("\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
        System.out.println("           Welcome to ThaiKuLa Company~");
        System.out.println("            Hope you have a nice day!\n");
        System.out.print("             Press Enter to continue...");
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
            System.out.println("Error (read staff file):" + e.getMessage());
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
            System.out.println("Error (write staff file):" + e.getMessage());
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

    public static void stfIdRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                      The Staff ID Should Be:                          ");
        System.out.println(" 1. Start with 'STF'                                                   ");
        System.out.println(" 2. Followed By 3 digits                                               ");
        System.out.println(" 3. Total Length of Staff ID is 6                                      ");
        System.out.println("                                                                       ");
        System.out.println(" * Enter '-1' in Any Field If You Want to Exit to Previous Page *      ");
        System.out.println("---------------------------------------------------------------------\n");
    }

    public static void stfPswdRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                   The Staff Password Should Be:                       ");
        System.out.println(" 1. Length is from 8 to 12                                             ");
        System.out.println(" 2. Must Contain At Least one digit and one character                  ");
        System.out.println("                                                                       ");
        System.out.println(" * Enter '-1' in Any Field If You Want to Exit to Previous Page *      ");
        System.out.println("---------------------------------------------------------------------\n");
    }

    public static void stfIcRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                   The IC Number Should Be:                            ");
        System.out.println(" 1. In Format 'xxxxxx-xx-xxxx'                                         ");
        System.out.println(" 2. Can Only Enter digits (0-9) and '-'                                ");
        System.out.println(" 3. Total Length of IC Number is 14                                    ");
        System.out.println("                                                                       ");
        System.out.println(" * Enter '-1' in Any Field If You Want to Exit to Previous Page *      ");
        System.out.println("---------------------------------------------------------------------\n");
    }

}
