package lk.ijse.photostudio.DTO;

public class MaterialDTO {
    private String materialId;
    private String materialName;
    private int qty;
    private String supplierId;

    public MaterialDTO() {
    }

    public MaterialDTO(String materialId, String materialName, int qty, String supplierId) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.qty = qty;
        this.supplierId = supplierId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "materialId='" + materialId + '\'' +
                ", materialName='" + materialName + '\'' +
                ", qty=" + qty +
                ", supplierId='" + supplierId + '\'' +
                '}';
    }
}