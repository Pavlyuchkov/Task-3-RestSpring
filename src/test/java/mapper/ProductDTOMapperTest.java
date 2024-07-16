package mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.mapper.ProductDTOMapper;
import springApp.model.Product;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDTOMapperTest {

    private ProductDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ProductDTOMapper.class);
    }

    @Test
    void testToEntityInc() {
        ProductIncDTO dto = new ProductIncDTO();
        dto.setProductName("Пельмехи");
        dto.setPrice(10L);

        Product product = mapper.toEntityInc(dto);

        assertNotNull(product);
        assertEquals("Пельмехи", product.getProductName());
        assertEquals(10L, product.getPrice());
    }

    @Test
    void testToEntityUpd() {
        ProductUpdDTO dto = new ProductUpdDTO();
        dto.setProductName("Варенички");
        dto.setPrice(10L);

        Product product = mapper.toEntityUpd(dto);

        assertNotNull(product);
        assertEquals("Варенички", product.getProductName());
        assertEquals(10L, product.getPrice());
    }

    @Test
    void testToDTO() {
        Product product = new Product();
        product.setProductName("Варенички");
        product.setPrice(10L);

        ProductOutDTO dto = mapper.toDTO(product);

        assertNotNull(dto);
        assertEquals("Варенички", dto.getProductName());
        assertEquals(10L, dto.getPrice());
    }

    @Test
    void testToDTOUpd() {
        Product product = new Product();
        product.setProductName("Пельмешки");
        product.setPrice(10L);

        ProductUpdDTO dto = mapper.toDTOUpd(product);

        assertNotNull(dto);
        assertEquals("Пельмешки", dto.getProductName());
        assertEquals(10L, dto.getPrice());
    }


}
