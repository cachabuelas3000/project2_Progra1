package co.edu.uptc.project2.domain;

import co.edu.uptc.project2.enums.ProductCategory;

public class InvoiceDetail {

    // Composición o agregación

    private String productName;
    private int quantity;
    private double unitPrice;
    private double subtotal;

    private ProductCategory category;

    // Constructor vacío
    public InvoiceDetail() {

    }

    // Constructor lleno
    public InvoiceDetail(String productName, int quantity,
            double unitPrice, ProductCategory category) {

        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.category = category;

        // subtotal automático
        this.subtotal = quantity * unitPrice;
    }

    // Getters y Setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    // Método auxiliar
    private void calculateSubtotal() {
        subtotal = quantity * unitPrice;
    }

    // toString()

    @Override
    public String toString() {
        return "Detalles de factura [Nombre del producto=" + productName
                + ", cantidad=" + quantity
                + ", precio unitario=" + unitPrice
                + ", subtotal=" + subtotal
                + ", categoría=" + category + "]";
    }

    // equals() y hashCode()

    @Override
    public int hashCode() {
        return productName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        InvoiceDetail other = (InvoiceDetail) obj;

        return productName.equalsIgnoreCase(other.productName);
    }

}