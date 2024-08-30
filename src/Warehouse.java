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

    public void addWarehouse() {
        String whName, whID = "", address = "";
        Scanner sc = new Scanner(System.in);
        boolean validId = true;

        System.out.println("----------- Warehouse Registration -----------");
        System.out.println("Enter '-1' to exit");

        System.out.println("Enter warehouse name [Warehouse Johor]: ");
        whName = sc.nextLine().trim();
        if (whName.equals("-1")) {
            return;
        }

        do {
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

    public void deleteWarehouse() {

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
            if(whID.equals("-1")){
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

    public void viewWarehouse() {
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

    public void viewAllWarehouse() {
        ArrayList<Warehouse> whList = readMasterWarehouseFile();
        for (Warehouse wh : whList) {
            // display all information for each supplier
        }

        System.out.println("Successfully display all the warehouse");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    public void viewOneWarehouse() {

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
            }


        } while (!validID);

        System.out.println("Successfully display the warehouse details");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();

    }

    public void editWarehouse() {

        Scanner sc = new Scanner(System.in);
        String whID;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Warehouse editWh = null;
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
                    break;
                }
            }

        } while (!validID);

        // information before edit
        System.out.println("Supplier Information Before Edited: ");
        editSup.displaySup();
        System.out.println("Do you sure you want to edit this supplier information? [Y = YES || Others = NO]");
        confirmationBefore = sc.nextLine().toUpperCase().trim().equals("Y");

        if (confirmationBefore) {

            do{
                Main.editSupplierMenu();
                int menuInput = ExtraFunction.menuInput(6);

                switch (menuInput) {
                    case 1:
                        // edit name
                        editSup.editName();
                        break;

                    case 2:
                        //edit email
                        editSup.editEmail();
                        break;

                    case 3:
                        // edit tel
                        editSup.editTel();
                        break;

                    case 4:
                        // edit address
                        editSup.editAddress();
                        break;

                    case 5:
                        // edit supplied product
                        editSup.editSuppliedProd();
                        break;

                    default:
                        // return
                        return;
                }

                System.out.println("Do you want to continue edit this Supplier information? [Y = YES || Others = NO]");
                continueEdit = sc.nextLine().toUpperCase().trim().equals("Y");
            }while(continueEdit);

        }

        // information after edit
        System.out.println("Supplier Information After Edited: ");
        editSup.displaySup();
        System.out.println("Do you sure you want to save this supplier information? [Y = YES || Others = NO]");
        confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");
        if (confirmationAfter) {
            for (Supplier supplier : supplierList) {
                if (id.equals(editSup.getId())) {
                    supplier = editSup;
                    break;
                }
            }
            writeSupplierFile(supplierList);
            System.out.println("Successfully updated the latest supplier information");
            System.out.println("Press Enter to continue...");
            sc.nextLine();
        }

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
            System.out.println("Error :" + e.getMessage());
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
            System.out.println("Error :" + e.getMessage());
        }
    }

}

