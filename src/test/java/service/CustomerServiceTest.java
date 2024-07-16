package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.model.Customer;
import springApp.exception.NotFoundException;
import springApp.mapper.CustomerDTOMapper;
import springApp.repository.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import springApp.service.CustomerService;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerDTOMapper customerDTOMapper;

    @InjectMocks
    private CustomerService customerService;

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
        customerService = new CustomerService(customerRepository, customerDTOMapper);
    }

    @AfterAll
    public static void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testSave() {
        CustomerIncDTO customerIncDTO = new CustomerIncDTO();
        Customer customer = new Customer();
        CustomerOutDTO customerOutDTO = new CustomerOutDTO();

        when(customerDTOMapper.toEntityInc(customerIncDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerDTOMapper.toDTO(customer)).thenReturn(customerOutDTO);

        CustomerOutDTO result = customerService.save(customerIncDTO);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
        verify(customerDTOMapper, times(1)).toDTO(customer);
    }

    @Test
    public void testUpdate() throws NotFoundException {
        CustomerUpdDTO customerUpdDTO = new CustomerUpdDTO();
        customerUpdDTO.setCustomerId(1L);
        Customer customer = new Customer();
        CustomerUpdDTO updatedCustomerDTO = new CustomerUpdDTO();

        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(customerDTOMapper.toEntityUpd(customerUpdDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerDTOMapper.toDTOUpd(customer)).thenReturn(updatedCustomerDTO);

        CustomerUpdDTO result = customerService.update(customerUpdDTO);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(customer);
        verify(customerDTOMapper, times(1)).toDTOUpd(customer);
    }

    @Test
    public void testUpdateNotFound() {
        CustomerUpdDTO customerUpdDTO = new CustomerUpdDTO();
        customerUpdDTO.setCustomerId(1L);

        when(customerRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.update(customerUpdDTO));
    }

    @Test
    public void testFindById() throws NotFoundException {
        Long customerId = 1L;
        Customer customer = new Customer();
        CustomerOutDTO customerOutDTO = new CustomerOutDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerDTOMapper.toDTO(customer)).thenReturn(customerOutDTO);

        CustomerOutDTO result = customerService.findById(customerId);

        assertNotNull(result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerDTOMapper, times(1)).toDTO(customer);
    }

    @Test
    public void testFindByIdNotFound() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.findById(customerId));
    }

    @Test
    public void testFindAll() {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());
        List<CustomerOutDTO> customerOutDTOs = customers.stream()
                .map(customer -> new CustomerOutDTO())
                .toList();

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerDTOMapper.toDTO(any(Customer.class))).thenReturn(new CustomerOutDTO());

        List<CustomerOutDTO> result = customerService.findAll();

        assertEquals(2, result.size());
        verify(customerRepository, times(1)).findAll();
        verify(customerDTOMapper, times(customers.size())).toDTO(any(Customer.class));
    }

    @Test
    public void testDelete() throws NotFoundException {
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(true);

        boolean result = customerService.delete(customerId);

        assertTrue(result);
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    public void testDeleteNotFound() {
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerService.delete(customerId));
    }
}
