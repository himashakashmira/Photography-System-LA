package lk.ijse.photostudio.Entity;

public class Supplier {
    private String supplierId;
    private String supplierName;
    private String materialType;
    private String supplierContact;

    public Supplier() {
    }

    public Supplier(String supplierId, String supplierName, String materialType, String supplierContact) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.materialType = materialType;
        this.supplierContact = supplierContact;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }
}
