package model;

import org.junit.jupiter.api.Test;
import springApp.model.Product;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    public void testProductConstructor() {
        Product product = new Product(7L, "Хвост", 1L);

        assertThat(product.getProductId()).isEqualTo(7L);
        assertThat(product.getProductName()).isEqualTo("Хвост");
        assertThat(product.getPrice()).isEqualTo(1L);
    }
}