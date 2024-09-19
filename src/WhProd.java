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

    public WhProd() {
    }

    public WhProd(String whID, String prodSKU) {
        super(prodSKU);
        this.whId = whID;
        this.quantity = 0;
        this.reorderLv = 20;

        // default value for quantity is 0 and reorderLevel is 20
        // use when add a new product to master file and all warehouse will automatically add the product
    }

    public WhProd(String prodSKU, int quantity, String prodName) {
        super(prodName, prodSKU);
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

    public void setWhId(String whId) {
        this.whId = whId;
    }

    public String getProductSKU() {
        return super.getProdSKU();
    }

    public static void goodsReceive() {

        Scanner scanner = new Scanner(System.in);
        ArrayList<WhProd> whProdList = WhProd.readWarehouseProductFile();

        String poNo;
        ArrayList<PurchaseOrder> poList = PurchaseOrder.readPurchaseOrderFile();
        boolean poValid = false;
        PurchaseOrder viewPO = null;

        Main.displayHeader();
        System.out.println("|                      Goods Receive                      |");
        System.out.println(" ========================================================= ");

        do {
            PurchaseOrder.poNoRules();
            System.out.print("Enter the Purchase Order Number [PO001]: ");
            poNo = scanner.nextLine().toUpperCase().trim();
            if (poNo.equals("-1")) {
                return;
            }

            for (PurchaseOrder po : poList) {
                if (poNo.equals(po.getPoNo())) {
                    poValid = true;
                    viewPO = po;
                    break;

                }
            }

            if (!poValid) {
                System.out.println("\n* Invalid Purchase Order Number!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }

        } while (!poValid);

        for (WhProd prod : viewPO.getPurchaseProd()) {

            boolean validQtt = false;

            do {
                try {
                    int receiveQtt;
                    System.out.print("Enter the Received Quantity for '" + prod.getProdSKU() + "' :");
                    receiveQtt = scanner.nextInt();
                    scanner.nextLine();

                    if (receiveQtt == -1) {
                        return;
                    }

                    if (receiveQtt < 0) {
                        System.out.println("\n* Quantity Should Not Less Than 0! *");
                        System.out.println("* Please Re-enter Again! *\n");
                    } else {
                        boolean confirmation;
                        validQtt = true;
                        System.out.println("\n -------------------------------------------------------- ");
                        System.out.println("|               Confirmation Goods Receive               | ");
                        System.out.println(" -------------------------------------------------------- \n");
                        System.out.print("Do you sure you wants to received " + receiveQtt + " product '" + prod.getProdSKU() + "' [ Y = Yes || Others = No ]: ");
                        confirmation = scanner.nextLine().equalsIgnoreCase("Y");

                        if (confirmation) {
                            for (WhProd p : whProdList) {
                                if (p.getProdSKU().equals(prod.getProdSKU())) {
                                    if (p.getWhId().equals("KUL001")) {
                                        p.setQuantity(p.getQuantity() + receiveQtt);
                                        System.out.println("\nSuccessfully Receive the Goods!!");
                                    }
                                    break;
                                }
                            }
                            WhProd.writeWarehouseProductFile(whProdList);
                        } else {
                            System.out.println("\n* Failed to Receive the Goods!! *");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("\n* Invalid Input!! *");
                    System.out.println("* You Can Only Enter Integer! Please Re-enter Again! *\n");
                    scanner.nextLine();
                }
            } while (!validQtt);

        }

        System.out.print("Press Enter to Continue...");
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

        Main.displayHeader();
        System.out.println("|                      Goods Return                       |");
        System.out.println(" ========================================================= ");

        do {
            Supplier.supIdRules();
            System.out.print("Enter the Supplier's ID that you wish to return [SUP001]: ");
            supID = scanner.nextLine().toUpperCase().trim();
            if (supID.equals("-1")) {
                return;
            }

            for (Supplier supply : supplyList) {
                if (supID.equals(supply.getId())) {
                    validSup = true;
                    sup = supply;
                    break;
                }
            }

            if (!validSup) {
                System.out.println("\n* Invalid Supplier ID!! *");
                System.out.println("* We Don't Have This Supplier! Please Re-enter Again! *\n");
            }

        } while (!validSup);

        String skuCode;
        int qtt = 0;
        boolean validProd = false, validQtt = false, confirmation = false;

        do {
            Product.prodSKURules();
            System.out.print("Enter Product SKU Code that you wish to return [PBRUL001]: ");
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
                System.out.println("\n* This Supplier Doesn't Supply This Product! *");
                System.out.println("* Cannot Return the Goods to This Supplier! Please Re-enter Again! *\n");
            }
        } while (!validProd);

        do {
            try {
                Supplier.inputIntRules();
                System.out.print("Enter the Quantity you want to return: ");
                qtt = scanner.nextInt();
                scanner.nextLine();
                if (qtt == -1) {
                    return;
                }
                for (WhProd whProd : whProdList) {
                    if ((whProd.getWhId().equals("KUL001")) && (whProd.getProductSKU().equals(skuCode))) {

                        if (whProd.getQuantity() < qtt) {
                            System.out.println("\n* You don't have enough products to be returned!! *");
                            System.out.println("* Please Re-enter Again! *\n");
                        }
                        else {
                            validQtt = true;
                            System.out.println("\n -------------------------------------------------------- ");
                            System.out.println("|                Confirmation Goods Return                | ");
                            System.out.println(" -------------------------------------------------------- \n");
                            System.out.print("Do you sure you wants to return " + qtt + " product '" + whProd.getProdSKU() + "' [ Y = Yes || Others = No ]: ");
                            confirmation = scanner.nextLine().equalsIgnoreCase("Y");

                            if (confirmation) {
                                for (WhProd p : whProdList) {
                                    if (p.getProdSKU().equals(whProd.getProdSKU())) {
                                        if (p.getWhId().equals("KUL001")) {
                                            p.setQuantity(p.getQuantity() - qtt);
                                            System.out.println("\nSuccessfully Return the Goods!!");
                                        }
                                        break;
                                    }
                                }
                                WhProd.writeWarehouseProductFile(whProdList);
                            } else {
                                System.out.println("\n* Failed to Return the Goods!! *");
                            }

                        }
                        break;
                    }

                }

            } catch (Exception e) {
                System.out.println("\n* You Can Only Enter Integer!! *");
                System.out.println("* Please Re-enter Again! *\n");
                scanner.nextLine();

            }

        } while (!validQtt);

        System.out.print("Press Enter to Continue...");
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

        Main.displayHeader();
        System.out.println("|                     Stock Transfer                      |");
        System.out.println(" ========================================================= ");

        do {
            Warehouse.whIdRules();
            System.out.print("Enter the Warehouse ID that you wish to transfer the stock [KLG002]: ");
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
                System.out.println("\n* Do not have this warehouse to be transferred!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validWh);

        do {
            Product.prodSKURules();
            System.out.print("Enter the Product SKU Code that you wish to transfer [PBRUL001]: ");
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
                System.out.println("\n* Do not have this product to be transferred!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validSKU);

        do {
            try {
                Supplier.inputIntRules();
                System.out.print("Enter the Quantity of product that you wish to transfer: ");
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
                    System.out.println("\n* Do not have enough product to be transferred!! *");
                    System.out.println("* Please Re-enter Again! \n*");
                } else {
                    validQtt = true;
                }
            } catch (Exception e) {
                System.out.println("\n* You Can Only Enter Integer!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validQtt);

        System.out.println("\n ----------------------------------------------------------- ");
        System.out.println("|                Confirmation Stock Transfer                |");
        System.out.println(" ----------------------------------------------------------- ");
        System.out.println("From :");
        source.displayWhProd();
        System.out.println("\nTo :");
        destination.displayWhProd();
        System.out.println("\nTransfer Quantity: " + qtt);

        System.out.print("Do you sure the transfer information is correct? [Y = YES || Others = NO]: ");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");

        if (confirmation) {
            whProdList.remove(source);
            whProdList.remove(destination);
            source.setQuantity(source.getQuantity() - qtt);
            destination.setQuantity(destination.getQuantity() + qtt);
            whProdList.add(source);
            whProdList.add(destination);
            writeWarehouseProductFile(whProdList);
            System.out.println("\nSuccessfully Perform Stock Transfer!!");
        } else {
            System.out.println("\n* Failed to Perform Stock Transfer!! *");
        }

        System.out.print("Press Enter to Continue...");
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

        Main.displayHeader();
        System.out.println("|                      Stock Return                       |");
        System.out.println(" ========================================================= \n");

        do {
            Warehouse.whIdRules();
            System.out.print("Enter the Warehouse ID that you need to return the stock to main warehouse [KLG002]: ");
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
                System.out.println("\n* This Warehouse Does Not Exist!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validWh);

        do {
            Product.prodSKURules();
            System.out.print("Enter the Product SKU Code that you wish to return [PBRUL001]: ");
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
                System.out.println("\n* This Product Does Not Exist!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validSKU);

        do {
            try {
                Supplier.inputIntRules();
                System.out.print("Enter the Quantity of Product that you wish to return: ");
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
                    System.out.println("\n* Do not have enough product to be transferred!! *");
                    System.out.println("* Please Re-enter Again! *\n");
                } else {
                    validQtt = true;
                }
            } catch (Exception e) {
                System.out.println("\n* You Can Only Enter Integer!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validQtt);

        System.out.println("\n ----------------------------------------------------------- ");
        System.out.println("|                 Confirmation Stock Return                 |");
        System.out.println(" ----------------------------------------------------------- ");
        System.out.println("From :");
        source.displayWhProd();
        System.out.println("\nTo :");
        destination.displayWhProd();
        System.out.println("Transfer Quantity: " + qtt);

        System.out.print("Do you sure the return information is correct? [Y = YES || Others = NO]: ");
        confirmation = sc.next().trim().equalsIgnoreCase("Y");
        sc.nextLine();

        if (confirmation) {
            whProdList.remove(source);
            whProdList.remove(destination);
            source.setQuantity(source.getQuantity() - qtt);
            destination.setQuantity(destination.getQuantity() + qtt);
            whProdList.add(source);
            whProdList.add(destination);
            writeWarehouseProductFile(whProdList);
            System.out.println("\nSuccessfully Perform Stock Return!!");
        } else {
            System.out.println("\n* Failed to Perform Stock Return!! *");
        }

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void resetReorderLv() {

        Scanner sc = new Scanner(System.in);
        ArrayList<WhProd> whProdList = readWarehouseProductFile();
        ArrayList<WhProd> productList = new ArrayList<>();
        ArrayList<Warehouse> warehouseList = Warehouse.readMasterWarehouseFile();
        boolean validWh = false, validLv = false;
        String whId;

        Main.displayHeader();
        System.out.println("|                   Reset Reorder Level                   |");
        System.out.println(" ========================================================= ");

        do {
            Warehouse.whIdRules();
            System.out.print("Enter Warehouse ID: ");
            whId = sc.nextLine().toUpperCase().trim();
            if (whId.equals("-1")) {
                return;
            }

            for (Warehouse wh : warehouseList) {
                if (wh.getWhID().equals(whId)) {
                    validWh = true;
                    break;
                }
            }
            if (!validWh) {
                System.out.println("\n* This Warehouse Does Not Exist!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validWh);

        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                               Product List:                               \n");
        System.out.printf(" %5s   %-8s   %-25s   %8s   %13s\n", "Index", "SKU", "Name", "Quantity", "Reorder Level");


        int prodIndex = 0, prodEdit, reorderLv;
        boolean validNum = false;
        for (WhProd whProd : whProdList) {
            if (whProd.getWhId().equals(whId)) {
                prodIndex++;
                productList.add(whProd);
                System.out.printf(" %1s%02d.    %8s   %-25s   %-8d   %-13d\n", "", prodIndex, whProd.getProductSKU(), whProd.getProdName(), whProd.getQuantity(), whProd.getReorderLv());

            }
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        Supplier.inputIntRules();
        System.out.println("Please Enter the Index Number for the Product you want to reset its reorder level.");
        prodEdit = ExtraFunction.menuInput(prodIndex);

        do {
            reorderLevelRules();
            System.out.print("Set New Reorder Level: ");
            reorderLv = sc.nextInt();
            sc.nextLine();
            try {
                if (reorderLv == -1) {
                    return;
                }
                if (reorderLv >= 5) {
                    productList.get(prodEdit - 1).setReorderLv(reorderLv);
                    System.out.println("\nSuccessfully Updated Reorder Level!!");
                    validLv = true;
                } else {
                    System.out.println("\n* Failed to Update Reorder Level!! *");
                    System.out.println("* The Reorder Level Must At Least 5! Please Re-enter Again! *\n");
                }
            } catch (Exception e) {
                System.out.println("\n* You Can Only Enter Integer!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }

        } while (!validLv);

        System.out.print("Press Enter to Continue...");
        sc.nextLine();

    }

    public static void summaryReportAll() {

        ArrayList<WhProd> whProdList = readWarehouseProductFile();
        ArrayList<Warehouse> warehouseList = Warehouse.readMasterWarehouseFile();
        ArrayList<Product> productList = Product.readMasterProductFile();

        for(WhProd whProd : whProdList){
            for(Product product : productList){
                if(whProd.getProdSKU().equals(product.getProdSKU())){
                    whProd.setProdName(product.getProdName());
                    break;
                }
            }
        }

        System.out.println("\n ============================================================================================ ");
        System.out.println("|                                       Summary Report                                       |");
        System.out.println("|============================================================================================|");

        for (Warehouse wh : warehouseList) {
            ArrayList<WhProd> reorderList = new ArrayList<>();
            ArrayList<WhProd> normalList = new ArrayList<>();
            for (WhProd whProd : whProdList) {

                if (wh.getWhID().equals(whProd.getWhId())) {
                    if (whProd.getQuantity() - whProd.getReorderLv() <= 0) {
                        reorderList.add(whProd);
                    } else {
                        normalList.add(whProd);
                    }
                }

            }

            System.out.printf("| %-13s | %-12s | %-30s | %-10s | %-13s |\n", "Warehouse ID", "Product SKU", "Product Name", "Quantity", "Reorder Level");
            System.out.println("|--------------------------------------------------------------------------------------------|");  //94 cols

            System.out.println("|                                 Product Needs to Reorder:                                  |");
            System.out.println("|--------------------------------------------------------------------------------------------|");
            for (WhProd whProd : reorderList) {
                System.out.printf("| %-13s | %-12s | %-30s | %-10d | %-13d |\n", whProd.getWhId(), whProd.getProductSKU(), whProd.getProdName(), whProd.getQuantity(), whProd.getReorderLv());
            }
            System.out.println("|--------------------------------------------------------------------------------------------|");
            System.out.println("|--------------------------------------------------------------------------------------------|");
            System.out.println("|                                 Product In Normal Status:                                  |");
            System.out.println("|--------------------------------------------------------------------------------------------|");

            //System.out.printf("| %-13s | %-12s | %-30s | %-10s | %-13s |\n", "Warehouse ID", "Product SKU", "Product Name", "Quantity", "Reorder Level");
            for (WhProd whProd : normalList) {
                System.out.printf("| %-13s | %-12s | %-30s | %-10d | %-13d |\n", whProd.getWhId(), whProd.getProductSKU(), whProd.getProdName(), whProd.getQuantity(), whProd.getReorderLv());
            }
        }
        System.out.println(" ============================================================================================\n ");

        System.out.println("                          Successfully Generate Report!!");
        System.out.print("                            Press Enter to Continue...");
        new Scanner(System.in).nextLine();

    }

    public static void summaryReportOne() {

        Scanner sc = new Scanner(System.in);
        boolean validID = false;
        String warehouseID = null;
        ArrayList<WhProd> whProdList = readWarehouseProductFile();
        ArrayList<Warehouse> warehouseList = Warehouse.readMasterWarehouseFile();
        ArrayList<Product> productList = Product.readMasterProductFile();

        // header
        // -1 to exist
        Main.displayHeader();
        System.out.println("|           Generate Report for Specific Branch           |");
        System.out.println(" ========================================================= ");


        for(WhProd whProd : whProdList){
            for(Product product : productList){
                if(whProd.getProdSKU().equals(product.getProdSKU())){
                    whProd.setProdName(product.getProdName());
                    break;
                }
            }
        }

        do {
            Warehouse.whIdRules();
            System.out.print("Enter Warehouse's ID that You Wish to Generate the Report [KUL001]: ");
            warehouseID = sc.nextLine().trim().toUpperCase();
            if (warehouseID.equals("-1")) {
                return;
            }
            for (Warehouse wh : warehouseList) {
                if (warehouseID.equals(wh.getWhID())) {
                    validID = true;
                    break;
                }
            }
            if (!validID) {
                System.out.println("\n* Invalid Warehouse ID!! *");
                System.out.println("* Please Re-enter Again! *\n");
            }
        } while (!validID);

        System.out.println("\n ============================================================================================ ");
        System.out.printf("|                              Summary Report for Branch %s                              |\n", warehouseID);
        System.out.println("|============================================================================================|");

        for (Warehouse wh : warehouseList) {
            if (wh.getWhID().equals(warehouseID)) {
                ArrayList<WhProd> reorderList = new ArrayList<>();
                ArrayList<WhProd> normalList = new ArrayList<>();
                for (WhProd whProd : whProdList) {

                    if (wh.getWhID().equals(whProd.getWhId())) {
                        if (whProd.getQuantity() - whProd.getReorderLv() <= 0) {
                            reorderList.add(whProd);
                        } else {
                            normalList.add(whProd);
                        }
                    }

                }

                System.out.printf("| %-13s | %-12s | %-30s | %-10s | %-13s |\n", "Warehouse ID", "Product SKU", "Product Name", "Quantity", "Reorder Level");
                System.out.println("|--------------------------------------------------------------------------------------------|");  //94 cols
                System.out.println("|                                 Product Needs to Reorder:                                  |");
                System.out.println("|--------------------------------------------------------------------------------------------|");

                //System.out.printf("%-13s%-12s%-30s%-10s%-13s\n", "Warehouse ID", "Product SKU", "Product Name", "Quantity", "Reorder Level");
                for (WhProd whProd : reorderList) {
                    System.out.printf("| %-13s | %-12s | %-30s | %-10d | %-13d |\n", whProd.getWhId(), whProd.getProductSKU(), whProd.getProdName(), whProd.getQuantity(), whProd.getReorderLv());
                }


                System.out.println("|--------------------------------------------------------------------------------------------|");
                System.out.println("|--------------------------------------------------------------------------------------------|");
                System.out.println("|                                 Product In Normal Status:                                  |");
                System.out.println("|--------------------------------------------------------------------------------------------|");
                //System.out.printf("%-12s%-20s%-10s%-13s\n", "Product SKU", "Product Name", "Quantity", "Reorder Level");
                for (WhProd whProd : normalList) {
                    System.out.printf("| %-13s | %-12s | %-30s | %-10d | %-13d |\n", whProd.getWhId(), whProd.getProductSKU(), whProd.getProdName(), whProd.getQuantity(), whProd.getReorderLv());
                }
            }

        }
        System.out.println(" ============================================================================================\n ");

        System.out.println("                             Successfully Generate Report!!");
        System.out.print("                               Press Enter to Continue...");
        sc.nextLine();
    }

    public void displayWhProd() {
        System.out.println("Warehouse ID: " + whId);
        System.out.println("Product SKU Code: " + prodSKU);
        System.out.println("Quantity: " + quantity);
        System.out.println("Reorder Level: " + reorderLv);
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
            System.out.println("Error (read stock file):" + e.getMessage());
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
                writer.write(Integer.toString(prod.getQuantity()));
                writer.write('|');
                writer.write(Integer.toString(prod.getReorderLv()));
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error (write stock file):" + e.getMessage());
        }
    }

    public static void reorderLevelRules() {
        System.out.println("\n---------------------------------------------------------------------");
        System.out.println("                     The Reorder Level Should Be:                      ");
        System.out.println(" 1. Can Only Enter Integer                                             ");
        System.out.println(" 2. Must At Least 5                                                    ");
        System.out.println(" 3. Cannot Enter Negative Integer [-25,-431], except exit              ");
        System.out.println("                                                                       ");
        System.out.println("  * Enter '-1' in Any Field If You Want to Exit to Previous Page *     ");
        System.out.println("---------------------------------------------------------------------\n");

    }

}
