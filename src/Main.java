import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        int loginChoice;
        boolean validateLogin = false;
        boolean exitFromNextMenu = false;

        while (!validateLogin || exitFromNextMenu) {
            mainMenu();
            loginChoice = ExtraFunction.menuInput(3);
            Staff staff = new Staff();
            switch (loginChoice) {
                case 1:
                    validateLogin = staff.login();
                    if (validateLogin) {
                        exitFromNextMenu = secondMenu();
                        break;
                    }
                    break;

                case 2:
                    Staff.register();
                    break;

                default:
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println("Thank You for Using ThaiKuLa Warehouse Management System!!!");
                    System.out.println("                Hope You Have a Nice Day~~");
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    return;
            }
        }

    }

    public static boolean secondMenu() {

        boolean exitPage = false;

        do{
            secondMenuList();
            int secondMenuInput = ExtraFunction.menuInput(6);
            switch (secondMenuInput) {
                case 1:
                    supplierEntry();
                    break;

                case 2:
                    productEntry();
                    break;

                case 3:
                    warehouseEntry();
                    break;

                case 4:
                    stockMngEntry();
                    break;

                case 5:
                    summaryEntry();
                    break;

                default:
                    exitPage = true;

            }
        }while(!exitPage);

        return exitPage;
    }

    public static void supplierEntry(){

        int menuInput;
        do{
            supplierMenu();
            menuInput = ExtraFunction.menuInput(5);

            switch (menuInput) {
                case 1:
                    Supplier.addSupplier();
                    break;

                case 2:
                    Supplier.editSupplier();
                    break;

                case 3:
                    Supplier.deleteSupplier();
                    break;

                case 4:
                    Supplier.viewSupplier();
                    break;

                default:
                    return;

            }
        }while(menuInput != 5);

    }

    public static void productEntry(){
        int menuInput;
        do{
            productMenu();
            menuInput = ExtraFunction.menuInput(5);

            switch (menuInput) {
                case 1:
                    Product.addProduct();
                    break;

                case 2:
                    Product.editProduct();
                    break;

                case 3:
                    Product.deleteProduct();
                    break;

                case 4:
                    Product.viewProduct();
                    break;

                default:
                    return;

            }
        }while(menuInput != 5);
    }

    public static void warehouseEntry(){
        int menuInput;
        do{
            warehouseMenu();
            menuInput = ExtraFunction.menuInput(5);

            switch (menuInput) {
                case 1:
                    Warehouse.addWarehouse();
                    break;

                case 2:
                    Warehouse.editWarehouse();
                    break;

                case 3:
                    Warehouse.deleteWarehouse();
                    break;

                case 4:
                    Warehouse.viewWarehouse();
                    break;

                default:
                    return;

            }
        }while(menuInput != 5);
    }

    public static void stockMngEntry(){
        int menuInput;
        do{
            whProdMenu();
            menuInput = ExtraFunction.menuInput(7);

            switch (menuInput) {
                case 1:
                    poEntry();
                    break;

                case 2:
                    WhProd.goodsReceive();
                    break;

                case 3:
                    WhProd.goodsReturn();
                    break;

                case 4:
                    WhProd.stockTransfer();
                    break;

                case 5:
                    WhProd.stockReturn();
                    break;

                case 6:
                    WhProd.resetReorderLv();
                    break;

                default:
                    return;

            }
        }while(menuInput != 5);
    }

    public static void poEntry(){
        int menuInput;
        do{
            poMenu();
            menuInput = ExtraFunction.menuInput(3);

            switch (menuInput) {
                case 1:
                    // add po
                    PurchaseOrder.addPurchaseOrder();
                    break;

                case 2:
                    // view po
                    PurchaseOrder.viewPOhistory();
                    break;


                default:
                    return;

            }
        }while(menuInput != 3);
    }

    public static void summaryEntry(){
        int menuInput;
        do{
            summaryReportMenu();
            menuInput = ExtraFunction.menuInput(3);

            switch (menuInput) {
                case 1:
                    // report for all branches
                    break;

                case 2:
                    // report for one branches
                    break;


                default:
                    return;

            }
        }while(menuInput != 3);
    }

    public static void mainMenu() {
        displayHeader();
        System.out.println("|                       Main Menu:                        |");
        System.out.println("|                                                         |");
        System.out.println("|                       1. Login                          |");
        System.out.println("|                       2. Register                       |");
        System.out.println("|                       3. Exit                           |");
        System.out.println(" ========================================================= ");
    }

    public static void secondMenuList() {
        displayHeader();
        System.out.println("|           Which Part You Want To Proceed To?            |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Supplier                               |");
        System.out.println("|               2. Product                                |");
        System.out.println("|               3. Warehouse                              |");
        System.out.println("|               4. Stock Management                       |");
        System.out.println("|               5. Generate Summary Report                |");
        System.out.println("|               6. Return to Previous Menu                |");
        System.out.println(" ========================================================= ");
    }

    public static void supplierMenu() {
        displayHeader();
        System.out.println("|                     Supplier Menu:                      |");
        System.out.println("|                                                         |");
        System.out.println("|              1. Add Supplier                            |");
        System.out.println("|              2. Edit Supplier                           |");
        System.out.println("|              3. Delete Supplier                         |");
        System.out.println("|              4. View Supplier                           |");
        System.out.println("|              5. Return to Previous Menu                 |");
        System.out.println(" ========================================================= ");
    }

    public static void whProdMenu(){
        displayHeader();
        System.out.println("|                 Stock Management Menu:                  |");
        System.out.println("|                                                         |");
        System.out.println("|           1. Purchase Order                             |");
        System.out.println("|           2. Goods Receive from Supplier                |");
        System.out.println("|           3. Goods Return to Supplier                   |");
        System.out.println("|           4. Stock Transfer to Other Branches           |");
        System.out.println("|           5. Stock Return from Other Branches           |");
        System.out.println("|           6. Reset Reorder Level                        |");
        System.out.println("|           7. Return to Previous Menu                    |");
        System.out.println(" ========================================================= ");
    }

    public static void productMenu() {
        displayHeader();
        System.out.println("|                      Product Menu:                      |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Add Product                            |");
        System.out.println("|               2. Edit Product                           |");
        System.out.println("|               3. Delete Product                         |");
        System.out.println("|               4. View Product                           |");
        System.out.println("|               5. Return to Previous Menu                |");
        System.out.println(" ========================================================= ");
    }

    public static void summaryReportMenu() {
        displayHeader();
        System.out.println("|                  Summary Report Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|        1. Generate Report for All Branches              |");
        System.out.println("|        2. Generate Report for Specific Branches         |");
        System.out.println("|        3. Return to Previous Menu                       |");
        System.out.println(" ========================================================= ");
    }

    public static void warehouseMenu() {
        displayHeader();
        System.out.println("|                     WareHouse Menu:                     |");
        System.out.println("|                                                         |");
        System.out.println("|          1. Add Warehouse                               |");
        System.out.println("|          2. Edit Warehouse                              |");
        System.out.println("|          3. Delete Warehouse                            |");
        System.out.println("|          4. Search & Display Warehouse Details          |");
        System.out.println("|          5. Return to Previous Menu                     |");
        System.out.println(" ========================================================= ");
    }

    public static void poMenu() {
        displayHeader();
        System.out.println("|                  Purchase Order Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|             1. Add Purchase Order                       |");
        System.out.println("|             2. View Purchase Order History              |");
        System.out.println("|             3. Return to Previous Menu                  |");
        System.out.println(" ========================================================= ");
    }

    public static void viewSupplierMenu() {
        displayHeader();
        System.out.println("|                   View Supplier Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|            1. View All Supplier Details                 |");
        System.out.println("|            2. View Specific Supplier Details            |");
        System.out.println("|            3. Return to Previous Menu                   |");
        System.out.println(" ========================================================= ");
    }

    public static void editSupplierMenu() {
        displayHeader();
        System.out.println("|                   Edit Supplier Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Supplier Name                          |");
        System.out.println("|               2. Supplier Email                         |");
        System.out.println("|               3. Supplier Tel                           |");
        System.out.println("|               4. Supplier Address                       |");
        System.out.println("|               5. Product Supplied                       |");
        System.out.println("|               6. Return to Previous Menu                |");
        System.out.println(" ========================================================= ");
    }

    public static void editWarehouseMenu() {
        displayHeader();
        System.out.println("|                  Edit WareHouse Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Warehouse Name                         |");
        System.out.println("|               2. Warehouse Id                           |");
        System.out.println("|               3. Warehouse Address                      |");
        System.out.println("|               4. Return to Previous Menu                |");
        System.out.println(" ========================================================= ");
    }

    public static void editProductMenu() {
        displayHeader();
        System.out.println("|                   Edit Product Menu:                    |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Product Name                           |");
        System.out.println("|               2. Product SKU                            |");
        System.out.println("|               3. Return to Previous Menu                |");
        System.out.println(" ========================================================= ");
    }

    public static void viewWarehouseMenu() {
        displayHeader();
        System.out.println("|                  View WareHouse Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|           1. View All Warehouse Details                 |");
        System.out.println("|           2. View Specific Warehouse Details            |");
        System.out.println("|           3. Return to Previous Menu                    |");
        System.out.println(" ========================================================= ");
    }

    public static void viewProductMenu() {
        displayHeader();
        System.out.println("|                   View Product Menu:                    |");
        System.out.println("|                                                         |");
        System.out.println("|            1. View All Product Details                  |");
        System.out.println("|            2. View Specific Product Details             |");
        System.out.println("|            3. Return to Previous Menu                   |");
        System.out.println(" ========================================================= ");

    }

    public static void displayHeader(){

        System.out.println("\n\n ========================================================= ");
        System.out.println("|          ThaiKuLa Warehouse Management System           |");
        System.out.println("|---------------------------------------------------------|");

    }
}








