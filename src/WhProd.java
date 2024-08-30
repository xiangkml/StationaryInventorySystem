public class WhProd extends Product{

    private int quantity, reorderLv;
    private String whId;

    public WhProd(String whID, String prodSKU, int quantity, int reorderLv) {
        super(prodSKU);
        this.whId = whID;
        this.quantity = quantity;
        this.reorderLv = reorderLv;
    }
    public WhProd(String whID, String prodSKU) {
        super(prodSKU);
        this.whId = whID;
        this.quantity = 0;
        this.reorderLv =20;

        // default value for quantity is 0 and reorderLevel is 20
        // use when add a new product to master file and all warehouse will automatically add the product
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


//  public static ArrayList<Product> readWarehouseProductFile() {}
    //  public static void writeWarehouseProductFile(ArrayList<Product> prodList) {}

}
// warehouseID , name , address
// ProductList {sku,name,quantity,reorder level}