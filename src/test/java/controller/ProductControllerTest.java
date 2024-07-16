package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springApp.controller.ProductController;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.exception.NotFoundException;
import springApp.service.ProductService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetProductById() throws Exception {
        ProductOutDTO productOutDTO = new ProductOutDTO(4L, "Чай", 10L);

        when(productService.findById(4L)).thenReturn(productOutDTO);

        mockMvc.perform(get("/products/4"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(4L))
                .andExpect(jsonPath("$.productName").value("Чай"))
                .andExpect(jsonPath("$.price").value(10L));
    }

    @Test
    public void testGetProductByIdNotFound() throws Exception {
        when(productService.findById(100L)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(get("/products/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<ProductOutDTO> productList = Arrays.asList(
                new ProductOutDTO(1L, "Молоко", 2L),
                new ProductOutDTO(2L, "Хлеб", 3L),
                new ProductOutDTO(3L, "Яблоки", 5L),
                new ProductOutDTO(4L, "Чай", 10L),
                new ProductOutDTO(5L, "Кофе", 15L),
                new ProductOutDTO(6L, "Говядина", 20L),
                new ProductOutDTO(7L, "Форель", 30L)
        );

        when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get("/products/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Молоко"))
                .andExpect(jsonPath("$[0].price").value(2L))
                .andExpect(jsonPath("$[1].productId").value(2L))
                .andExpect(jsonPath("$[1].productName").value("Хлеб"))
                .andExpect(jsonPath("$[1].price").value(3L))
                .andExpect(jsonPath("$[2].productId").value(3L))
                .andExpect(jsonPath("$[2].productName").value("Яблоки"))
                .andExpect(jsonPath("$[2].price").value(5L))
                .andExpect(jsonPath("$[3].productId").value(4L))
                .andExpect(jsonPath("$[3].productName").value("Чай"))
                .andExpect(jsonPath("$[3].price").value(10L))
                .andExpect(jsonPath("$[4].productId").value(5L))
                .andExpect(jsonPath("$[4].productName").value("Кофе"))
                .andExpect(jsonPath("$[4].price").value(15L))
                .andExpect(jsonPath("$[5].productId").value(6L))
                .andExpect(jsonPath("$[5].productName").value("Говядина"))
                .andExpect(jsonPath("$[5].price").value(20L))
                .andExpect(jsonPath("$[6].productId").value(7L))
                .andExpect(jsonPath("$[6].productName").value("Форель"))
                .andExpect(jsonPath("$[6].price").value(30));
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductIncDTO productIncDTO = new ProductIncDTO("Сметана", 9L);
        ProductOutDTO productOutDTO = new ProductOutDTO(8L, "Сметана", 9L);

        when(productService.save(any(ProductIncDTO.class))).thenReturn(productOutDTO);

        mockMvc.perform(post("/products/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productIncDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(8L))
                .andExpect(jsonPath("$.productName").value("Сметана"))
                .andExpect(jsonPath("$.price").value(9L));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        ProductUpdDTO productUpdDTO = new ProductUpdDTO(6L, "Салат", 6L);

        mockMvc.perform(put("/products/6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProductNotFound() throws Exception {
        ProductUpdDTO productUpdDTO = new ProductUpdDTO(100L, "Водяра", 15L);

        when(productService.update(any(ProductUpdDTO.class))).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(put("/products/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

    @Test
    public void testUpdateProductBadRequest() throws Exception {
        ProductUpdDTO productUpdDTO = new ProductUpdDTO();
        productUpdDTO.setProductId(1L);
        productUpdDTO.setProductName("Вискарик");
        productUpdDTO.setPrice(20L);

        mockMvc.perform(put("/products/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.delete(4L)).thenReturn(true);

        mockMvc.perform(delete("/products/4"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProductNotFound() throws Exception {
        when(productService.delete(100L)).thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(delete("/products/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));
    }

}
