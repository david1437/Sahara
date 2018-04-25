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
    public int quantity;
    public String date;

    public Product(String title, float price, String category, String producerId, String productId, int quantity) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.producerId = producerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Product(String title, float price, String category, String producerId, String productId, int quantity, String date) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.producerId = producerId;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
    }
}
