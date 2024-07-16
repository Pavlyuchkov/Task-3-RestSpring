package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.exception.NotFoundException;
import springApp.mapper.ProductDTOMapper;
import springApp.model.Product;
import springApp.repository.ProductRepository;
import springApp.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Testcontainers
public class ProductServiceTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductDTOMapper productDTOMapper;

    @InjectMocks
    private ProductService productService;

    private static HikariDataSource dataSource;

    @BeforeAll
    public static void init() {
        postgreSQLContainer.start();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        config.setUsername(postgreSQLContainer.getUsername());
        config.setPassword(postgreSQLContainer.getPassword());
        dataSource = new HikariDataSource(config);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productRepository, productDTOMapper);
    }

    @AfterAll
    public static void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
        postgreSQLContainer.stop();
    }

    @Test
    public void testSaveProduct() {
        ProductIncDTO productIncDTO = new ProductIncDTO("Водяра", 100L);
        Product product = new Product();
        product.setProductName("Водяра");
        product.setPrice(100L);
        ProductOutDTO productOutDTO = new ProductOutDTO(1L, "Водяра", 100L);

        when(productDTOMapper.toEntityInc(any(ProductIncDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productDTOMapper.toDTO(any(Product.class))).thenReturn(productOutDTO);

        ProductOutDTO result = productService.save(productIncDTO);

        assertNotNull(result);
        assertEquals("Водяра", result.getProductName());
        assertEquals(100L, result.getPrice());
    }

    @Test
    public void testUpdateProduct() throws NotFoundException {
        ProductUpdDTO productUpdDTO = new ProductUpdDTO(3L, "Вискарик", 100L);
        Product product = new Product();
        product.setProductId(3L);
        product.setProductName("Вискарик");
        product.setPrice(100L);
        ProductOutDTO productOutDTO = new ProductOutDTO(3L, "Вискарик", 100L);

        when(productRepository.existsById(3L)).thenReturn(true);
        when(productDTOMapper.toEntityUpd(any(ProductUpdDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productDTOMapper.toDTOUpd(any(Product.class))).thenReturn(productUpdDTO);

        ProductUpdDTO result = productService.update(productUpdDTO);

        assertNotNull(result);
        assertEquals(3L, result.getProductId());
        assertEquals("Вискарик", result.getProductName());
        assertEquals(100L, result.getPrice());
    }

    @Test
    public void testFindProductById() throws NotFoundException {
        Product product = new Product();
        product.setProductId(5L);
        product.setProductName("Кофе");
        product.setPrice(15L);
        ProductOutDTO productOutDTO = new ProductOutDTO(5L, "Кофе", 15L);

        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(productDTOMapper.toDTO(any(Product.class))).thenReturn(productOutDTO);

        ProductOutDTO result = productService.findById(5L);

        assertNotNull(result);
        assertEquals("Кофе", result.getProductName());
        assertEquals(15L, result.getPrice());
    }

    @Test
    public void testFindAllProducts() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setProductName("Хлеб");
        product1.setPrice(2L);

        Product product2 = new Product();
        product2.setProductId(2L);
        product2.setProductName("Молоко");
        product2.setPrice(3L);

        ProductOutDTO productOutDTO1 = new ProductOutDTO(1L, "Хлеб", 2L);
        ProductOutDTO productOutDTO2 = new ProductOutDTO(2L, "Молоко", 3L);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productDTOMapper.toDTO(product1)).thenReturn(productOutDTO1);
        when(productDTOMapper.toDTO(product2)).thenReturn(productOutDTO2);

        List<ProductOutDTO> result = productService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Хлеб", result.get(0).getProductName());
        assertEquals(2L, result.get(0).getPrice());
        assertEquals("Молоко", result.get(1).getProductName());
        assertEquals(3L, result.get(1).getPrice());
    }

    @Test
    public void testDeleteProduct() throws NotFoundException {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.delete(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            productService.delete(1L);
        });

        assertEquals("This Product does not exist!", exception.getMessage());
    }


}
