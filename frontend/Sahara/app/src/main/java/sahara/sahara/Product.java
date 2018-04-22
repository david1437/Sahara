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

    public Product(String title, float price, String category, String producerId, String productId) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.producerId = producerId;
        this.productId = productId;
    }
}
