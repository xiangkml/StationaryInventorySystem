public class Product {

    private String prodName;
    private int prodQty;
    private String prodSKU;

    /* Constructor */
    public Product(String prodName, int prodQty, String prodSKU) {}

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
}
