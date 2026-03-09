package lk.ijse.photostudio.DTO;

public class PackageAdditionalDTO {
    private String optionId;
    private String optionName;
    private double price;
    private String description;

    public PackageAdditionalDTO() {
    }

    public PackageAdditionalDTO(String optionId, String optionName, double price, String description) {
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

    @Override
    public String toString() {
        return "PackageAdditionalDTO{" +
                "optionId='" + optionId + '\'' +
                ", optionName='" + optionName + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
