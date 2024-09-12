import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static java.lang.Character.isDigit;

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
                    System.out.println("Thank you for using ThaiKuLa Warehouse Management System !");
                    return;
            }
        }

    }


    public static boolean secondMenu() {

        secondMenuList();
        int secondMenuInput = ExtraFunction.menuInput(5);
        boolean exitPage = false;
        switch (secondMenuInput) {
            case 1:
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            default:
                exitPage = true;

        }
        return exitPage;
    }

    public static void mainMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                       Main Menu:                        |");
        System.out.println("|                                                         |");
        System.out.println("|                       1. Login                          |");
        System.out.println("|                       2. Register                       |");
        System.out.println("|                       3. Exit                           |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void secondMenuList() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|            What Part You Want To Proceed To?            |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Supplier                               |");
        System.out.println("|               2. Product                                |");
        System.out.println("|               3. Warehouse                              |");
        System.out.println("|               4. Stock Management                       |");
        System.out.println("|               5. Generate Summary Report                |");
        System.out.println("|               6. Return to Previous Menu                |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void supplierMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                     Supplier Menu:                      |");
        System.out.println("|                                                         |");
        System.out.println("|              1. Add Supplier                            |");
        System.out.println("|              2. Edit Supplier                           |");
        System.out.println("|              3. Delete Supplier                         |");
        System.out.println("|              4. Search & Display Supplier               |");
        System.out.println("|              5. Return to Previous Menu                 |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void whProdMenu(){
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                 Stock Management Menu:                  |");
        System.out.println("|                                                         |");
        System.out.println("|           1. Purchase Order                             |");
        System.out.println("|           2. Goods Receive from Supplier                |");
        System.out.println("|           3. Goods Return to Supplier                   |");
        System.out.println("|           4. Stock Transfer to Other Branches           |");
        System.out.println("|           5. Stock Return from Other Branches           |");
        System.out.println("|           6. Reset Reorder Level                        |");
        System.out.println("|           7. Return to Previous Menu                    |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void productMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                      Product Menu:                      |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Add Product                            |");
        System.out.println("|               2. Edit Product                           |");
        System.out.println("|               3. Delete Product                         |");
        System.out.println("|               4. Search & Display Product               |");
        System.out.println("|               5. Return to Previous Menu                |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void summaryReportMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                  Summary Report Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|        1. Generate Report for All Branches              |");
        System.out.println("|        2. Generate Report for Specific Branches         |");
        System.out.println("|        3. Return to Previous Menu                       |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void warehouseMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                     WareHouse Menu:                     |");
        System.out.println("|                                                         |");
        System.out.println("|          1. Add Warehouse                               |");
        System.out.println("|          2. Edit Warehouse                              |");
        System.out.println("|          3. Delete Warehouse                            |");
        System.out.println("|          4. Search & Display Warehouse Details          |");
        System.out.println("|          5. Return to Previous Menu                     |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void poMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                  Purchase Order Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|             1. Add Purchase Order                       |");
        System.out.println("|             2. View Purchase Order History              |");
        System.out.println("|             3. Return to Previous Menu                  |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void viewSupplierMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                   View Supplier Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|            1. View All Supplier Details                 |");
        System.out.println("|            2. View Specific Supplier Details            |");
        System.out.println("|            3. Return to Previous Menu                   |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void editSupplierMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                   Edit Supplier Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Supplier Name                          |");
        System.out.println("|               2. Supplier Email                         |");
        System.out.println("|               3. Supplier Tel                           |");
        System.out.println("|               4. Supplier Address                       |");
        System.out.println("|               5. Product Supplied                       |");
        System.out.println("|               6. Return to Previous Menu                |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void editWarehouseMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                  Edit WareHouse Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Warehouse Name                         |");
        System.out.println("|               2. Warehouse Id                           |");
        System.out.println("|               3. Warehouse Address                      |");
        System.out.println("|               4. Return to Previous Menu                |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void editProductMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                   Edit Product Menu:                    |");
        System.out.println("|                                                         |");
        System.out.println("|               1. Product Name                           |");
        System.out.println("|               2. Product SKU                            |");
        System.out.println("|               3. Return to Previous Menu                |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void viewWarehouseMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                  View WareHouse Menu:                   |");
        System.out.println("|                                                         |");
        System.out.println("|           1. View All Warehouse Details                 |");
        System.out.println("|           2. View Specific Warehouse Details            |");
        System.out.println("|           3. Return to Previous Menu                    |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");
    }

    public static void viewProductMenu() {
        displayHeader();
        System.out.println("|                                                         |");
        System.out.println("|                   View Product Menu:                    |");
        System.out.println("|                                                         |");
        System.out.println("|            1. View All Product Details                  |");
        System.out.println("|            2. View Specific Product Details             |");
        System.out.println("|            3. Return to Previous Menu                   |");
        System.out.println("|                                                         |");
        System.out.println(" ========================================================= ");

    }

    public static void displayHeader(){

        System.out.println(" ========================================================= ");
        System.out.println("|          ThaiKuLa Warehouse Management System           |");
        System.out.println(" ========================================================= ");

    }
}








