import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Product {

    private String prodName,prodSKU;
    private int prodQty,reorderLV;

    /* Constructor */

    /* product list use for master file that only store name and quantity*/
    public Product(String prodName, String prodSKU) {
        this.prodName = prodName;
        this.prodSKU = prodSKU;
    }

    /* product list use for warehouse branches store their product */
    public Product(String prodName, int prodQty, String prodSKU, int reorderLV){
        this.prodName = prodName;
        this.prodQty = prodQty;
        this.prodSKU = prodSKU;
        this.reorderLV = reorderLV;
    }

    /*Getter & Setter*/
    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getProdQty() {
        return prodQty;
    }
    public void setProdQty(int prodQty){
        this.prodQty = prodQty;
    }

    public String getProdSKU() {
        return prodSKU;
    }
    public void setProdSKU(String prodSKU) {
        this.prodSKU = prodSKU;
    }

    public int getReorderLV(){
        return reorderLV;
    }
    public void setReorderLV(int reorderLV){
        this.reorderLV = reorderLV;
    }

    public static ArrayList<Product> readMasterProductFile() {
        String pathName = "product.txt";

        File file = new File(pathName);
        ArrayList<Product> productList = new ArrayList<>();
        Scanner scanFile;
        try {
            scanFile = new Scanner(file);
            scanFile.useDelimiter("\\|");

            while (scanFile.hasNextLine()) {
                String line = scanFile.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 2) { // Ensure the line has the expected number of fields
                    String prodName = data[0];
                    String prodSKU = data[1];

                    Product product = new Product(prodName, prodSKU);
                    productList.add(product);
                } else {
                    System.out.println("Invalid data format: " + line);
                }

            }
            scanFile.close();
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
        return productList;
    }

    public static void writeMasterProductFile(ArrayList<Product> prodList) {
        String pathName = "product.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathName))) {
            for (Product prod : prodList) {
                writer.write(prod.getProdSKU());
                writer.write('|');
                writer.write(prod.getProdName());
                writer.newLine();  // Write each item on a new line
            }

        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
        }
    }

  //  public static ArrayList<Product> readWarehouseProductFile() {}
  //  public static void writeWarehouseProductFile(ArrayList<Product> prodList) {}
}
