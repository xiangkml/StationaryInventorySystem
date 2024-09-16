import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Warehouse {

    private String whID, whName, address;

    public Warehouse(String whID, String whName, String address) {
        this.whID = whID;
        this.whName = whName;
        this.address = address;
    }

    public String getWhID() {
        return whID;
    }

    public void setWhID(String whID) {
        this.whID = whID;
    }

    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void addWarehouse() {
        String whName, whID = "", address = "";
        Scanner sc = new Scanner(System.in);
        boolean validId;

        System.out.println("----------- Warehouse Registration -----------");
        System.out.println("Enter '-1' to exit");

        System.out.println("Enter warehouse name [Warehouse Johor]: ");
        whName = sc.nextLine().trim();
        if (whName.equals("-1")) {
            return;
        }

        do {
            validId = true;
            System.out.println("Enter warehouse ID [JHR004]: ");
            whID = sc.nextLine().trim().toUpperCase();
            if (whID.equals("-1")) {
                return;
            }

            if (!ExtraFunction.checkPattern(whID, "^[A-Z]{3}\\d{3}$")) {
                System.out.println("The warehouse ID format should be : 3 Letters follow by 3 Digit.");
                validId = false;
            }
        } while (!validId);


        System.out.println("Enter warehouse address : ");
        address = sc.nextLine().trim().toUpperCase();
        if (address.equals("-1")) {
            return;
        }

        Warehouse newWh = new Warehouse(whID, whName, address);

        // display new warehouse info
        // double confirm from the user

        // if user confirm then process below
        ArrayList<Warehouse> warehouseList = readMasterWarehouseFile();
        warehouseList.add(newWh);
        writeMasterWarehouseFile(warehouseList);

        System.out.println("Successfully registered a new warehouse!");
        System.out.println("Press Enter to continue...");
        sc.nextLine();

    }

    public static void deleteWarehouse() {

        String whID;
        Warehouse whDel = null;
        boolean validID = false;
        ArrayList<Warehouse> whList;
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Delete Warehouse -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the warehouse's id that you wish to delete [KLG001]: ");
            whID = sc.nextLine().trim().toUpperCase();
            if (whID.equals("-1")) {
                return;
            }

            whList = readMasterWarehouseFile();
            for (Warehouse wh : whList) {
                if (whID.equals(wh.getWhID())) {
                    validID = true;
                    whDel = wh;
                    break;
                }
            }
            if (!validID) {
                System.out.println("Invalid warehouse ID");
                System.out.println("Please re-enter a valid warehouse ID");
            } else {
                // display warehouse information that need to be deleted
                // double confirm from user

                // if user confirm
                whList.remove(whDel);
                writeMasterWarehouseFile(whList);
                System.out.println("Successfully deleted the warehouse");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }

        } while ((!validID));

    }

    public static void viewWarehouse() {
        int menuInput;
        boolean returnPage = false;

        do {
            Main.viewWarehouseMenu();
            menuInput = ExtraFunction.menuInput(3);
            switch (menuInput) {

                case 1:
                    viewAllWarehouse();
                    break;

                case 2:
                    viewOneWarehouse();
                    break;

                default:
                    returnPage = true;
            }
        } while (!returnPage);

    }

    public static void viewAllWarehouse() {
        ArrayList<Warehouse> whList = readMasterWarehouseFile();
        ArrayList<WhProd> prodList = WhProd.readWarehouseProductFile();

        for (Warehouse wh : whList) {
            // display all information for each warehouse

            // design product listed
            System.out.println("--------------------------------------- Warehouse Product ---------------------------------------");
            for (WhProd whP : prodList) {
                if (whP.getWhId().equals(wh.getWhID())) {
                    System.out.println(whP.getProductSKU() + " " + whP.getProdName() + " " + whP.getQuantity() + " " + whP.getReorderLv());
                }
            }
        }

        System.out.println("Successfully display all the warehouse detail");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    public static void viewOneWarehouse() {

        Scanner sc = new Scanner(System.in);
        String whID;
        boolean validID = false;
        Warehouse viewWh = null;
        ArrayList<Warehouse> whList;

        System.out.println("----------- View Specific Warehouse -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the warehouse's id that you wish to view [KLG001]: ");
            whID = sc.nextLine().trim().toUpperCase();
            if (whID.equals("-1")) {
                return;
            }
            whList = readMasterWarehouseFile();
            for (Warehouse wh : whList) {
                if (whID.equals(wh.getWhID())) {
                    validID = true;
                    viewWh = wh;
                    break;
                }
            }

            if (validID) {
                // display information for warehouse

                // design product listed
                System.out.println("--------------------------------------- Warehouse Product ---------------------------------------");
                ArrayList<WhProd> prodList = WhProd.readWarehouseProductFile();

                for (WhProd whP : prodList) {
                    if (whP.getWhId().equals(whID)) {
                        System.out.println(whP.getProductSKU() + " " + whP.getProdName() + " " + whP.getQuantity() + " " + whP.getReorderLv());
                    }
                }
            }


        } while (!validID);

        System.out.println("Successfully display the warehouse details");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();

    }

    public static void editWarehouse() {

        Scanner sc = new Scanner(System.in);
        String whID;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Warehouse editWh = null, oriWh = null;
        ArrayList<Warehouse> whList;

        System.out.println("----------- Edit Warehouse -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the warehouse's id that you wish to edit [KLG001]: ");
            whID = sc.nextLine().trim().toUpperCase();
            if (whID.equals("-1")) {
                return;
            }

            whList = readMasterWarehouseFile();
            for (Warehouse wh : whList) {
                if (whID.equals(wh.getWhID())) {
                    validID = true;
                    editWh = wh;
                    oriWh = wh;
                    break;
                }
            }

            if (!validID) {
                System.out.println("Invalid warehouse ID!");
                System.out.println("Please re-enter warehouse's ID");
            }

        } while (!validID);

        // information before edit
        System.out.println("Warehouse Information Before Edited: ");
        editWh.displayWh();
        System.out.println("Do you sure you want to edit this warehouse information? [Y = YES || Others = NO]");
        confirmationBefore = sc.nextLine().toUpperCase().trim().equals("Y");

        if (confirmationBefore) {

            do {
                Main.editWarehouseMenu();
                int menuInput = ExtraFunction.menuInput(4);

                switch (menuInput) {
                    case 1:
                        // edit name
                        editWh.editName();
                        break;

                    case 2:
                        //edit id
                        editWh.editID();
                        break;

                    case 3:
                        // edit address
                        editWh.editAddress();
                        break;

                    default:
                        // return
                        return;
                }

                System.out.println("Do you want to continue edit this Warehouse information? [Y = YES || Others = NO]");
                continueEdit = sc.nextLine().toUpperCase().trim().equals("Y");
            } while (continueEdit);
            // information after edit
            System.out.println("Warehouse Information After Edited: ");
            editWh.displayWh();
            System.out.println("Do you sure you want to save this warehouse information? [Y = YES || Others = NO]");
            confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");
            if (confirmationAfter) {
                for (Warehouse wh : whList) {
                    if (wh.getWhID().equals(oriWh.getWhID())) {
                        wh = editWh;
                        break;
                    }
                }
                writeMasterWarehouseFile(whList);
                System.out.println("Successfully updated the latest warehouse information");
            }
        }

        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    public void editName() {

        System.out.println("Enter a new name for warehouse [" + whID + "]: ");
        this.setWhName(new Scanner(System.in).nextLine().trim());

    }

    public void editID() {

        Scanner sc = new Scanner(System.in);
        boolean validID = true;
        do {
            System.out.println("Enter a new id for warehouse [" + whID + "]: ");
            String newID = sc.nextLine().trim().toUpperCase();
            if (ExtraFunction.checkPattern(newID, "^[A-Z]{3}\\d{3}$")) {

                ArrayList<Warehouse> whList = readMasterWarehouseFile();
                for (Warehouse wh : whList) {
                    if (newID.equals(wh.getWhID())) {
                        validID = false;
                        break;
                    }
                }

                if (validID) {
                    this.setWhID(newID);
                } else {
                    System.out.println("The id has been used.");
                    System.out.println("Please re-enter a valid ID");
                }

            } else {
                System.out.println("Invalid id format.");
                System.out.println("Please re-enter a valid ID");
            }
        } while (!validID);

    }

    public void editAddress() {

        System.out.println("Enter a new address for warehouse [" + whID + "]: ");
        this.setAddress(new Scanner(System.in).nextLine().trim());

    }

    public void displayWh() {
        System.out.println("Warehouse ID: " + whID);
        System.out.println("Warehouse Name: " + whName);
        System.out.println("Warehouse Address: " + address);
    }

    public static ArrayList<Warehouse> readMasterWarehouseFile() {
        String pathName = "warehouse.txt";

        File file = new File(pathName);
        ArrayList<Warehouse> warehouseList = new ArrayList<>();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 3) { // Ensure the line has the expected number of fields
                    String whID = data[0];
                    String whName = data[1];
                    String address = data[1];

                    Warehouse warehouse = new Warehouse(whID, whName, address);
                    warehouseList.add(warehouse);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error (read warehouse file):" + e.getMessage());
        }
        return warehouseList;
    }

    public static void writeMasterWarehouseFile(ArrayList<Warehouse> whList) {
        String pathName = "warehouse.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Warehouse wh : whList) {
                writer.write(wh.getWhID());
                writer.write('|');
                writer.write(wh.getWhName());
                writer.write('|');
                writer.write(wh.getAddress());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error (write warehouse file):" + e.getMessage());
        }
    }

}

