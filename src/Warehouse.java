import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
        boolean validId, confirmation = false;

        Main.displayHeader();
        System.out.println("|                 Warehouse Registration                  |");
        System.out.println(" ========================================================= ");

        whNameRules();
        System.out.print("Enter Warehouse Name [Warehouse Johor]: ");
        whName = sc.nextLine().trim();
        if (whName.equals("-1")) {
            return;
        }

        do {
            validId = true;
            whIdRules();
            System.out.print("\nEnter Warehouse ID [JHR004]: ");
            whID = sc.nextLine().trim().toUpperCase();
            if (whID.equals("-1")) {
                return;
            }

            if (!ExtraFunction.checkPattern(whID, "^[A-Z]{3}\\d{3}$")) {
                System.out.println("\n* Invalid WareHouse ID Format!! *");
                System.out.println("* Please Re-enter a Valid WareHouse ID! *");
                validId = false;
            }

            ArrayList<Warehouse> warehouse = readMasterWarehouseFile();
            for(Warehouse w : warehouse){
                if(w.getWhID().equals(whID)){
                    validId = false;
                    System.out.println("\n* This ID has been Used!! *");
                    System.out.println("* Please Re-enter a Valid Warehouse ID! *");
                    break;
                }
            }

        } while (!validId);


        System.out.print("\nEnter Warehouse Address: ");
        address = sc.nextLine().trim().toUpperCase();
        if (address.equals("-1")) {
            return;
        }

        Warehouse newWh = new Warehouse(whID, whName, address);

        // display new warehouse info
        // double confirm from the user
        System.out.println("\n ---------------------------------------------------------- ");
        System.out.println("|              Confirmation for New Warehouse              |");
        System.out.println(" ---------------------------------------------------------- ");
        newWh.displayWh();

        System.out.print("\nDo you sure the warehouse information is correct? [Y = YES || Others = NO]: ");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");
        sc.nextLine();

        if (confirmation) {
            // if user confirm then process below
            ArrayList<Warehouse> warehouseList = readMasterWarehouseFile();
            warehouseList.add(newWh);
            writeMasterWarehouseFile(warehouseList);

            ArrayList<Product> masterP = Product.readMasterProductFile();
            ArrayList<WhProd> stock = WhProd.readWarehouseProductFile();

            for(Product p : masterP){
                stock.add(new WhProd(newWh.getWhID(),p.getProdSKU()));
            }

            WhProd.writeWarehouseProductFile(stock);

            System.out.println("\nSuccessfully registered a new warehouse!!!");

        } else {
            System.out.println("\n* Failed to Add New WareHouse! *");
        }

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void deleteWarehouse() {

        String whID;
        Warehouse whDel = null;
        boolean validID = false, confirmation ;
        ArrayList<Warehouse> whList;
        Scanner sc = new Scanner(System.in);

        Main.displayHeader();
        System.out.println("|                    Delete Warehouse                     |");
        System.out.println(" ========================================================= ");

        do {
            whIdRules();
            System.out.print("Enter the Warehouse's ID that you wish to delete [KLG001]: ");
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
                System.out.println("\n* Invalid Warehouse ID!! *");
                System.out.println("* Please Re-enter a Valid Warehouse ID *");
            } else {
                // display warehouse information that need to be deleted
                // double confirm from user
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("               Warehouse Information Wanted to be Deleted:\n");
                System.out.println("Warehouse ID: " + whDel.getWhID());
                System.out.println("Warehouse Name: " + whDel.getWhName());
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                System.out.print("\nDo you sure want to delete this warehouse? [Y = YES || Others = NO]: ");
                confirmation = sc.next().trim().equalsIgnoreCase("Y");
                sc.nextLine();

                if (confirmation) {
                    // if user confirm
                    whList.remove(whDel);
                    writeMasterWarehouseFile(whList);

                    ArrayList<WhProd> stockList = WhProd.readWarehouseProductFile();

                    for (Iterator<WhProd> iterator = stockList.iterator(); iterator.hasNext(); ) {
                        WhProd item = iterator.next();
                        if (item.getWhId().equals(whDel.getWhID())) {
                            iterator.remove();
                        }
                    }

                    WhProd.writeWarehouseProductFile(stockList);

                    System.out.println("\nSuccessfully Deleted the Warehouse!!!");
                } else {
                    System.out.println("\n* Failed to Delete the Warehouse! *");
                }
            }

        } while ((!validID));

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

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
        ArrayList<Product> masterProd = Product.readMasterProductFile();

        int noWh = 1;

        System.out.println("\n ========================================================================================================= ");
        System.out.println("|                                             Warehouse List                                              |");
        System.out.println(" ========================================================================================================= ");

        for (Warehouse wh : whList) {

            // display all information for each product
            System.out.printf("| %02d. | %-6s | %-25s |", noWh, wh.getWhID(), wh.getWhName());

            for (WhProd whP : prodList) {
                if (whP.getWhId().equals(wh.getWhID())) {
                    for(Product p: masterProd){
                        if(p.getProdSKU().equals(whP.getProdSKU())){
                            System.out.printf(" %-6s | %-35s | %4d | %4d |\n", whP.getProdSKU(), p.getProdName(), whP.getQuantity(), whP.getReorderLv());
                            System.out.printf("|%5s|%8s|%27s|", "", "", "");
                            //System.out.printf("| %02d. | %-6s | %whP.getProductSKU() + " " + whP.getProdName() + " " + whP.getQuantity() + " " + whP.getReorderLv());

                        }
                    }

                }
            }
            System.out.printf("%62s|", "");

            System.out.println(" ");

            noWh++;
        }
        System.out.println(" ========================================================================================================= ");

        System.out.println("\n                        Successfully Display All the Warehouse Details!!");
        System.out.print("                                   Press Enter to Continue...");
        new Scanner(System.in).nextLine();

    }

    public static void viewOneWarehouse() {

        Scanner sc = new Scanner(System.in);
        String whID;
        boolean validID = false;
        Warehouse viewWh = null;
        ArrayList<Warehouse> whList;
        ArrayList<WhProd> prodList = WhProd.readWarehouseProductFile();
        ArrayList<Product> masterProd = Product.readMasterProductFile();

        Main.displayHeader();
        System.out.println("|           View Specific Warehouse Information           |");
        System.out.println(" ========================================================= ");

        do {
            whIdRules();
            System.out.print("Enter the Warehouse's ID that you wish to view [KLG001]: ");
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
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.printf("                     Information for Warehouse %6s\n\n", viewWh.getWhID());
                viewWh.displayWh();
                System.out.print("Warehouse Product(s): ");

                for (WhProd whP : prodList) {
                    if (whP.getWhId().equals(viewWh.getWhID())) {
                        for(Product p: masterProd){
                            if(p.getProdSKU().equals(whP.getProdSKU())){
                                System.out.printf("%6s , %-30s , %4d , %4d\n%22s", whP.getProdSKU(), p.getProdName(), whP.getQuantity(), whP.getReorderLv(), "");
                            }
                        }

                    }
                }
                System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            }
            else{
                System.out.println("\n* Invalid Warehouse ID!! *");
                System.out.println("* Please Re-enter Warehouse's ID! *\n");
            }

        } while (!validID);

        System.out.println("Successfully Display the Warehouse Details!!!");
        System.out.print("Press Enter to Continue...");
        new Scanner(System.in).nextLine();

    }

    public static void editWarehouse() {

        Scanner sc = new Scanner(System.in);
        String whID;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Warehouse editWh = null, oriWh = null;
        ArrayList<Warehouse> whList;

        Main.displayHeader();
        System.out.println("|                     Edit Warehouse                      |");
        System.out.println(" ========================================================= ");

        do {
            whIdRules();
            System.out.print("Enter the Warehouse's ID that you wish to edit [KLG001]: ");
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
                System.out.println("\n* Invalid Warehouse ID!! *");
                System.out.println("* Please Re-enter Warehouse's ID! *\n");
            }

        } while (!validID);

        // information before edit
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                  Warehouse Information Before Edited: \n");
        editWh.displayWh();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        System.out.print("Do you sure you want to edit this Warehouse Information? [Y = YES || Others = NO]: ");
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

                System.out.print("Do you want to continue edit this Warehouse Information? [Y = YES || Others = NO]: ");
                continueEdit = sc.nextLine().toUpperCase().trim().equals("Y");
            } while (continueEdit);

            // information after edit
            System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("                 Warehouse Information After Edited: \n");
            editWh.displayWh();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            System.out.print("Do you sure you want to save this Warehouse Information? [Y = YES || Others = NO]: ");
            confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");

            if (confirmationAfter) {
                for (Warehouse wh : whList) {
                    if (wh.getWhID().equals(oriWh.getWhID())) {
                        wh = editWh;
                        break;
                    }
                }
                writeMasterWarehouseFile(whList);

                ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();

                for (WhProd w : whProdList) {
                    if (whID.equals(w.getWhId())) {
                        w.setWhId(editWh.getWhID());
                    }
                }
                WhProd.writeWarehouseProductFile(whProdList);

                System.out.println("Successfully Updated the Latest Warehouse Information!!");
            }
            else{
                System.out.println("Failed to Edit the Warehouse Information");
            }
        }

        System.out.print("Press Enter to Continue...");
        sc.nextLine();
    }

    public void editName() {

        whNameRules();
        System.out.print("Enter a New Name for Warehouse '" + whID + "': ");
        String name =  new Scanner(System.in).nextLine().trim();
        if(name.equals("-1")){
            return;
        }
        this.setWhName(name);

    }

    public void editID() {

        Scanner sc = new Scanner(System.in);
        boolean validID ;
        do {
            validID = true;
            whIdRules();
            System.out.print("Enter a New ID for Warehouse '" + whID + "': ");
            String newID = sc.nextLine().trim().toUpperCase();
            if(newID.equals("-1")){
                return;
            }
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
                    System.out.println("\n* The Warehouse ID has been Used!! *");
                    System.out.println("* Please Re-enter a Valid Warehouse ID! *\n");
                }

            } else {
                System.out.println("\n* Invalid Warehouse ID Format!! *");
                System.out.println("* Please Re-enter a Valid ID! *\n");
            }
        } while (!validID);

    }

    public void editAddress() {

        System.out.print("Enter a New Address for Warehouse '" + whID + "': ");
        String address = new Scanner(System.in).nextLine().trim();
        if(address.equals("-1")){
            return;
        }
        this.setAddress(address);

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
                    String address = data[2];

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

    public static void whNameRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                  The WareHouse Name Should Be:                        ");
        System.out.println(" 1. Check if the name is correct before pressing 'Enter'               ");
        System.out.println(" 2. Avoid containing special character [@,#,$,...], especially '|'     ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");
    }

    public static void whIdRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                      The WareHouse ID Should Be:                      ");
        System.out.println(" 1. Start With 3 Characters                                            ");
        System.out.println(" 3. Followed By 3 digits                                               ");
        System.out.println(" 4. Total Length of Product SKU is 6                                   ");
        System.out.println(" 5. In Format [XXXxxx]                                                 ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");

    }

}

