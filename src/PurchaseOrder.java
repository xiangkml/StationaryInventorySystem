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
    public PurchaseOrder(String poNo,String supID, int day, int month, int year, ArrayList<WhProd> purchaseProd, int numOfPurchaseProd) {
        this.supID = supID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.purchaseProd = purchaseProd;
        this.numOfPurchaseProd = numOfPurchaseProd;
        this.poNo = poNo;
    }

    public String getPoNo() {
        return poNo;
    }

    public String getSupID() {
        return supID;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<WhProd> getPurchaseProd() {
        return purchaseProd;
    }

    public int getNumOfPurchaseProd() {
        return numOfPurchaseProd;
    }

    public static void addPurchaseOrder() {

        Scanner sc = new Scanner(System.in);
        String supId;
        boolean validNum = false,validSup = false;
        int numOfPurchaseProd = 0;
        ArrayList<WhProd> purchaseProd = new ArrayList<>();
        ArrayList<Supplier> suppliers = Supplier.readSupplierFile();
        WhProd orderProd = null;
        int year, month, day;

        Main.displayHeader();
        System.out.println("|                   Add Purchase Order                    |");
        System.out.println(" ========================================================= \n");

        do{
            Supplier.supIdRules();
            System.out.print("Enter Supplier's ID that you wish to order from [SUP001]: ");
            supId = sc.nextLine().trim().toUpperCase();
            if (supId.equals("-1")) {
                return;
            }

            if(ExtraFunction.checkPattern(supId,"^SUP\\d{3}$")){
                for(Supplier s : suppliers){
                    if(supId.equals(s.getId())){
                        validSup = true;
                        break;
                    }
                }
                if(!validSup){
                    System.out.println("\n* Don,t have this Supplier!! *");
                    System.out.println("* Please Enter Again! *\n");

                }
            }else{
                System.out.println("\n* Invalid Supplier ID!! *");
                System.out.println("* Please Enter Valid Supplier ID! *");
            }

        }while(!validSup);

        do {
            try {
                Supplier.inputIntRules();
                System.out.print("How many Product(s) you want to order: ");
                numOfPurchaseProd = sc.nextInt();
                sc.nextLine();
                if (numOfPurchaseProd == -1) {
                    return;
                }

                if (numOfPurchaseProd < 1) {
                    System.out.println("\n* Number of Product Must At Least 1!! *");
                    System.out.println("* Please Enter Again! *\n");
                } else {
                    validNum = true;
                }

            } catch (Exception e) {
                System.out.println("\n* Invalid Input!! *");
                System.out.println("* Can Only Enter Integer! *\n");
                sc.nextLine();
            }
        } while (!validNum);

        for (int i = 0; (i < numOfPurchaseProd); i++) {

            boolean validProd = false, validQtt = false;
            String skuCode;
            int quantity;

            do {
                Product.prodSKURules();
                System.out.print("Enter Product " + (i + 1) + " SKU Code [OLBOK007]: ");
                skuCode = sc.nextLine().trim().toUpperCase();
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
                        break;
                    }

                }

                if (!validProd) {
                    System.out.println("\n* Invalid Product SKU Code!! *");
                    System.out.println("* Please register new product for the supplier before order! *\n");
                }

            } while (!validProd);

            do {

                try {
                    System.out.println("\n#   For '" + orderProd.getProductSKU() + "'   #");
                    Supplier.inputIntRules();
                    System.out.print("Enter Quantity that you want to order: ");
                    quantity = sc.nextInt();
                    sc.nextLine();

                    if (quantity == -1) {
                        return;
                    }

                    if (quantity < 1) {
                        System.out.println("\n* Product Quantity Must At Least 1!! *");
                        System.out.println("* Please Enter Again! *\n");
                    } else {
                        validQtt = true;
                        orderProd.setQuantity(quantity);
                    }

                } catch (Exception e) {
                    System.out.println("\n* Invalid Input!! *");
                    System.out.println("* Can Only Enter Integer! *\n");
                    sc.nextLine();
                }

            } while (!validQtt);

            // display the product that need to order
            System.out.println("Product " + (i + 1) + ":");
            System.out.println("Product SKU: " + orderProd.getProductSKU());
            System.out.println("Product Name: " + orderProd.getProdName());
            System.out.println("Product Quantity: " + orderProd.getQuantity());
            System.out.println(" ");

            // double confirm from user
            System.out.print("Do you sure you want to order this product from the supplier '" + supId + "'? [ Y = Yes || Others = No ]: ");
            boolean confirm = sc.nextLine().toUpperCase().trim().equals("Y");

            if (confirm) {
                purchaseProd.add(orderProd);
                System.out.println("\nSuccessfully Add Product into Purchase Order!!");
            }else{
                numOfPurchaseProd--;
                System.out.println("\n* Failed to Add Product into Purchase Order! *");
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

        System.out.println("\n ========================================================= ");
        System.out.println("|          Confirmation for New Purchase Order           |");
        System.out.println(" ========================================================= \n");

        newPO.displayPO();

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.print("\nDo you sure the Purchase Order Information is correct? [Y = YES || Others = NO]: ");
        boolean confirmation = sc.next().trim().equalsIgnoreCase("Y");
        sc.nextLine();

        if (confirmation) {
            // if user confirm then process below
            ArrayList<PurchaseOrder> poList = readPurchaseOrderFile();
            poList.add(newPO);
            writePurchaseOrderFile(poList);

            System.out.println("\n\nSuccessfully Send the Purchase Order!!!");

        }else{
            System.out.println("\n* Failed to Send the Purchase Order!! *");
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
            poNoRules();
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
                System.out.printf("                 Information for Purchase Order '%5s'\n\n", viewPo.getPoNo());
                // display po
                viewPo.displayPO();
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            }
            else {
                System.out.println("\n* Invalid Purchase Order Number!! *");
                System.out.println("* Please Re-enter a Valid Purchase Order Number! *");
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
        System.out.print("Product SKU: ");
        for(WhProd prod : purchaseProd){
            // display sku name quantity
            System.out.printf("%s , %-25s , %d\n%13s", prod.getProductSKU(),prod.getProdName(),prod.getQuantity(), "");
        }
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b");
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

                    PurchaseOrder newPO = new PurchaseOrder(poNum,supId,day,month,year,productList,numProduct);
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

    public static void poNoRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                 The Purchase Order Number Should Be:                  ");
        System.out.println(" 1. Start With 'PO'                                                   ");
        System.out.println(" 2. Followed by 3 Digits                                               ");
        System.out.println(" 3. Total Length of Supplier ID is 5                                   ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");

    }

}
