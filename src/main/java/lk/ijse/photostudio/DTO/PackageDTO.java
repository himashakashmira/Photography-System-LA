package lk.ijse.photostudio.DTO;

public class PackageDTO {

    private String packageId;
    private String packageName;
    private String category;
    private String packageDescription;
    private double basePrice;


    public PackageDTO() {
    }

    public PackageDTO(String packageId, String packageName, String category, String packageDescription, double basePrice) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.category = category;
        this.packageDescription = packageDescription;
        this.basePrice = basePrice;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "PackageDTO [packageId=" + packageId + ", packageName=" + packageName + ", category=" + category + ", packageDescription=" + packageDescription + ", basePrice=" + basePrice + "]";
    }
}
