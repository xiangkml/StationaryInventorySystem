import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Product implements ProductInterface{

    protected String prodName, prodSKU;

    /* Constructor */

    /* product list use for master file that only store name and product sku*/
    public Product(String prodName, String prodSKU) {
        this(prodSKU);
        this.prodName = prodName;
    }

    public Product(String prodSKU) {
        this.prodSKU = prodSKU;
    }

    public Product() {
    }

    /*Getter & Setter*/
    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdSKU() {
        return prodSKU;
    }

    public void setProdSKU(String prodSKU) {
        this.prodSKU = prodSKU;
    }

    public static void addProduct() {
        String prodName, prodSKU;
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> masterProduct = readMasterProductFile();

        Main.displayHeader();
        System.out.println("|                       Add Product                       |");
        System.out.println(" ========================================================= ");

        prodNameRules();
        System.out.print("Enter Product Name [555 Notebook]: ");
        prodName = sc.nextLine().trim();
        if (prodName.equals("-1")) {
            return;
        }

        boolean validInput = true, confirmation = false;

        do {
            validInput = true;
            prodSKURules();
            System.out.print("Enter Product SKU Code [PLPEN001]: ");
            prodSKU = sc.nextLine().trim().toUpperCase();
            if (prodSKU.equals("-1")) {
                return;
            }

            if (!ExtraFunction.checkPattern(prodSKU, "^^[A-Z]{5}\\d{3}$")) {
                System.out.println("\n* Invalid Product SKU Code!! *");
                System.out.println("* Please Enter Valid SKU Code! *");
                validInput = false;
            }

            for(Product oldProd: masterProduct){
                if(prodSKU.equals(oldProd.getProdSKU())){
                    System.out.println("\n* The Product SKU Code is Duplicated *");
                    System.out.println("* Please Re-enter a New SKU Code *");
                    validInput = false;
                }
            }

        } while (!validInput);

        // display new product info
        // double confirm from the user
        Product newProduct = new Product(prodName, prodSKU);
        System.out.println("\n -------------------------------------------------------- ");
        System.out.println("|              Confirmation for New Product              |");
        System.out.println(" -------------------------------------------------------- ");
        newProduct.displayProduct();

        System.out.print("\nDo you sure the product information is correct? [Y = YES || Others = NO]: ");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");
        sc.nextLine();

        if (confirmation) {
            // if user confirm then process below
            masterProduct.add(newProduct);
            writeMasterProductFile(masterProduct);

            ArrayList<Warehouse> whList = Warehouse.readMasterWarehouseFile();
            ArrayList<WhProd> whProduct = WhProd.readWarehouseProductFile();
            for (Warehouse w : whList) {
                WhProd newWhProd = new WhProd(w.getWhID(), prodSKU);
                whProduct.add(newWhProd);
            }
            WhProd.writeWarehouseProductFile(whProduct);

            System.out.println("\nSuccessfully Add a New Product!!!");

        } else {
            System.out.println("\n* Failed to Add New Product! *");
        }



        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void editProduct() {

        Scanner sc = new Scanner(System.in);
        String prodID;
        int menuInput;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Product editProduct = null;
        ArrayList<Product> prodList = null;

        Main.displayHeader();
        System.out.println("|                      Edit Product                       |");
        System.out.println(" ========================================================= ");

        do {
            prodSKURules();
            System.out.print("Enter the Product SKU that You Wish to Edit [PLPEN001]: ");
            prodID = sc.nextLine().trim().toUpperCase();
            if (prodID.equals("-1")) {
                return;
            }

            prodList = readMasterProductFile();
            for (Product p : prodList) {
                if (prodID.equals(p.getProdSKU())) {
                    validID = true;
                    editProduct = p;
                    break;
                }
            }

            if (!validID) {
                System.out.println("\n* Invalid Product SKU Code!! *");
                System.out.println("* Please Re-enter Product SKU Code! *");
            }

        } while (!validID);

        // information before edit
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Product Information Before Edited: \n");
        editProduct.displayProduct();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        System.out.print("Do you sure you want to edit this product information? [Y = YES || Others = NO]: ");
        confirmationBefore = sc.nextLine().toUpperCase().trim().equals("Y");

        if (confirmationBefore) {

            do {
                Main.editProductMenu();
                menuInput = ExtraFunction.menuInput(3);

                switch (menuInput) {
                    case 1:
                        // edit name
                        editProduct.editName();
                        break;

                    case 2:
                        //edit sku
                        editProduct.editSKU();
                        break;

                    default:
                        // return
                        return;
                }

                System.out.print("Do you want to continue edit this product information? [Y = YES || Others = NO]: ");
                continueEdit = sc.nextLine().toUpperCase().trim().equals("Y");
            } while (continueEdit);

            // information after edit
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("Product Information After Edited: \n");
            editProduct.displayProduct();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            System.out.print("Do you sure you want to save this product information? [Y = YES || Others = NO]: ");
            confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");

            if (confirmationAfter) {
                writeMasterProductFile(prodList);

                ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();

                for (WhProd w : whProdList) {
                    if (prodID.equals(w.getProdSKU())) {
                        w.setProdSKU(editProduct.getProdSKU());
                    }
                }
                WhProd.writeWarehouseProductFile(whProdList);

                System.out.println("* Successfully updated the latest product information!!! *");
            }
        }

        System.out.print("\nPress Enter to Continue...");
        sc.nextLine();
    }

    public void editName() {

        System.out.print("Enter a new name for product [" + prodSKU + "]: ");
        this.setProdName(new Scanner(System.in).nextLine().trim());

    }

    public void editSKU() {

        Scanner sc = new Scanner(System.in);
        boolean validID ;

        do {
            validID = true;

            System.out.print("Enter a new SKU for product [" + prodSKU + "]: ");
            String newID = sc.nextLine().trim().toUpperCase();
            if (ExtraFunction.checkPattern(newID, "^[A-Z]{5}\\d{3}$")) {

                ArrayList<Product> prodList = readMasterProductFile();
                for (Product p : prodList) {
                    if (newID.equals(p.getProdSKU())) {
                        validID = false;
                        break;
                    }
                }

                if (validID) {
                    this.setProdSKU(newID);
                } else {
                    validID = false;
                    System.out.println("\n* The Product SKU has been used!! *");
                    System.out.println("* Please Re-enter a Valid SKU! *");
                }

            } else {
                validID = false;
                System.out.println("\n* Invalid Product SKU Format!! *");
                System.out.println("* Please Re-enter a Valid SKU! *");
            }
        } while (!validID);

    }

    public static void deleteProduct() {

        String prodSKU;
        boolean validID = false, confirmation = false;
        Product prodDel = new Product();
        ArrayList<Product> prodList;
        ArrayList<WhProd> stockList = WhProd.readWarehouseProductFile();
        ArrayList<Product> productDeleted = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        Main.displayHeader();
        System.out.println("|                     Delete Product                      |");
        System.out.println(" ========================================================= ");

        do {
            prodSKURules();
            System.out.print("Enter the Product's SKU that you wish to delete [OPPEN001]: ");
            prodSKU = sc.nextLine().trim().toUpperCase();
            if (prodSKU.equals("-1")) {
                return;
            }

            prodList = readMasterProductFile();
            for (Product prod : prodList) {
                if (prodSKU.equals(prod.getProdSKU())) {
                    validID = true;
                    prodDel = prod;
                    break;
                }
            }
            if (!validID) {
                System.out.println("\n* Invalid Product SKU Code!! *");
                System.out.println("* Please Re-enter a Valid Product SKU Code! *");
            } else {
                // display product information that need to be deleted
                // double confirm from user
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Product Information Wanted to be Deleted:\n");
                System.out.println("Product SKU Code: " + prodDel.getProdSKU());
                System.out.println("Product Name: " + prodDel.getProdName());
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                System.out.print("\nDo you sure want to delete this product? [Y = YES || Others = NO]: ");
                confirmation = sc.next().trim().equalsIgnoreCase("Y");
                sc.nextLine();

                if (confirmation) {
                    // if user confirm
                    prodList.remove(prodDel);
                    writeMasterProductFile(prodList);

                    for (Iterator<WhProd> iterator = stockList.iterator(); iterator.hasNext(); ) {
                        WhProd item = iterator.next();
                        if (item.getProductSKU().equals(prodDel.getProdSKU())) {
                            iterator.remove();
                        }
                    }

                    WhProd.writeWarehouseProductFile(stockList);

                    System.out.println("\nSuccessfully Delete the Product!!!");


                } else {
                    System.out.println("\n* Failed to Delete the Product! *");
                }

            }

        } while ((!validID));

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void viewProduct() {
        int menuInput;
        boolean returnPage = false;

        do {
            Main.viewProductMenu();
            menuInput = ExtraFunction.menuInput(3);
            switch (menuInput) {

                case 1:
                    viewAllProduct();
                    break;

                case 2:
                    viewOneProduct();
                    break;

                default:
                    returnPage = true;
            }
        } while (!returnPage);

    }

    public static void viewAllProduct() {
        ArrayList<Product> prodList = readMasterProductFile();
        int noProd = 1;

        System.out.println("\n =================================================================== ");
        System.out.println("|                         Product List                              |");
        System.out.println(" =================================================================== ");

        for (Product prod : prodList) {

            // display all information for each product
            System.out.printf("| %02d. | %-6s | %-48s |\n", noProd, prod.getProdSKU(), prod.getProdName());

            noProd++;
        }
        System.out.println(" =================================================================== ");


        System.out.println("\n         Successfully display all the Product Detail!!");
        System.out.print("                Press Enter to Continue...");
        new Scanner(System.in).nextLine();
    }

    public static void viewOneProduct() {

        Scanner sc = new Scanner(System.in);
        String prodID;
        boolean validID = false;
        ArrayList<Product> prodList;

        Main.displayHeader();
        System.out.println("|              View Specific Product Details              |");
        System.out.println(" ========================================================= \n");

        do {
            prodSKURules();
            System.out.print("Enter the Product's SKU that you wish to view [OPPEN001]: ");
            prodID = sc.nextLine().trim().toUpperCase();
            if (prodID.equals("-1")) {
                return;
            }
            prodList = readMasterProductFile();
            for (Product prod : prodList) {
                if (prodID.equals(prod.getProdSKU())) {
                    validID = true;
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.printf("     Product Details for Product '%6s'\n\n", prod.getProdSKU());
                    System.out.println(" Product SKU: " + prod.getProdSKU() +
                                       "\n Product Name: "+ prod.getProdName());
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    break;
                }
            }

            if (!validID) {
                System.out.println("\n* Invalid Product SKU Code!! *");
                System.out.println("* Please Re-enter a Valid Product SKU Code! *");
            }

        } while (!validID);

        System.out.println("\nSuccessfully display the Product Details!!!\n");
        System.out.print("Press Enter to Continue...");
        new Scanner(System.in).nextLine();

    }

    public void displayProduct() {
        System.out.println("Product SKU Code: " + prodSKU);
        System.out.println("Product Name    : " + prodName);
    }

    public static ArrayList<Product> readMasterProductFile() {
        String pathName = "product.txt";

        File file = new File(pathName);
        ArrayList<Product> productList = new ArrayList<>();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 2) { // Ensure the line has the expected number of fields
                    String prodName = data[0];
                    String prodSKU = data[1];

                    Product product = new Product(prodName, prodSKU);
                    productList.add(product);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error (read master product file):" + e.getMessage());
        }
        return productList;
    }

    public static void writeMasterProductFile(ArrayList<Product> prodList) {
        String pathName = "product.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Product prod : prodList) {
                writer.write(prod.getProdName());
                writer.write('|');
                writer.write(prod.getProdSKU());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error (write master product file):" + e.getMessage());
        }
    }

    public static void prodNameRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                    The Product Name Should Be:                        ");
        System.out.println(" 1. Check if the name is correct before press 'Enter'                  ");
        System.out.println(" 2. Avoid containing special character [@,#,$,...], especially '|'     ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");

    }

    public static void prodSKURules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                      The Product SKU Should Be:                       ");
        System.out.println(" 1. Start With Brand Name (Short Form), 2 characters                   ");
        System.out.println(" 2. Followed By Type of the product (Short Form), 3 characters         ");
        System.out.println(" 3. Followed By 3 digits                                               ");
        System.out.println(" 4. Total Length of Product SKU is 8                                   ");
        System.out.println(" 5. In Format [XXXXXxxx]                                               ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");

    }

}
