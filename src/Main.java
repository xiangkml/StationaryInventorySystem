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
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    public static void secondMenuList() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Supplier");
        System.out.println("2. Product");
        System.out.println("3. Warehouse");
        System.out.println("4. Generate Summary Report");
        System.out.println("5. Return to Previous Menu");
    }

    public static void supplierMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("-------------------- [ Supplier Menu ] -------------------");
        System.out.println("1. Purchase Order");
        System.out.println("2. Good Receive from Supplier");
        System.out.println("3. Good Return to Supplier");
        System.out.println("4. Add Supplier");
        System.out.println("5. Edit Supplier");
        System.out.println("6. Delete Supplier");
        System.out.println("7. Search & Display Supplier");
        System.out.println("8. Return to Previous Menu");
    }

    public static void productMenu() { //Jiayu
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Add Product");
        System.out.println("2. Edit Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Search & Display Product");
        System.out.println("5. Return to Previous Menu");
    }

    public static void warehouseMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Add Warehouse");
        System.out.println("2. Edit Warehouse");
        System.out.println("3. Delete Warehouse");
        System.out.println("4. Search & Display Warehouse");
        System.out.println("5. Stock Transfer to Other Branches");
        System.out.println("6. Stock Return from Other Branches");
        System.out.println("7. Return to Previous Menu");
    }

    public static void productWarehouseMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("4. Search & Display");
    }

    public static void poMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Add Purchase Order");
        System.out.println("2. View Purchase Order History");
        System.out.println("3. Return to Previous Menu");
    }

    public static void viewSupplierMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. View All Supplier Details");
        System.out.println("2. View Specific Supplier Details");
        System.out.println("3. Return to Previous Menu");
    }

    public static void editSupplierMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. Supplier Name");
        System.out.println("2. Supplier Email");
        System.out.println("3. Supplier Tel");
        System.out.println("4. Supplier Address");
        System.out.println("5. Product Supplied");
        System.out.println("6. Return to Previous Menu");
    }

    public static void viewWarehouseMenu() {
        System.out.println("---------- ThaiKuLa Warehouse Management System ----------");
        System.out.println("1. View All Warehouse Details");
        System.out.println("2. View Specific Warehouse Details");
        System.out.println("3. Return to Previous Menu");
    }
}








