import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Supplier {
    private String name, id, email, address, tel;
    private ArrayList<String> supplyProduct = new ArrayList<>();

    public Supplier(String name, String email, String address, String tel, ArrayList<String> supplyProduct) {
        this.name = name;
        this.id = String.format("SUP%03d", countSupplier() + 1);
        this.email = email;
        this.address = address;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public Supplier(String name, String id, String email, String address, String tel, ArrayList<String> supplyProduct) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.address = address;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public ArrayList<String> getSupplyProduct() {
        return supplyProduct;
    }

    public void setSupplyProduct(ArrayList<String> supplyProduct) {
        this.supplyProduct = supplyProduct;
    }

    public void addSupplier() {
        String supName = "", email = "", address = "", tel = "";
        boolean exitPage = false;
        int numOfSupplyProduct = 0;
        ArrayList<String> supplyProduct = new ArrayList<>();
        ArrayList<Product> masterProduct = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        System.out.println("----------- Supplier Registration -----------");
        System.out.println("Enter '-1' to exit");

        System.out.println("Enter supplier name [Kee Meng La]: ");
        supName = sc.nextLine().trim();
        exitPage = supName.equals("-1");

        if (!exitPage) {
            boolean validInput = true;
            do {
                System.out.println("Enter supplier email [thaikula520@gmail.com]: ");
                email = sc.nextLine().trim();
                exitPage = email.equals("-1");

                if (!ExtraFunction.checkPattern(email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")) {
                    System.out.println("Enter valid email address");
                    validInput = false;
                }
            } while (!validInput);

        }

        if (!exitPage) {
            System.out.println("Enter supplier address : ");
            address = sc.nextLine().trim().toUpperCase();
            exitPage = address.equals("-1");
        }

        if (!exitPage) {
            boolean validInput;
            do {
                System.out.println("Enter Telephone Number [01x-xxx xxxx | 01x-xxxx xxxx]: ");
                tel = sc.nextLine().trim();
                exitPage = tel.equals("-1");
                validInput = true;
                if (!ExtraFunction.checkPattern(tel, "^01[02-46-9]-[0-9]{3} [0-9]{4}$|^011-[0-9]{4} [0-9]{4}$")) {
                    validInput = false;
                }
            } while (!validInput);
        }

        if (!exitPage) {
            boolean validInput;
            do {
                System.out.println("Enter number of product [Integer]: ");
                try {
                    numOfSupplyProduct = sc.nextInt();
                    exitPage = (numOfSupplyProduct == -1);
                    validInput = true;
                } catch (Exception e) {
                    validInput = false;
                    System.out.println("Invalid Input.");
                    System.out.println("Only enter integer!");
                    sc.nextLine();
                }
            } while (!validInput);
        }

        if (!exitPage) {
            for (int i = 0; (i < numOfSupplyProduct) && (!exitPage); i++) {

                boolean validProd = false;
                String skuCode;

                do {
                    System.out.println("Enter product SKU code [OLBOK007]:");
                    skuCode = sc.nextLine().trim();
                    exitPage = (skuCode.equals("-1"));

                    if (ExtraFunction.checkPattern(skuCode, "^[A-Za-z]{5}\\d{3}$")) {
                        masterProduct = Product.readMasterProductFile();
                        for (Product prod : masterProduct) {
                            if (prod.getProdSKU().equals(skuCode)) {
                                validProd = true;
                                supplyProduct.add(skuCode);
                                break;
                            }
                        }
                        if (!validProd) {
                            System.out.println("Invalid product SKU code");
                            System.out.println("Please register new product before register a new supplier");
                        }
                    }

                } while ((!validProd) && (!exitPage));

                if (!exitPage) {

                    Supplier newSupplier = new Supplier(name, email, address, tel, supplyProduct);
                    ArrayList<Supplier> supplierList = readSupplierFile();
                    supplierList.add(newSupplier);
                    writeSupplierFile(supplierList);

                    System.out.println("Successfully registered a new supplier!");
                    System.out.println("Press Enter to continue...");
                    sc.nextLine();
                }
            }
        }

    }


    public static ArrayList<Supplier> readSupplierFile() {
        String pathName = "supplier.txt";

        File file = new File(pathName);
        ArrayList<Supplier> supplierList = new ArrayList<>();
        ArrayList<String> productSkuList = new ArrayList<>();
        Scanner scanFile = null;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length >= 6) { // Ensure the line has the expected number of fields
                    String companyName = data[0];
                    String id = data[1];
                    String email = data[2];
                    String address = data[3];
                    String tel = data[4];

                    productSkuList.addAll(Arrays.asList(data).subList(5, data.length + 1));

                    Supplier supplier = new Supplier(companyName, id, email, address, tel, productSkuList);
                    supplierList.add(supplier);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return supplierList;
    }

    public static void writeSupplierFile(ArrayList<Supplier> supplierList) {
        String pathName = "supplier.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Supplier supplier : supplierList) {
                writer.write(supplier.getName());
                writer.write('|');
                writer.write(supplier.getId());
                writer.write('|');
                writer.write(supplier.getEmail());
                writer.write('|');
                writer.write(supplier.getAddress());
                writer.write('|');
                writer.write(supplier.getTel());
                writer.write('|');
                for (String product : supplier.getSupplyProduct()) {
                    writer.write(product);
                    writer.write('|');
                }
                writer.write('\b');
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

    private int countSupplier() {
        ArrayList<Supplier> supplier = readSupplierFile();
        return supplier.size();
    }
}
