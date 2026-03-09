package lk.ijse.photostudio.DTO;


public class ReportDTO {


        // Package Report
        private String packageId;
        private String packageName;
        private String packageCategory;
        private double packagePrice;
        private int usageCount;

        // Stock Report
        private String itemId;
        private String itemName;
        private int qty;
        private String supplierId;
        private String status;

    public ReportDTO() {
    }

    public ReportDTO(String packageId, String packageName, String packageCategory, double packagePrice, int usageCount) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packageCategory = packageCategory;
        this.packagePrice = packagePrice;
        this.usageCount = usageCount;
    }

    public ReportDTO(String packageId, String packageName, String packageCategory, double packagePrice, int usageCount, String itemId, String itemName, int qty, String supplierId, String status) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packageCategory = packageCategory;
        this.packagePrice = packagePrice;
        this.usageCount = usageCount;
        this.itemId = itemId;
        this.itemName = itemName;
        this.qty = qty;
        this.supplierId = supplierId;
        this.status = status;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageCategory() {
        return packageCategory;
    }

    public void setPackageCategory(String packageCategory) {
        this.packageCategory = packageCategory;
    }

    public double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "packageId='" + packageId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", packageCategory='" + packageCategory + '\'' +
                ", packagePrice=" + packagePrice +
                ", usageCount=" + usageCount +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", qty=" + qty +
                ", supplierId='" + supplierId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}