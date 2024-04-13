import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

@RestController
public class ProductController {

    // Simulated data source
    private ProductService productService = new ProductService();

    @GetMapping("/api/products")
    public List<Product> getProductsByShopper(
            @RequestParam String shopperId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "10") int limit) {

        // Call ProductService to get products based on filters
        return productService.getProductsByShopper(shopperId, category, brand, limit);
    }
}

public class ProductService {
    // Database connection parameters
    private static final String url = "jdbc:postgresql://localhost:5432/database_name";
    private static final String user = "username";
    private static final String password = "password";

    // Method to retrieve products by shopper from the database
    public List<Product> getProductsByShopper(String shopperId, String category, String brand, int limit) {
        List<Product> products = new ArrayList<>();

        String query = "SELECT p.product_id, p.category, p.brand " +
                       "FROM products p " +
                       "JOIN shoppers s ON p.product_id = s.product_id " +
                       "WHERE s.shopper_id = ?";

        if (category != null) {
            query += " AND p.category = ?";
        }
        if (brand != null) {
            query += " AND p.brand = ?";
        }
        query += " LIMIT ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, shopperId);
            int paramIndex = 2;
            if (category != null) {
                pstmt.setString(paramIndex++, category);
            }
            if (brand != null) {
                pstmt.setString(paramIndex++, brand);
            }
            pstmt.setInt(paramIndex, limit);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getString("product_id"));
                product.setCategory(rs.getString("category"));
                product.setBrand(rs.getString("brand"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}

class Product {
    private String id;
    private String name;
    private String category;
    private String brand;
    // Other product attributes

    // Constructor, getters, setters
}

// Class to hold simulated data for demonstration purposes
class DummyData {
    public static List<Product> getDummyProducts() {
        // This method would typically fetch products from your database or external API
        // For simplicity, we'll just return some dummy products
        // Replace this with actual data retrieval logic
        return List.of(
            new Product("1", "Product A", "Category A", "Brand X"),
            new Product("2", "Product B", "Category B", "Brand Y"),
            new Product("3", "Product C", "Category A", "Brand Z")
            // Add more dummy products as needed
        );
    }
}
