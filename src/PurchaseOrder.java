import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

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
        this.poNo = String.format("PO%03d", countPo() + 1);
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
        int year, month, day;

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
                System.out.println("Successfully Add Product into Purchase Order");
            }else{
                System.out.println("Failed to Add Product into Purchase Order");
            }
        }

        //get date
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        year  = localDate.getYear();
        month = localDate.getMonthValue();
        day   = localDate.getDayOfMonth();

        PurchaseOrder newPO = new PurchaseOrder(supId,day,month,year,purchaseProd,numOfPurchaseProd);
        // display new purchase order information
        // double confirm from the user

        System.out.println(" ========================================================= ");
        System.out.println("|          Confirmation for New Purchase Order           |");
        System.out.println(" ========================================================= \n");

        newPO.displayPO();

        System.out.print("\nDo you sure the warehouse information is correct? [Y = YES || Others = NO]: ");
        boolean confirmation = sc.next().trim().equalsIgnoreCase("Y");
        sc.nextLine();

        if (confirmation) {
            // if user confirm then process below
            ArrayList<PurchaseOrder> poList = readPurchaseOrderFile();
            poList.add(newPO);
            writePurchaseOrderFile(poList);

            System.out.println("Successfully Send the Purchase Order!");

        }else{
            System.out.println("Failed to Send the Purchase Order");
        }

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void viewPOhistory() {

        Scanner sc = new Scanner(System.in);
        String poID;
        boolean validID = false;
        PurchaseOrder viewPo = null;
        ArrayList<PurchaseOrder> poList = readPurchaseOrderFile();

        Main.displayHeader();
        System.out.println("|               View Purchase Order History               |");
        System.out.println(" ========================================================= ");

        do {
            System.out.print("Enter the Purchase Order's Number that you wish to view [PO001]: ");
            poID = sc.nextLine().trim().toUpperCase();
            if (poID.equals("-1")) {
                return;
            }
            poList = readPurchaseOrderFile();
            for (PurchaseOrder po : poList) {
                if (poID.equals(po.getPoNo())) {
                    validID = true;
                    viewPo = po;
                    break;
                }
            }

            if (validID) {
                // display information for purchase order
                System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.printf("                 Information for Purchase Order %6s\n\n", viewPo.getPoNo());

                // display po


                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            }


        } while (!validID);

        System.out.println("Successfully Display the Purchase Order Details!!!");
        System.out.print("Press Enter to Continue...");
        new Scanner(System.in).nextLine();

    }

    public void displayPO() {
        System.out.println("Purchase Order Number: "+poNo);
        System.out.println("Date: "+day+"/"+month+"/"+year);
        System.out.println("Order From: "+supID);
        System.out.println("Number of Product:"+numOfPurchaseProd);
        for(WhProd prod : purchaseProd){
            System.out.println("Product SKU: "+prod.getProductSKU());
            // display sku name quantity
        }

    }

     private int countPo() {
         ArrayList<PurchaseOrder> po = readPurchaseOrderFile();
         return po.size();
     }

    public static ArrayList<PurchaseOrder> readPurchaseOrderFile() {
        String pathName = "purchase_order.txt";

        File file = new File(pathName);
        ArrayList<PurchaseOrder> poList = new ArrayList<>();

        ArrayList<Product> masterProd = Product.readMasterProductFile();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                ArrayList<WhProd> productList = new ArrayList<>();
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length >= 8) { // Ensure the line has the expected number of fields
                    String poNum = data[0];
                    String supId = data[1];
                    int day = Integer.parseInt(data[2]);
                    int month = Integer.parseInt(data[3]);
                    int year = Integer.parseInt(data[4]);
                    int numProduct = Integer.parseInt(data[5]);

                    for (int i = 6; i < data.length; i=i+2) {
                        productList.add(new WhProd(data[i],Integer.parseInt(data[i+1]),null));
                    }

                    //read product name from master product file
                    for (WhProd whP : productList) {
                        for (Product p : masterProd) {
                            if (whP.getProdSKU().equals(p.getProdSKU())) {
                               whP.setProdName(p.getProdName());
                                break;
                            }
                        }
                    }

                    PurchaseOrder newPO = new PurchaseOrder(supId,day,month,year,productList,numProduct);
                    poList.add(newPO);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error (read purchase file):" + e.getMessage());
        }
        return poList;
    }

    public static void writePurchaseOrderFile(ArrayList<PurchaseOrder> poList) {
        String pathName = "purchase_order.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (PurchaseOrder po : poList) {
                writer.write(po.getPoNo());
                writer.write('|');
                writer.write(po.getSupID());
                writer.write('|');
                writer.write(Integer.toString(po.getDay()));
                writer.write('|');
                writer.write(Integer.toString(po.getMonth()));
                writer.write('|');
                writer.write(Integer.toString(po.getYear()));
                writer.write('|');
                writer.write(Integer.toString(po.getNumOfPurchaseProd()));
                writer.write('|');

                for (int i = 0; i < po.getPurchaseProd().size() - 1; i++) {
                    writer.write(po.getPurchaseProd().get(i).getProdSKU());
                    writer.write('|');
                    writer.write(Integer.toString(po.getPurchaseProd().get(i).getQuantity()));
                    writer.write('|');
                }
                writer.write(po.getPurchaseProd().get(po.getPurchaseProd().size() - 1).getProdSKU());
                writer.write('|');
                writer.write(Integer.toString(po.getPurchaseProd().get(po.getPurchaseProd().size() - 1).getQuantity()));

                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error (write purchase order file):" + e.getMessage());
        }
    }

}
