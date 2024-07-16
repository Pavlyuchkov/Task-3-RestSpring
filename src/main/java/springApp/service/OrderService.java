package springApp.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOMapper orderDTOMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderDTOMapper orderDTOMapper, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDTOMapper = orderDTOMapper;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public List<OrderOutDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(orderDTOMapper::toDTO)
                .toList();
    }

    public List<OrderOutDTO> findAllById(Long customerId) {
        return orderRepository.findAllByCustomer_CustomerId(customerId).stream()
                .map(orderDTOMapper::toDTO)
                .toList();
    }

    public List<OrderJoinProductOutDTO> findAllJoinProduct() {
        return orderRepository.findAll().stream()
                .map(orderDTOMapper::toDTOJoinProduct)
                .toList();
    }

    public OrderOutDTO findById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("This Order does not exist: " + orderId));
        return orderDTOMapper.toDTO(order);
    }

    public OrderJoinProductOutDTO findByIdJoinProduct(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("This Order does not exist: " + orderId));
        List<Product> products = order.getProducts();
        order.setProducts(products);

        return orderDTOMapper.toDTOJoinProduct(order);
    }

    @Transactional
    public OrderOutDTO save(OrderIncDTO orderIncDTO) {
        Order order = orderDTOMapper.toEntityInc(orderIncDTO);

        Customer customer = customerRepository.findById(orderIncDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("This Customer does not exist: " + orderIncDTO.getCustomerId()));

        List<Product> products = orderIncDTO.getProductList();
        if (products == null) {
            products = Collections.emptyList();
        } else {
            products = products.stream()
                    .map(product -> productRepository.findById(product.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("This Product does not exist: " + product.getProductId()))
                    ).toList();
        }

        order.setCustomer(customer);
        order.setProducts(products);
        Order savedOrder = orderRepository.save(order);

        return orderDTOMapper.toDTO(savedOrder);
    }

    @Transactional
    public OrderUpdDTO update(Long id, OrderUpdDTO orderUpdDTO) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Chosen Order does not exist!"));

        Customer customer = customerRepository.findById(orderUpdDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("This Customer does not exist: " + orderUpdDTO.getCustomerId()));

        List<Product> products = orderUpdDTO.getProductList();
        if (products == null) {
            products = Collections.emptyList();
        } else {
            products = products.stream()
                    .map(product -> productRepository.findById(product.getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("This Product does not exist: " + product.getProductId()))
                    ).collect(Collectors.toList());
        }

        order.setOrderStatus(orderUpdDTO.getOrderStatus());
        order.setCustomer(customer);
        order.setProducts(products);
        Order updatedOrder = orderRepository.save(order);

        return orderDTOMapper.toDTOUpd(updatedOrder);
    }

    @Transactional
    public boolean delete(Long orderId) throws NotFoundException {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException("This Order does not exist!");
        }
        orderRepository.deleteById(orderId);
        return true;
    }

}
