package model;

import org.junit.jupiter.api.Test;
import springApp.model.Customer;
import springApp.model.Order;
import springApp.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void testOrderConstructor() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(5L, "Водяра", 10L));

        Customer customer = new Customer(3L, "Урод", "Уродов", null);

        Order order = new Order(2L, "Новый", customer, products);

        assertThat(order.getOrderId()).isEqualTo(2L);
        assertThat(order.getOrderStatus()).isEqualTo("Новый");
        assertThat(order.getCustomer()).isEqualTo(customer);
        assertThat(order.getProducts()).containsExactlyElementsOf(products);
    }
}