package model;

import org.junit.jupiter.api.Test;
import springApp.model.Customer;
import springApp.model.Order;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    @Test
    public void testCustomerConstructor() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(new Order());

        Customer customer = new Customer(1L, "Иван", "Иванов", orderList);

        assertThat(customer.getCustomerId()).isEqualTo(1L);
        assertThat(customer.getCustomerName()).isEqualTo("Иван");
        assertThat(customer.getCustomerSurname()).isEqualTo("Иванов");
        assertThat(customer.getOrderList()).containsExactlyElementsOf(orderList);
    }
}