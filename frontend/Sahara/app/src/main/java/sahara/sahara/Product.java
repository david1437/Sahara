package sahara.sahara;

/**
 * Created by root on 4/22/18.
 */

public class Product {
    public String title;
    public float price;
    public String category;
    public String producerId;
    public String productId;

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getProducerId() {
        return producerId;
    }

    public String getProductId() {
        return productId;
    }

    public Product(String title, float price, String category, String producerId, String productId) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.producerId = producerId;
        this.productId = productId;
    }
}
