package mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.mapper.CustomerDTOMapper;
import springApp.model.Customer;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDTOMapperTest {

    private CustomerDTOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CustomerDTOMapper.class);
    }

    @Test
    void testToEntityInc() {
        CustomerIncDTO dto = new CustomerIncDTO();
        dto.setCustomerName("Петр");
        dto.setCustomerSurname("Петров");

        Customer customer = mapper.toEntityInc(dto);

        assertNotNull(customer);
        assertEquals("Петр", customer.getCustomerName());
        assertEquals("Петров", customer.getCustomerSurname());
    }

    @Test
    void testToEntityUpd() {
        CustomerUpdDTO dto = new CustomerUpdDTO();
        dto.setCustomerName("Владислав");
        dto.setCustomerSurname("Капланов");

        Customer customer = mapper.toEntityUpd(dto);

        assertNotNull(customer);
        assertEquals("Владислав", customer.getCustomerName());
        assertEquals("Капланов", customer.getCustomerSurname());
    }

    @Test
    void testToDTO() {
        Customer customer = new Customer();
        customer.setCustomerName("Иван");
        customer.setCustomerSurname("Иванов");

        CustomerOutDTO dto = mapper.toDTO(customer);

        assertNotNull(dto);
        assertEquals("Иван", dto.getCustomerName());
        assertEquals("Иванов", dto.getCustomerSurname());
    }

    @Test
    void testToDTOUpd() {
        Customer customer = new Customer();
        customer.setCustomerName("Петр");
        customer.setCustomerSurname("Петров");

        CustomerUpdDTO dto = mapper.toDTOUpd(customer);

        assertNotNull(dto);
        assertEquals("Петр", dto.getCustomerName());
        assertEquals("Петров", dto.getCustomerSurname());
    }


}
