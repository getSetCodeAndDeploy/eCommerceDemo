import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseInterface {
    // Database connection parameters
    private static final String url = "jdbc:postgresql://localhost:5432/database_name";
    private static final String user = "username";
    private static final String password = "password";

    // Method to store shopper data in the database
    public static void storeShopperData(String shopperId, List<ShelfItem> shelfItems) {
        String sql = "INSERT INTO shoppers (shopper_id, product_id, relevancy_score) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (ShelfItem item : shelfItems) {
                pstmt.setString(1, shopperId);
                pstmt.setString(2, item.getProductId());
                pstmt.setFloat(3, item.getRelevancyScore());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method to store product metadata in the database
    public static void storeProductMetadata(String productId, String category, String brand) {
        String sql = "INSERT INTO products (product_id, category, brand) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, category);
            pstmt.setString(3, brand);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example shopper data and product metadata
        String shopperId = "S-1000";
        List<ShelfItem> shelfItems = List.of(
                new ShelfItem("MB-2093193398", 31.089209569320897f),
                new ShelfItem("BB-2144746855", 55.16626010671777f),
                new ShelfItem("MD-543564697", 73.01492966268303f)
        );

        String productId = "BB-2144746855";
        String category = "Babies";
        String brand = "Babyom";

        // Store shopper data
        storeShopperData(shopperId, shelfItems);

        // Store product metadata
        storeProductMetadata(productId, category, brand);
    }
}

// Class representing shelf item
class ShelfItem {
    private String productId;
    private float relevancyScore;

    public ShelfItem(String productId, float relevancyScore) {
        this.productId = productId;
        this.relevancyScore = relevancyScore;
    }

    public String getProductId() {
        return productId;
    }

    public float getRelevancyScore() {
        return relevancyScore;
    }
}
