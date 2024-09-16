import java.util.ArrayList;
import java.util.Scanner;

public class PurchaseOrder {

    private String poNo, supID;
    private int day, month, year;
    ArrayList<WhProd> purchaseProd;
    private int numOfPurchaseProd;

    public PurchaseOrder(String supID, int day, int month, int year, ArrayList<WhProd> purchaseProd, int numOfPurchaseProd) {
        this.supID = supID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.purchaseProd = purchaseProd;
        this.numOfPurchaseProd = numOfPurchaseProd;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getSupID() {
        return supID;
    }

    public void setSupID(String supID) {
        this.supID = supID;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<WhProd> getPurchaseProd() {
        return purchaseProd;
    }

    public void setPurchaseProd(ArrayList<WhProd> purchaseProd) {
        this.purchaseProd = purchaseProd;
    }

    public int getNumOfPurchaseProd() {
        return numOfPurchaseProd;
    }

    public void setNumOfPurchaseProd(int numOfPurchaseProd) {
        this.numOfPurchaseProd = numOfPurchaseProd;
    }

    public static void addPurchaseOrder() {

        Scanner sc = new Scanner(System.in);
        String supId;
        boolean validNum = false;
        int numOfPurchaseProd = 0;
        ArrayList<WhProd> purchaseProd = new ArrayList<>();
        WhProd orderProd = null;

        //header and rules, -1 use to exit

        System.out.println("Enter supplier's Id that you wish to order from: [SUP001]");
        supId = sc.nextLine();
        if (supId.equals("-1")) {
            return;
        }

        do {
            try {
                System.out.println("How many product you want to order : [Integer]");
                numOfPurchaseProd = sc.nextInt();
                sc.nextLine();
                if (numOfPurchaseProd == -1) {
                    return;
                }

                if (numOfPurchaseProd < 1) {
                    System.out.println("Number of Product must at least 1!");
                    System.out.println("Please enter again");
                } else {
                    validNum = true;
                }

            } catch (Exception e) {
                System.out.println("Invalid Input.");
                System.out.println("Only enter integer!");
                sc.nextLine();
            }
        } while (!validNum);

        for (int i = 0; (i < numOfPurchaseProd); i++) {

            boolean validProd = false, validQtt = false;
            String skuCode;
            int quantity;

            do {
                System.out.println("Enter product " + (i + 1) + " SKU code [OLBOK007]:");
                skuCode = sc.nextLine().trim();
                if (skuCode.equals("-1")) {
                    return;
                }

                ArrayList<Supplier> supplierList = Supplier.readSupplierFile();
                for (Supplier supplier : supplierList) {
                    if (supplier.getId().equals(supId)) {
                        ArrayList<Product> supplierProd = supplier.getSupplyProduct();
                        for (Product product : supplierProd) {
                            if (product.getProdSKU().equals(skuCode)) {
                                validProd = true;
                                orderProd = new WhProd(skuCode, 0, product.getProdName());
                                break;
                            }
                        }
                    }

                }

                if (!validProd) {
                    System.out.println("Invalid product SKU code");
                    System.out.println("Please register new product for the supplier before order");
                }

            } while (!validProd);

            do {

                try {
                    System.out.println("For [ " + orderProd.getProductSKU() + " ]");
                    System.out.println("Enter quantity that you want to order: ");
                    quantity = sc.nextInt();
                    sc.nextLine();

                    if (quantity == -1) {
                        return;
                    }

                    if (quantity < 1) {
                        System.out.println("Product Quantity must at least 1!");
                        System.out.println("Please enter again");
                    } else {
                        validQtt = true;
                        orderProd.setQuantity(quantity);
                    }

                } catch (Exception e) {
                    System.out.println("Invalid Input.");
                    System.out.println("Only enter integer!");
                    sc.nextLine();
                }

            } while (!validQtt);

            // display the product that need to order
            System.out.println("Product " + (i + 1) + ":");
            System.out.println("SKU: " + orderProd.getProductSKU());
            System.out.println("Name: " + orderProd.getProdName());
            System.out.println("Quantity: " + orderProd.getQuantity());
            System.out.println(" ");
            // double confirm from user
            System.out.println("Do you sure you want to order this product from the supplier [ " + supId + " ] ?");
            System.out.println("[ Y = Yes || Others = No ]");
            boolean confirm = sc.nextLine().toUpperCase().trim().equals("Y");

            if (confirm) {
                purchaseProd.add(orderProd);
            }
        }

//            Supplier newSupplier = new Supplier(supName, email, address, tel, supplyProduct);
//
//            // display new supplier info
//            // double confirm from the user
//            System.out.println(" ========================================================= ");
//            System.out.println("|        Confirmation for New Supplier Information        |");
//            System.out.println(" ========================================================= \n");
//
//            newSupplier.displaySup();
//
//            // if user confirm then process below
//            ArrayList<Supplier> supplierList = readSupplierFile();
//            supplierList.add(newSupplier);
//            writeSupplierFile(supplierList);
//
//            System.out.println("Successfully registered a new supplier!");
//            System.out.print("Press Enter to Continue...");
//            sc.nextLine();



    }

    public static void displayPO() {
        System.out.println("PO No");
    }

    // private int countPo() {
    //     ArrayList<Staff> stf = readPurchaseOrderFile();
    //     return stf.size();
    // }
}
