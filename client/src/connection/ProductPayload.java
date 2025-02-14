package connection;

import java.io.Serializable;

/**
 * ProductPayload class to represent the data for a product.
 * Implements Serializable for object serialization.
 */
public class ProductPayload implements Serializable {

    private final int productId; // ID of the product
    private final String name; // Name of the product
    private final String brand; // Brand of the product
    private final String desc; // Description of the product
    private final int price; // Price of the product
    private final String image; // Image URL of the product
    private final int contribution; // Contribution amount for the product

    /**
     * Constructs a new ProductPayload with the specified details.
     *
     * @param productId ID of the product.
     * @param name Name of the product.
     * @param brand Brand of the product.
     * @param desc Description of the product.
     * @param price Price of the product.
     * @param image Image URL of the product.
     */
    public ProductPayload(int productId, String name, String brand, String desc, int price, String image) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.contribution = 0;
    }

    /**
     * Constructs a new ProductPayload with the specified details, including contribution.
     *
     * @param productId ID of the product.
     * @param name Name of the product.
     * @param brand Brand of the product.
     * @param desc Description of the product.
     * @param price Price of the product.
     * @param image Image URL of the product.
     * @param contribution Contribution amount for the product.
     */
    public ProductPayload(int productId, String name, String brand, String desc, int price, String image, int contribution) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.desc = desc;
        this.price = price;
        this.image = image;
        this.contribution = contribution;
    }

    /**
     * Gets the ID of the product.
     *
     * @return The ID of the product.
     */
    public int getProductID() {
        return productId;
    }

    /**
     * Gets the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the brand of the product.
     *
     * @return The brand of the product.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets the description of the product.
     *
     * @return The description of the product.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the price of the product.
     *
     * @return The price of the product.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets the image URL of the product.
     *
     * @return The image URL of the product.
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets the contribution amount for the product.
     *
     * @return The contribution amount for the product.
     */
    public int getContribution() {
        return contribution;
    }
}
