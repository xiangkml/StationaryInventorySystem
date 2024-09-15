import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class WhProd extends Product {

    private int quantity, reorderLv;
    private String whId;

    public WhProd(String whID, String prodSKU, int quantity, int reorderLv) {
        super(prodSKU);
        this.whId = whID;
        this.quantity = quantity;
        this.reorderLv = reorderLv;
    }

    public WhProd(){}

    public WhProd(String whID, String prodSKU) {
        super(prodSKU);
        this.whId = whID;
        this.quantity = 0;
        this.reorderLv = 20;

        // default value for quantity is 0 and reorderLevel is 20
        // use when add a new product to master file and all warehouse will automatically add the product
    }

    public WhProd(String prodSKU, int quantity,String prodName) {
        super(prodName,prodSKU);
        this.quantity = quantity;
        // use for purchase order , only need sku quantity and name
       }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLv() {
        return reorderLv;
    }

    public void setReorderLv(int reorderLv) {
        this.reorderLv = reorderLv;
    }

    public String getWhId() {
        return whId;
    }

    public String getProductSKU() {
        return super.getProdSKU();
    }

    public static void goodsReceive() {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Supplier> supplyList = Supplier.readSupplierFile();
        ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();
        String supID;
        WhProd goodsBefore = null;
        Supplier sup = null;
        int numProd = 0, total = 0;
        boolean validSup = false, validNum = false;

        System.out.println("----------- Goods Receive -----------");
        System.out.println("Enter '-1' to exit");

        do {
            System.out.println("Enter the supplier's id [SUP001]: ");
            supID = scanner.nextLine().toUpperCase().trim();
            if (supID.equals("-1")) {
                return;
            }

            for (Supplier supply : supplyList) {
                if (supID.equals(supply.getId())) {
                    validSup = true;
                    sup = supply;
                }
            }
        } while (!validSup);

        do {
            try {
                System.out.println("Enter the number of product types [Integer]: ");
                numProd = scanner.nextInt();
                if (numProd == -1) {
                    return;
                }
                validNum = true;
            } catch (Exception e) {
                System.out.println("Enter Integer Only.");
                System.out.println("Please re-enter again!");
            }
        } while (!validNum);


        for (int i = 1; i <= numProd; i++) {

            String skuCode;
            int qtt = 0;
            boolean validProd = false, validQtt = false, confirmation = false;

            do {
                System.out.println("Enter product [" + i + "] SKU code [PBRUL001]: ");
                skuCode = scanner.nextLine().toUpperCase().trim();
                if (skuCode.equals("-1")) {
                    return;
                }
                for (Product prod : sup.getSupplyProduct()) {
                    if (prod.getProdSKU().equals(skuCode)) {
                        validProd = true;
                        break;
                    }
                }
                if (!validProd) {
                    System.out.println("Supplier haven't register this product!");
                    System.out.println("Please re-enter again!");
                }
            } while (!validProd);

            do {
                try {
                    System.out.println("Enter the quantity receive [Integer]: ");
                    qtt = scanner.nextInt();
                    if (qtt == -1) {
                        return;
                    }
                    validQtt = true;
                } catch (Exception e) {
                    System.out.println("Enter Integer Only.");
                    System.out.println("Please re-enter again!");
                }

            } while (!validQtt);

            for (WhProd whProd : whProdList) {
                if ((whProd.getWhId().equals("KUL001")) && (whProd.getProductSKU().equals(skuCode))) {
                    goodsBefore = whProd;
                    total = qtt + goodsBefore.getQuantity();
                }
                break;
            }

            WhProd goodsReceive = new WhProd("KUL001", skuCode, total, 20);
            System.out.println("----------- Confirmation -----------");
            goodsReceive.displayWhProd();
            System.out.println("Do you sure the product information is correct? [Y = YES || Others = NO]");
            confirmation = scanner.next().trim().equalsIgnoreCase("Y");

            if (confirmation) {
                whProdList.add(goodsReceive);
                whProdList.remove(goodsBefore);
                WhProd.writeWarehouseProductFile(whProdList);
                System.out.println("Successfully update product quantity in database.");
            } else {
                System.out.println("Failed to receives goods");
            }

            System.out.println("Press Enter to continue...");
            scanner.nextLine();

        }
        System.out.println("Successfully receives goods!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void goodsReturn() {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Supplier> supplyList = Supplier.readSupplierFile();
        ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();
        String supID;
        WhProd goodsBefore = null;
        Supplier sup = null;
        int numProd = 0;
        boolean validSup = false, validNum = false;

        System.out.println("----------- Goods Return -----------");
        System.out.println("Enter '-1' to exit");

        do {
            System.out.println("Enter the supplier's id that you wish to return [SUP001]: ");
            supID = scanner.nextLine().toUpperCase().trim();
            if (supID.equals("-1")) {
                return;
            }

            for (Supplier supply : supplyList) {
                if (supID.equals(supply.getId())) {
                    validSup = true;
                    sup = supply;
                }
            }
        } while (!validSup);

        do {
            try {
                System.out.println("Enter the number of product types that you wish to return [Integer]: ");
                numProd = scanner.nextInt();
                if (numProd == -1) {
                    return;
                }
                validNum = true;
            } catch (Exception e) {
                System.out.println("Enter Integer Only.");
                System.out.println("Please re-enter again!");
            }
        } while (!validNum);


        for (int i = 1; i <= numProd; i++) {

            String skuCode;
            int qtt = 0, total = 0;
            boolean validProd = false, validQtt = false, confirmation = false;

            do {
                System.out.println("Enter product [" + i + "] SKU code [PBRUL001]: ");
                skuCode = scanner.nextLine().toUpperCase().trim();
                if (skuCode.equals("-1")) {
                    return;
                }
                for (Product prod : sup.getSupplyProduct()) {
                    if (prod.getProdSKU().equals(skuCode)) {
                        validProd = true;
                        break;
                    }
                }
                if (!validProd) {
                    System.out.println("Do not have this product! What do yo want to return?");
                    System.out.println("Please re-enter again!");
                }
            } while (!validProd);

            do {
                try {
                    System.out.println("Enter the quantity you want to return [Integer]: ");
                    qtt = scanner.nextInt();
                    if (qtt == -1) {
                        return;
                    }
                    validQtt = true;
                    for (WhProd whProd : whProdList) {
                        if ((whProd.getWhId().equals("KUL001")) && (whProd.getProductSKU().equals(skuCode))) {
                            goodsBefore = whProd;
                            total = goodsBefore.getQuantity() - qtt;
                        }
                        break;
                    }
                    if (total < 0) {
                        System.out.println("You don't have enough products to be returned!");
                        System.out.println("Please re-enter again");
                    }

                } catch (Exception e) {
                    System.out.println("Enter Integer Only.");
                    System.out.println("Please re-enter again!");
                }

            } while (!validQtt || total < 0);

            WhProd goodsReceive = new WhProd("KUL001", skuCode, total, 20);
            System.out.println("----------- Confirmation -----------");
            goodsReceive.displayWhProd();
            System.out.println("Do you sure the product information is correct? [Y = YES || Others = NO]");
            confirmation = scanner.next().trim().equalsIgnoreCase("Y");

            if (confirmation) {
                whProdList.add(goodsReceive);
                whProdList.remove(goodsBefore);
                WhProd.writeWarehouseProductFile(whProdList);
                System.out.println("Successfully update product quantity in database.");
            } else {
                System.out.println("Failed to return goods");
            }

            System.out.println("Press Enter to continue...");
            scanner.nextLine();

        }
        System.out.println("Successfully return goods!");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void stockTransfer() {

        Scanner sc = new Scanner(System.in);
        String warehouse, sku;
        int qtt = 0;
        boolean validWh = false, validSKU = false, validQtt = false, confirmation = false;
        WhProd source = null, destination = null;
        ArrayList<Warehouse> whList;
        ArrayList<WhProd> whProdList = readWarehouseProductFile();

        // Polymorphism
        Product prodPoly = new WhProd();

        System.out.println("----------- Stock Transfer ----------");
        System.out.println("Enter '-1' to exit");

        do {
            System.out.println("Enter the warehouse id that you wish to transfer the stock [KLG002]:");
            warehouse = sc.nextLine().toUpperCase().trim();
            if (warehouse.equals("-1")) {
                return;
            }

            whList = Warehouse.readMasterWarehouseFile();
            for (Warehouse wh : whList) {
                if (wh.getWhID().equals(warehouse)) {
                    validWh = true;
                    break;
                }
            }

            if (!validWh) {
                System.out.println("Do not have this warehouse to be transferred!");
                System.out.println("Please re-enter again");
            }
        } while (!validWh);

        do {
            System.out.println("Enter the product sku code that you wish to transfer [PBRUL001]: ");
            sku = sc.nextLine().toUpperCase().trim();
            if (sku.equals("-1")) {
                return;
            }

            ArrayList<Product> prodList = Product.readMasterProductFile();
            for (Product prod : prodList) {
                if (prod.getProdSKU().equals(sku)) {
                    validSKU = true;
                    break;
                }
            }
            if (!validSKU) {
                System.out.println("Do not have this product to be transferred!");
                System.out.println("Please re-enter again");
            }
        } while (!validSKU);

        do {
            try {
                System.out.println("Enter the quantity of product that you wish to transfer [Integer]: ");
                qtt = sc.nextInt();
                if (qtt == -1) {
                    return;
                }
                for (WhProd whProd : whProdList) {
                    if (whProd.getProductSKU().equals(sku)) {
                        if (whProd.getWhId().equals("KUL001")) {
                            source = whProd;
                        }

                        if (whProd.getWhId().equals(warehouse)) {
                            destination = whProd;
                        }
                    }
                }
                if (source.getQuantity() < qtt) {
                    System.out.println("Do not have enough product to be transferred!");
                    System.out.println("Please re-enter again!");
                } else {
                    validQtt = true;
                }
            } catch (Exception e) {
                System.out.println("Enter Integer Only.");
                System.out.println("Please re-enter again!");
            }
        } while (!validQtt);

        System.out.println("----------- Confirmation -----------");
        System.out.println("From :");
        source.displayWhProd();
        System.out.println("To :");
        destination.displayWhProd();
        System.out.println("Transfer Quantity: " + qtt);

        System.out.println("Do you sure the transfer information is correct? [Y = YES || Others = NO]");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");

        if (confirmation) {
            whProdList.remove(source);
            whProdList.remove(destination);
            source.setQuantity(source.getQuantity() - qtt);
            destination.setQuantity(destination.getQuantity() + qtt);
            whProdList.add(source);
            whProdList.add(destination);
            writeWarehouseProductFile(whProdList);
            System.out.println("Successfully perform stock transfer.");
        } else {
            System.out.println("Failed to perform stock transfer.");
        }

        System.out.println("Press Enter to continue...");
        sc.nextLine();

    }

    public static void stockReturn() {

        Scanner sc = new Scanner(System.in);
        String warehouse, sku;
        int qtt = 0;
        boolean validWh = false, validSKU = false, validQtt = false, confirmation = false;
        WhProd source = null, destination = null;
        ArrayList<Warehouse> whList;
        ArrayList<WhProd> whProdList = readWarehouseProductFile();

        System.out.println("----------- Stock Return ----------");
        System.out.println("Enter '-1' to exit");

        do {
            System.out.println("Enter the warehouse id that you need to return the stock to main warehouse [KLG002]:");
            warehouse = sc.nextLine().toUpperCase().trim();
            if (warehouse.equals("-1")) {
                return;
            }

            whList = Warehouse.readMasterWarehouseFile();
            for (Warehouse wh : whList) {
                if (wh.getWhID().equals(warehouse)) {
                    validWh = true;
                    break;
                }
            }

            if (!validWh) {
                System.out.println("Do not have this warehouse!");
                System.out.println("Please re-enter again");
            }
        } while (!validWh);

        do {
            System.out.println("Enter the product sku code that you wish to return [PBRUL001]: ");
            sku = sc.nextLine().toUpperCase().trim();
            if (sku.equals("-1")) {
                return;
            }

            ArrayList<Product> prodList = Product.readMasterProductFile();
            for (Product prod : prodList) {
                if (prod.getProdSKU().equals(sku)) {
                    validSKU = true;
                    break;
                }
            }
            if (!validSKU) {
                System.out.println("Do not have this product !");
                System.out.println("Please re-enter again");
            }
        } while (!validSKU);

        do {
            try {
                System.out.println("Enter the quantity of product that you wish to return [Integer]: ");
                qtt = sc.nextInt();
                if (qtt == -1) {
                    return;
                }
                for (WhProd whProd : whProdList) {
                    if (whProd.getProductSKU().equals(sku)) {
                        if (whProd.getWhId().equals("KUL001")) {
                            destination = whProd;
                        }

                        if (whProd.getWhId().equals(warehouse)) {
                            source = whProd;
                        }
                    }
                }
                if (source.getQuantity() < qtt) {
                    System.out.println("Do not have enough product to be transferred!");
                    System.out.println("Please re-enter again!");
                } else {
                    validQtt = true;
                }
            } catch (Exception e) {
                System.out.println("Enter Integer Only.");
                System.out.println("Please re-enter again!");
            }
        } while (!validQtt);

        System.out.println("----------- Confirmation -----------");
        System.out.println("From :");
        source.displayWhProd();
        System.out.println("To :");
        destination.displayWhProd();
        System.out.println("Transfer Quantity: " + qtt);

        System.out.println("Do you sure the return information is correct? [Y = YES || Others = NO]");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");

        if (confirmation) {
            whProdList.remove(source);
            whProdList.remove(destination);
            source.setQuantity(source.getQuantity() - qtt);
            destination.setQuantity(destination.getQuantity() + qtt);
            whProdList.add(source);
            whProdList.add(destination);
            writeWarehouseProductFile(whProdList);
            System.out.println("Successfully perform stock return.");
        } else {
            System.out.println("Failed to perform stock return.");
        }

        System.out.println("Press Enter to continue...");
        sc.nextLine();

    }

    public static void resetReorderLv() {

        Scanner sc = new Scanner(System.in);
        ArrayList<WhProd> whProdList = readWarehouseProductFile();
        ArrayList<WhProd> productList = new ArrayList<>();
        ArrayList<Warehouse> warehouseList = Warehouse.readMasterWarehouseFile();
        boolean validWh = false,validLv = false;
        String whId;

        System.out.println("----------- Reset Reorder Level -----------");
        System.out.println("Enter '-1' to exit");

        do {
            System.out.println("Enter warehouse id:");
            whId = sc.nextLine().toUpperCase().trim();
            if(whId.equals("-1")){
                return;
            }

            for (Warehouse wh : warehouseList) {
                if (wh.getWhID().equals(whId)) {
                    validWh = true;
                    break;
                }
            }
            if (!validWh) {
                System.out.println("Do not have this warehouse !");
                System.out.println("Please re-enter again");
            }
        } while (!validWh);

        System.out.println("----------- Product List -----------");
        System.out.printf("%5s %8s %-20s %8s %13s","Index","SKU","Name","Quantity","Reorder Level");


        int prodIndex = 0,prodEdit,reorderLv;
        boolean validNum = false;
        for (WhProd whProd : whProdList) {
            if (whProd.getWhId().equals(whId)) {
                prodIndex ++;
                productList.add(whProd);
                System.out.printf("%05d. %8s %-20s %8d %13d",prodIndex,whProd.getProductSKU(),whProd.getProdName(),whProd.getQuantity(),whProd.getReorderLv());

            }
        }

        System.out.println("Which product you want to reset it reorder level?");
        System.out.println("[Reorder Level must at least 5]");
        prodEdit = ExtraFunction.menuInput(prodIndex);

        do{
            System.out.println("Set new reorder level: ");
            reorderLv = sc.nextInt();
            try{
                if(reorderLv ==-1){
                    return;
                }
                if(reorderLv >= 5 ){
                    productList.get(prodEdit-1).setReorderLv(reorderLv);
                    System.out.println("Successfully updated reorder level");
                    validLv = true;
                }
                else{
                    System.out.println("Failed to update reorder level");
                    System.out.println("The reorder level must at least 5");
                    System.out.println("Please re-enter again");
                }
            }catch(Exception e){
                System.out.println("Enter Integer Only.");
                System.out.println("Please re-enter again");
            }

        }while(!validLv);

        System.out.println("Press Enter to continue...");
        sc.nextLine();

    }

    public void displayWhProd() {
        System.out.println("Warehouse ID: " + whId);
        System.out.println("Product SKU Code: " + prodSKU);
        System.out.println("Product Name: " + prodName);
        System.out.println("Quantity: " + quantity);
    }

    public static ArrayList<WhProd> readWarehouseProductFile() {
        String pathName = "warehouse_product.txt";

        File file = new File(pathName);
        ArrayList<WhProd> prodList = new ArrayList<>();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 4) { // Ensure the line has the expected number of fields
                    String whID = data[0];
                    String sku = data[1];
                    int qtt = Integer.parseInt(data[2]);
                    int reorderLv = Integer.parseInt(data[3]);

                    WhProd product = new WhProd(whID, sku, qtt, reorderLv);
                    prodList.add(product);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return prodList;
    }

    public static void writeWarehouseProductFile(ArrayList<WhProd> prodList) {
        String pathName = "warehouse_product.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (WhProd prod : prodList) {
                writer.write(prod.getWhId());
                writer.write('|');
                writer.write(prod.getProductSKU());
                writer.write('|');
                writer.write(prod.getQuantity());
                writer.write('|');
                writer.write(prod.getReorderLv());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

}
// warehouseID , name , address
// ProductList {sku,name,quantity,reorder level}