package mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import springApp.dto.order.OrderIncDTO;
import springApp.dto.order.OrderJoinProductOutDTO;
import springApp.dto.order.OrderOutDTO;
import springApp.dto.order.OrderUpdDTO;
import springApp.mapper.OrderDTOMapper;
import springApp.model.Customer;
import springApp.model.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderDTOMapperTest {

    private OrderDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(OrderDTOMapper.class);
    }

    @Test
    void testToEntityInc() {
        OrderIncDTO dto = new OrderIncDTO();
        dto.setOrderStatus("Новый заказ");

        Order order = mapper.toEntityInc(dto);

        assertNotNull(order);
        assertEquals("Новый заказ", order.getOrderStatus());
    }

    @Test
    void testToDTOUpd() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Order order = new Order();
        order.setOrderStatus("Обновленный заказ");
        order.setCustomer(customer);

        OrderUpdDTO dto = mapper.toDTOUpd(order);

        assertNotNull(dto);
        assertEquals(1L, dto.getCustomerId());
    }

    @Test
    void testToDTO() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Order order = new Order();
        order.setOrderStatus("Новый заказ");
        order.setCustomer(customer);

        OrderOutDTO dto = mapper.toDTO(order);

        assertNotNull(dto);
        assertEquals(1L, dto.getCustomerId());
    }

    @Test
    void testToDTOJoinProduct() {
        Customer customer = new Customer();
        customer.setCustomerId(1L);

        Order order = new Order();
        order.setOrderStatus("Новый заказ с продуктами");
        order.setCustomer(customer);

        OrderJoinProductOutDTO dto = mapper.toDTOJoinProduct(order);

        assertNotNull(dto);
        assertEquals(1L, dto.getCustomerId());
    }
}