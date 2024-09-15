import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Supplier {
    private String name, id, email, address, tel;
    private ArrayList<Product> supplyProduct = new ArrayList<>();

    public Supplier(String name, String email, String address, String tel, ArrayList<Product> supplyProduct) {
        this.name = name;
        this.id = String.format("SUP%03d", countSupplier() + 1);
        this.email = email;
        this.address = address;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public Supplier(String name, String id, String email, String address, String tel, ArrayList<Product> supplyProduct) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.address = address;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public ArrayList<Product> getSupplyProduct() {
        return supplyProduct;
    }

    public void setSupplyProduct(ArrayList<Product> supplyProduct) {
        this.supplyProduct = supplyProduct;
    }

    public static void addSupplier() {
        String supName, email = "", address = "", tel = "";
        boolean exitPage = false;
        int numOfSupplyProduct = 0;
        ArrayList<Product> supplyProduct = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        System.out.println(" =============================================== ");
        System.out.println("|             Supplier Registration             |");
        System.out.println(" =============================================== \n");

        nameRules();
        System.out.println("Enter Supplier Name [Wong Ann Nee]: ");
        supName = sc.nextLine().trim();
        exitPage = supName.equals("-1");

        if (!exitPage) {
            boolean validInput = true;
            do {
                System.out.println(" ");
                emailRules();
                System.out.println("\nEnter Supplier Email [thaikula520@gmail.com]: ");
                email = sc.nextLine().trim();
                exitPage = email.equals("-1");

                if (!ExtraFunction.checkPattern(email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                    System.out.println("\nPlease Enter Valid Email Address!\n");
                    validInput = false;
                }
            } while (!validInput);

        }

        if (!exitPage) {
            System.out.println("\nEnter Supplier Address: ");
            address = sc.nextLine().trim().toUpperCase();
            exitPage = address.equals("-1");
        }

        if (!exitPage) {
            boolean validInput;
            do {
                System.out.println(" ");
                telRules();
                System.out.println("\nEnter Telephone Number: ");
                tel = sc.nextLine().trim();
                exitPage = tel.equals("-1");
                validInput = true;
                if (!ExtraFunction.checkPattern(tel, "^01[02-46-9]-[0-9]{3} [0-9]{4}$|^011-[0-9]{4} [0-9]{4}$")) {
                    validInput = false;
                }
            } while (!validInput);
        }

        if (!exitPage) {
            boolean validInput = false;
            do {
                System.out.println(" ");
                inputIntRules();
                System.out.println("\nEnter Number of Product: ");
                try {
                    numOfSupplyProduct = sc.nextInt();
                    exitPage = (numOfSupplyProduct == -1);

                    if (numOfSupplyProduct < 1) {
                        System.out.println("Number of Product Cannot less than 1 !");
                        System.out.println("\nPlease Enter Valid Number of Product!\n");
                    } else {
                        validInput = true;
                    }

                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    System.out.println("Only enter integer!");
                    sc.nextLine();
                }
            } while (!validInput);
        }

        if (!exitPage) {
            for (int i = 0; (i < numOfSupplyProduct) && (!exitPage); i++) {

                boolean validProd = false;
                String skuCode;

                do {
                    System.out.println("Enter product SKU code [OLBOK007]:");
                    skuCode = sc.nextLine().trim();
                    exitPage = (skuCode.equals("-1"));

                    ArrayList<Product> masterProduct = Product.readMasterProductFile();
                    for (Product prod : masterProduct) {
                        if (prod.getProdSKU().equals(skuCode)) {
                            validProd = true;
                            supplyProduct.add(new Product(skuCode, prod.getProdName()));
                            break;
                        }
                    }
                    if (!validProd) {
                        System.out.println("Invalid product SKU code");
                        System.out.println("Please register new product before register a new supplier");
                    }

                } while ((!validProd) && (!exitPage));

                if (!exitPage) {

                    Supplier newSupplier = new Supplier(supName, email, address, tel, supplyProduct);

                    // display new supplier info
                    // double confirm from the user
                    System.out.println(" ========================================================= ");
                    System.out.println("|        Confirmation for New Supplier Information        |");
                    System.out.println(" ========================================================= \n");

                    newSupplier.displaySup();

                    // if user confirm then process below
                    ArrayList<Supplier> supplierList = readSupplierFile();
                    supplierList.add(newSupplier);
                    writeSupplierFile(supplierList);

                    System.out.println("Successfully registered a new supplier!");
                    System.out.print("Press Enter to Continue...");
                    sc.nextLine();
                }
            }
        }

    }

    public static void deleteSupplier() {

        String supID;
        Supplier supDel = null;
        boolean exitPage, validID = false;
        ArrayList<Supplier> supplierList;
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Delete Supplier -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the supplier's id that you wish to delete [SUP001]: ");
            supID = sc.nextLine().trim();
            exitPage = supID.equals("-1");

            if (!exitPage) {

                supplierList = readSupplierFile();
                for (Supplier supplier : supplierList) {
                    if (supID.equals(supplier.getId())) {
                        validID = true;
                        supDel = supplier;
                        break;
                    }
                }
                if (!validID) {
                    System.out.println("Invalid supplier ID");
                    System.out.println("Please re-enter a valid supplier ID");
                } else {
                    // display supplier information that need to be deleted
                    // double confirm from user

                    // if user confirm
                    supplierList.remove(supDel);
                    writeSupplierFile(supplierList);
                    System.out.println("Successfully deleted the supplier");
                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
                }
            }
        } while ((!validID) && (!exitPage));

    }

    public static void viewSupplier() {
        int menuInput;
        boolean returnPage = false;

        do {
            Main.viewSupplierMenu();
            menuInput = ExtraFunction.menuInput(3);
            switch (menuInput) {

                case 1:
                    Supplier.viewAllSupplier();
                    break;

                case 2:
                    Supplier.viewOneSupplier();
                    break;

                default:
                    returnPage = true;
            }
        } while (!returnPage);

    }

    public static void viewAllSupplier() {

        int noSup = 1;

        System.out.println(" =============================================================================================== ");
        System.out.println("|                                   Supplier Information List                                   |");
        System.out.println(" =============================================================================================== ");

        ArrayList<Supplier> supplierList = readSupplierFile();
        for (Supplier supplier : supplierList) {
            // display all information for each supplier
            System.out.printf("| %02d. | %-6s | %-41s ", noSup, supplier.getId(), supplier.getName());

            for (Product prod : supplier.getSupplyProduct()) {
                System.out.printf("| %-6s | %-25s |\n", prod.getProdSKU(), prod.getProdName());
                System.out.printf("|%5s|%8s|%43s", "", "", "");
            }

            System.out.println(" ");

            noSup++;
        }
        System.out.println(" =============================================================================================== \n");


        System.out.println("            Successfully Display All the Supplier            ");
        System.out.print("                   Press Enter to Continue...");
        new Scanner(System.in).nextLine();
    }

    public static void viewOneSupplier() {

        Scanner sc = new Scanner(System.in);
        String supID;
        boolean validID = false;
        Supplier viewSup = null;
        ArrayList<Supplier> supplierList;

        System.out.println("----------- View Specific Supplier -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the supplier's id that you wish to view [SUP001]: ");
            supID = sc.nextLine().trim().toUpperCase();
            if (supID.equals("-1")) {
                return;
            }
            supplierList = readSupplierFile();
            for (Supplier supplier : supplierList) {
                if (supID.equals(supplier.getId())) {
                    validID = true;
                    viewSup = supplier;
                    break;
                }
            }

            if (validID) {
                // display information for supplier
                System.out.println(" =============================================================================================== ");
                System.out.printf(" |                                 Information for Supplier %6s                               |", viewSup.getId());
                System.out.println(" =============================================================================================== ");
                System.out.println("|                                                                                               |");


                // display all information for each supplier
                System.out.printf("| %-6s | %-41s ", viewSup.getId(), viewSup.getName());

                for (Product prod : viewSup.getSupplyProduct()) {
                    System.out.printf("| %-6s | %-25s |\n", prod.getProdSKU(), prod.getProdName());
                    System.out.printf("|%8s|%43s", "", "");


                    System.out.println(" ");

                }
                System.out.println("|                                                                                               |");
                System.out.println(" =============================================================================================== \n");


            }
        } while (!validID);

        System.out.println("Successfully display supplier details");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();

    }

    public static void editSupplier() {

        Scanner sc = new Scanner(System.in);
        String supID;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Supplier editSup = null;
        ArrayList<Supplier> supplierList;

        System.out.println("----------- Edit Supplier -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the supplier's id that you wish to edit [SUP001]: ");
            supID = sc.nextLine().trim().toUpperCase();
            if (supID.equals("-1")) {
                return;
            }

            supplierList = readSupplierFile();
            for (Supplier supplier : supplierList) {
                if (supID.equals(supplier.getId())) {
                    validID = true;
                    editSup = supplier;
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

            do {
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
            } while (continueEdit);

            // information after edit
            System.out.println("Supplier Information After Edited: ");
            editSup.displaySup();
            System.out.println("Do you sure you want to save this supplier information? [Y = YES || Others = NO]");
            confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");
            if (confirmationAfter) {
                for (Supplier supplier : supplierList) {
                    if (supID.equals(editSup.getId())) {
                        supplier = editSup;
                        break;
                    }
                }
                writeSupplierFile(supplierList);
                System.out.println("Successfully Updated the Latest Supplier Information");

            }

        }

        System.out.print("Press Enter to Continue...");
        sc.nextLine();
    }

    public void editName() {

        System.out.println("Enter a new name for supplier [" + id + "]: ");
        this.setName(new Scanner(System.in).nextLine().trim());

    }

    public void editEmail() {

        Scanner sc = new Scanner(System.in);
        boolean validEmail;
        do {
            System.out.println("Enter a new email for supplier [" + id + "]: ");
            String email = sc.nextLine().trim();
            validEmail = ExtraFunction.checkPattern(email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
            if (validEmail) {
                this.setEmail(email);
            } else {
                System.out.println("Invalid email");
                System.out.println("Please re-enter a valid email");
            }
        } while (!validEmail);

    }

    public void editTel() {

        Scanner sc = new Scanner(System.in);
        boolean validTel;
        do {
            System.out.println("Enter a new tel for supplier [" + id + "]: ");
            String tel = sc.nextLine().trim();
            validTel = ExtraFunction.checkPattern(tel, "^01[02-46-9]-[0-9]{3} [0-9]{4}$|^011-[0-9]{4} [0-9]{4}$");
            if (validTel) {
                this.setTel(tel);
            } else {
                System.out.println("Invalid email");
                System.out.println("Please re-enter a valid email");
            }
        } while (!validTel);
    }

    public void editAddress() {

        System.out.println("Enter a new address for supplier [" + id + "]: ");
        this.setAddress(new Scanner(System.in).nextLine().trim());

    }

    public void editSuppliedProd() {

        Scanner sc = new Scanner(System.in);
        String oldSKU, newSKU;
        Product newProd = null, oldProd = null;
        boolean validOldSKU = false, validNewSKU = false;

        do {
            System.out.println("Enter the old Product SKU you wish to edit for supplier [" + id + "]: ");
            oldSKU = sc.nextLine().trim().toUpperCase();

            for (Product prod : supplyProduct) {
                if (prod.getProdSKU().equals(oldSKU)) {
                    validOldSKU = true;
                    oldProd = prod;
                    break;
                }
            }
            if (validOldSKU) {
                do {
                    System.out.println("Enter the new Product SKU to replace it: ");
                    newSKU = sc.nextLine().trim().toUpperCase();
                    ArrayList<Product> allProd = Product.readMasterProductFile();
                    for (Product prod : allProd) {
                        if (prod.getProdSKU().equals(newSKU)) {
                            validNewSKU = true;
                            newProd = prod;
                            this.supplyProduct.remove(oldProd);
                            this.supplyProduct.add(newProd);
                            break;
                        }
                    }

                    if (!validNewSKU) {
                        System.out.println("Invalid Product SKU");
                        System.out.println("Please re-enter a valid Product SKU");
                    }

                } while (!validNewSKU);

            } else {
                System.out.println("Invalid Product SKU");
                System.out.println("Please re-enter a valid Product SKU");
            }

        } while (!validOldSKU);

    }

    public void displaySup() {
        System.out.println("Supplier ID     : " + id);
        System.out.println("Supplier Name   : " + name);
        System.out.println("Supplier Email  : " + email);
        System.out.println("Supplier Address: " + address);
        System.out.println("Supplier Tel    : " + tel);
        System.out.println("Product Supplied: ");
        for (Product prod : supplyProduct) {
            System.out.println(prod.getProdSKU() + " , " + prod.getProdName());
        }
        System.out.println("\b\b");
    }


    public static ArrayList<Supplier> readSupplierFile() {
        String pathName = "supplier.txt";

        File file = new File(pathName);
        ArrayList<Supplier> supplierList = new ArrayList<>();
        ArrayList<Product> productList = new ArrayList<>();
        ArrayList<String> productSkuList = new ArrayList<>();
        ArrayList<Product> masterProd = Product.readMasterProductFile();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length >= 6) { // Ensure the line has the expected number of fields
                    String companyName = data[0];
                    String id = data[1];
                    String email = data[2];
                    String address = data[3];
                    String tel = data[4];

                    productSkuList.addAll(Arrays.asList(data).subList(5, data.length + 1));

                    //read product name from master product file
                    for(String sku:productSkuList){

                        for(Product p:masterProd){
                            if(sku.equals(p.getProdSKU())){
                                productList.add(new Product(p.getProdName(),p.getProdSKU()));
                            }
                        }

                    }

                    Supplier supplier = new Supplier(companyName, id, email, address, tel, productList);
                    supplierList.add(supplier);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return supplierList;
    }

    public static void writeSupplierFile(ArrayList<Supplier> supplierList) {
        String pathName = "supplier.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Supplier supplier : supplierList) {
                writer.write(supplier.getName());
                writer.write('|');
                writer.write(supplier.getId());
                writer.write('|');
                writer.write(supplier.getEmail());
                writer.write('|');
                writer.write(supplier.getAddress());
                writer.write('|');
                writer.write(supplier.getTel());
                writer.write('|');
                for (Product product : supplier.getSupplyProduct()) {
                    writer.write(product.getProdSKU());
                    writer.write('|');
                }
                writer.write('\b');
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

    private int countSupplier() {
        ArrayList<Supplier> supplier = readSupplierFile();
        return supplier.size();
    }

    public static void nameRules() {
        System.out.println(" =================================================================== ");
        System.out.println("|                         The Name Should Be:                        |");
        System.out.println("| 1. Only Include characters                                         |");
        System.out.println("| 2. Cannot Include Special Character(s) [@,!,?,#,...], exclude '/'  |");
        System.out.println("| 3. Cannot Include Digit(s) [0,1,2,3,...]                           |");
        System.out.println("|                                                                    |");
        System.out.println("|          * Enter '-1' in Any Field If You Want to Exit *           |");
        System.out.println(" =================================================================== ");
    }

    public static void emailRules() {
        System.out.println(" =================================================================== ");
        System.out.println("|                        The Email Should Be:                        |");
        System.out.println("| 1. Can Include character(s) and Digit(s)                           |");
        System.out.println("| 2. Can Include Special Character(s) [_,-,.]                        |");
        System.out.println("| 3. Must Include '@'                                                |");
        System.out.println("|                                                                    |");
        System.out.println("|          * Enter '-1' in Any Field If You Want to Exit *           |");
        System.out.println(" =================================================================== ");
    }

    public static void telRules() {
        System.out.println(" =================================================================== ");
        System.out.println("|                  The Telephone Number Should Be:                   |");
        System.out.println("| 1. Cannot Start from '015'                                         |");
        System.out.println("| 2. If Start From '01x', Then Will only Followed by 7 Digits        |");
        System.out.println("| 3. If Start From '011x', Then Will only Followed by 8 Digits       |");
        System.out.println("| 4. Example Format : [01x-xxx xxxx | 01x-xxxx xxxx]                 |");
        System.out.println("|                                                                    |");
        System.out.println("|          * Enter '-1' in Any Field If You Want to Exit *           |");
        System.out.println(" =================================================================== ");
    }

    public static void inputIntRules() {
        System.out.println(" =================================================================== ");
        System.out.println("|                       The Number Should Be:                        |");
        System.out.println("| 1. Can Only Enter Integer                                          |");
        System.out.println("| 2. Cannot Enter Negative Integer [-25,-431], except exit           |");
        System.out.println("|                                                                    |");
        System.out.println("|          * Enter '-1' in Any Field If You Want to Exit *           |");
        System.out.println(" =================================================================== ");
    }


}
