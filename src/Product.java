import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {

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

    public void addProduct() {
        String prodName, prodSKU;
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Add Product -----------");
        System.out.println("Enter '-1' to exit");

        System.out.println("Enter product name [555 Notebook]: ");
        prodName = sc.nextLine().trim();
        if (prodName.equals("-1")) {
            return;
        }

        boolean validInput = true;
        do {
            System.out.println("Enter product sku code [PLPEN001]: ");
            prodSKU = sc.nextLine().trim().toUpperCase();
            if (prodSKU.equals("-1")) {
                return;
            }

            if (!ExtraFunction.checkPattern(prodSKU, "^^[A-Z]{5}\\d{3}$")) {
                System.out.println("Enter valid product sku code");
                validInput = false;
            }
        } while (!validInput);

        Product newProduct = new Product(prodName, prodSKU);

        // display new product info
        // double confirm from the user

        // if user confirm then process below
        ArrayList<Product> masterProduct = readMasterProductFile();
        masterProduct.add(newProduct);
        writeMasterProductFile(masterProduct);

        ArrayList<Warehouse> whList = Warehouse.readMasterWarehouseFile();
        ArrayList<WhProd> whProduct = WhProd.readWarehouseProductFile();
        for (Warehouse w : whList) {
            WhProd newWhProd = new WhProd(w.getWhID(), prodSKU, 0, 20);
            whProduct.add(newWhProd);
        }

        System.out.println("Successfully add a new product!");
        System.out.println("Press Enter to continue...");
        sc.nextLine();

    }

    public void editProduct() {

        Scanner sc = new Scanner(System.in);
        String prodID;
        int menuInput;
        boolean validID = false, confirmationBefore = false, confirmationAfter = false, continueEdit = false;
        Product editProduct = null;
        ArrayList<Product> prodList = null;

        System.out.println("----------- Edit Product -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the product's id that you wish to edit [PLPEN001]: ");
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
                System.out.println("Invalid product sku code!");
                System.out.println("Please re-enter product sku code");
            }

        } while (!validID);

        // information before edit
        System.out.println("Product Information Before Edited: ");
        editProduct.displayProduct();
        System.out.println("Do you sure you want to edit this product information? [Y = YES || Others = NO]");
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
                        //edit id
                        editProduct.editSKU();
                        break;

                    default:
                        // return
                        return;
                }

                System.out.println("Do you want to continue edit this Warehouse information? [Y = YES || Others = NO]");
                continueEdit = sc.nextLine().toUpperCase().trim().equals("Y");
            } while (continueEdit);

            // information after edit
            System.out.println("Product Information After Edited: ");
            editProduct.displayProduct();
            System.out.println("Do you sure you want to save this product information? [Y = YES || Others = NO]");
            confirmationAfter = sc.nextLine().toUpperCase().trim().equals("Y");

            if (confirmationAfter) {
                prodList.add(editProduct);
                writeMasterProductFile(prodList);

                if (menuInput == 2) {
                    ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();

                    for (WhProd w : whProdList) {
                        if (prodID.equals(w.getProdSKU())) {
                            w.setProdSKU(editProduct.getProdSKU());
                        }
                    }
                }

                System.out.println("Successfully updated the latest product information");
            }
        }

        System.out.println("Press Enter to continue...");
        sc.nextLine();
    }

    public void editName() {

        System.out.println("Enter a new name for product [" + prodSKU + "]: ");
        this.prodName = new Scanner(System.in).nextLine().trim();

    }

    public void editSKU() {

        Scanner sc = new Scanner(System.in);
        boolean validID = true;
        do {
            System.out.println("Enter a new id for product [" + prodSKU + "]: ");
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
                    this.prodSKU = newID;
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

    public void deleteProduct() {

        String prodSKU;
        boolean validID = false;
        Product prodDel = new Product();
        ArrayList<Product> prodList;
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Delete Product -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the product's SKU that you wish to delete [OPPEN001]: ");
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
                System.out.println("Invalid product SKU Code");
                System.out.println("Please re-enter a valid product SKU Code");
            } else {
                // display product information that need to be deleted
                // double confirm from user

                // if user confirm
                prodList.remove(prodDel);
                writeMasterProductFile(prodList);
                System.out.println("Successfully deleted the product");
                System.out.println("Press Enter to continue...");
                sc.nextLine();
            }

        } while ((!validID));

    }

    public void viewProduct() {
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

    public void viewAllProduct() {
        ArrayList<Product> prodList = readMasterProductFile();

        System.out.println("--------------------------------------- Product List ---------------------------------------");

        for (Product prod : prodList) {
            // display all information for each product
            System.out.println(prod.getProdSKU() + " " + prod.getProdName());
        }

        System.out.println("Successfully display all the product detail");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    public void viewOneProduct() {

        Scanner sc = new Scanner(System.in);
        String prodID;
        boolean validID = false;
        ArrayList<Product> prodList;

        System.out.println("----------- View Specific Product Details -----------");
        System.out.println("Enter '-1' to exit");
        if (sc.nextLine().equals("-1")) {
            return;
        }

        do {
            System.out.println("Enter the product's SKU that you wish to view [OPPEN001]: ");
            prodID = sc.nextLine().trim().toUpperCase();
            if (prodID.equals("-1")) {
                return;
            }
            prodList = readMasterProductFile();
            for (Product prod : prodList) {
                if (prodID.equals(prod.getProdSKU())) {
                    validID = true;
                    System.out.println(prod.getProdSKU() + " " + prod.getProdName());
                    break;
                }
            }

            if (!validID) {
                System.out.println("Invalid product SKU Code");
                System.out.println("Please re-enter a valid product SKU Code");
            }

        } while (!validID);

        System.out.println("Successfully display the product details");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();

    }

    public void displayProduct() {
        System.out.println("Product SKU Code: " + prodSKU);
        System.out.println("Product Name: " + prodName);
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
            System.out.println("Error :" + e.getMessage());
        }
        return productList;
    }

    public static void writeMasterProductFile(ArrayList<Product> prodList) {
        String pathName = "product.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Product prod : prodList) {
                writer.write(prod.getProdSKU());
                writer.write('|');
                writer.write(prod.getProdName());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

}
