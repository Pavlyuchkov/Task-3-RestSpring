package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import springApp.dto.order.OrderIncDTO;
import springApp.dto.order.OrderJoinProductOutDTO;
import springApp.dto.order.OrderOutDTO;
import springApp.dto.order.OrderUpdDTO;
import springApp.exception.NotFoundException;
import springApp.mapper.OrderDTOMapper;
import springApp.model.Customer;
import springApp.model.Order;
import springApp.model.Product;
import springApp.repository.CustomerRepository;
import springApp.repository.OrderRepository;
import springApp.repository.ProductRepository;
import springApp.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDTOMapper orderDTOMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private static HikariDataSource dataSource;

    @BeforeAll
    public static void init() {
        postgreSQLContainer.start();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariConfig.setUsername(postgreSQLContainer.getUsername());
        hikariConfig.setPassword(postgreSQLContainer.getPassword());
        dataSource = new HikariDataSource(hikariConfig);
    }

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, orderDTOMapper, customerRepository, productRepository);
    }

    @AfterAll
    public static void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testSave() {
        OrderIncDTO orderIncDTO = new OrderIncDTO();
        orderIncDTO.setProductList(new ArrayList<>());
        Order order = new Order();
        Customer customer = new Customer();
        OrderOutDTO orderOutDTO = new OrderOutDTO();

        when(orderDTOMapper.toEntityInc(orderIncDTO)).thenReturn(order);
        when(customerRepository.findById(orderIncDTO.getCustomerId())).thenReturn(java.util.Optional.of(customer));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderDTOMapper.toDTO(order)).thenReturn(orderOutDTO);

        OrderOutDTO result = orderService.save(orderIncDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(orderDTOMapper, times(1)).toDTO(order);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        Long orderId = 1L;
        OrderUpdDTO orderUpdDTO = new OrderUpdDTO();
        orderUpdDTO.setOrderId(orderId);
        Order order = new Order();
        Customer customer = new Customer();
        Order updatedOrder = new Order();
        updatedOrder.setOrderId(orderId);
        OrderUpdDTO updatedOrderDTO = new OrderUpdDTO();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(customerRepository.findById(orderUpdDTO.getCustomerId())).thenReturn(java.util.Optional.of(customer));
        when(orderRepository.save(order)).thenReturn(updatedOrder);
        when(orderDTOMapper.toDTOUpd(updatedOrder)).thenReturn(updatedOrderDTO);

        OrderUpdDTO result = orderService.update(orderId, orderUpdDTO);

        assertNotNull(result);
        verify(orderRepository, times(1)).save(order);
        verify(orderDTOMapper, times(1)).toDTOUpd(updatedOrder);
    }

    @Test
    public void testUpdateNotFound() {
        Long orderId = 1L;
        OrderUpdDTO orderUpdDTO = new OrderUpdDTO();
        orderUpdDTO.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.update(orderId, orderUpdDTO));
    }

    @Test
    public void testFindById() {
        Long orderId = 1L;
        Order order = new Order();
        OrderOutDTO orderOutDTO = new OrderOutDTO();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderDTOMapper.toDTO(order)).thenReturn(orderOutDTO);

        OrderOutDTO result = orderService.findById(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDTOMapper, times(1)).toDTO(order);
    }

    @Test
    public void testFindByIdNotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findById(orderId));
    }

    @Test
    public void testFindAll() {
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderOutDTO> orderOutDTOs = orders.stream()
                .map(order -> new OrderOutDTO())
                .toList();

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderDTOMapper.toDTO(any(Order.class))).thenReturn(new OrderOutDTO());

        List<OrderOutDTO> result = orderService.findAll();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderDTOMapper, times(orders.size())).toDTO(any(Order.class));
    }

    @Test
    public void testFindAllById() {
        Long customerId = 1L;
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderOutDTO> orderOutDTOs = orders.stream()
                .map(order -> new OrderOutDTO())
                .toList();

        when(orderRepository.findAllByCustomer_CustomerId(customerId)).thenReturn(orders);
        when(orderDTOMapper.toDTO(any(Order.class))).thenReturn(new OrderOutDTO());

        List<OrderOutDTO> result = orderService.findAllById(customerId);

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAllByCustomer_CustomerId(customerId);
        verify(orderDTOMapper, times(orders.size())).toDTO(any(Order.class));
    }

    @Test
    public void testFindAllJoinProduct() {
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderJoinProductOutDTO> orderJoinProductOutDTOs = orders.stream()
                .map(order -> new OrderJoinProductOutDTO())
                .toList();

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderDTOMapper.toDTOJoinProduct(any(Order.class))).thenReturn(new OrderJoinProductOutDTO());

        List<OrderJoinProductOutDTO> result = orderService.findAllJoinProduct();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
        verify(orderDTOMapper, times(orders.size())).toDTOJoinProduct(any(Order.class));
    }

    @Test
    public void testFindByIdJoinProduct() {
        Long orderId = 1L;
        Order order = new Order();
        List<Product> products = List.of(new Product(), new Product());
        order.setProducts(products);
        OrderJoinProductOutDTO orderJoinProductOutDTO = new OrderJoinProductOutDTO();

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        when(orderDTOMapper.toDTOJoinProduct(order)).thenReturn(orderJoinProductOutDTO);

        OrderJoinProductOutDTO result = orderService.findByIdJoinProduct(orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderDTOMapper, times(1)).toDTOJoinProduct(order);
    }

    @Test
    public void testDelete() throws NotFoundException {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(true);

        boolean result = orderService.delete(orderId);

        assertTrue(result);
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    public void testDeleteNotFound() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.delete(orderId));
    }
}