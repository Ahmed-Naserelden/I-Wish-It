package connection;

import java.io.Serializable;

public class ProductPayload implements Serializable {

    private final int productId;
    private final String name;
    private final String brand;
    private final String desc;
    private final int price;
    private final String image;
    private final int contribution;

    public ProductPayload(int productId, String name, String brand, String desc, int price, String image) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.contribution = 0;
    }

    public ProductPayload(int productId, String name, String brand, String desc, int price, String image, int contribution) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.contribution = contribution;
    }

    public int getProductID() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getContribution() {
        return contribution;
    }
}
