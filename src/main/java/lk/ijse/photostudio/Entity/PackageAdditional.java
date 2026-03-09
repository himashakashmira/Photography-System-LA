package lk.ijse.photostudio.Entity;

public class PackageAdditional {
    private String optionId;
    private String optionName;
    private double price;
    private String description;

    public PackageAdditional() {
    }

    public PackageAdditional(String optionId, String optionName, double price, String description) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.price = price;
        this.description = description;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
