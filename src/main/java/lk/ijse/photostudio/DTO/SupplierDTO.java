package lk.ijse.photostudio.DTO;

public class SupplierDTO {

    private String supplierId;
    private String supplierName;
    private String materialType;
    private String supplierContact;

    public SupplierDTO() {
    }

    public  SupplierDTO(String supplierId, String supplierName, String materialType, String supplierContact) {
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

    @Override
    public String toString() {
        return "SupplierDTO [supplierId=" + supplierId + ", supplierName=" + supplierName + ", materialType=" + materialType + ", supplierContact=" + supplierContact + "]";
    }
}
