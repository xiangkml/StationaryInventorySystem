import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Supplier {
    private String companyName,id,email,hotline,address,pic,tel;
    private ArrayList<String> supplyProduct = new ArrayList<>();

    public Supplier(String companyName, String email, String hotline, String address, String pic, String tel, ArrayList<String> supplyProduct) {
        this.companyName = companyName;
        this.id = String.format("SUP%03d", countSupplier() + 1);
        this.email = email;
        this.hotline = hotline;
        this.address = address;
        this.pic = pic;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public Supplier(String companyName, String id, String email, String hotline, String address, String pic, String tel, ArrayList<String> supplyProduct) {
        this.companyName = companyName;
        this.id = id;
        this.email = email;
        this.hotline = hotline;
        this.address = address;
        this.pic = pic;
        this.tel = tel;
        this.supplyProduct = supplyProduct;
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getHotline() {
        return hotline;
    }
    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
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

    public void addSupplier(){
        String companyName,email,hotline,address,pic,tel;
        int numOfSupplyProduct;
        ArrayList<String> supplyProduct = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter supplier company name [ThaiKuLa Sdn Bhd]: ");
        companyName = sc.nextLine();

        System.out.println("Enter supplier email [thaikula520@gmail.com]: ");
        email = sc.nextLine();

        System.out.println("Enter supplier hotline [03-xxxx xxxx]: ");
        hotline = sc.nextLine();

        System.out.println("Enter supplier address : ");
        address = sc.nextLine();

        System.out.println("Enter Person In Charge Name [First Name + Last Name]: ");
        pic = sc.nextLine();

        System.out.println("Enter PIC Tel [01x-xxx xxxx | 01x-xxxx xxxx]: ");
        tel = sc.nextLine();

        System.out.println("Enter number of product [Integer]: ");
        numOfSupplyProduct = sc.nextInt();

        for (int i = 0; i < numOfSupplyProduct; i++) {

            String skuCode;

            System.out.println("Enter product SKU code [OLBOK007]:");
            skuCode = sc.nextLine();

            // validate input

            supplyProduct.add(skuCode);


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

                if (data.length >= 8) { // Ensure the line has the expected number of fields
                    String companyName = data[0];
                    String id = data[1];
                    String email = data[2];
                    String hotline = data[3];
                    String address = data[4];
                    String pic = data[5];
                    String tel = data[6];

                    productSkuList.addAll(Arrays.asList(data).subList(7, data.length + 1));

                    Supplier supplier = new Supplier(companyName,id,email,hotline,address,pic,tel,productSkuList);
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
                writer.write(supplier.getCompanyName());
                writer.write('|');
                writer.write(supplier.getId());
                writer.write('|');
                writer.write(supplier.getEmail());
                writer.write('|');
                writer.write(supplier.getHotline());
                writer.write('|');
                writer.write(supplier.getAddress());
                writer.write('|');
                writer.write(supplier.getPic());
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
