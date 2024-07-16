package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springApp.controller.CustomerController;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.exception.NotFoundException;
import springApp.service.CustomerService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    private final CustomerService customerService = Mockito.mock(CustomerService.class);
    private final CustomerController customerController = new CustomerController(customerService);
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testGetCustomerById() throws Exception {
        CustomerOutDTO customerOutDTO = new CustomerOutDTO(1L, "Иван", "Иванов");

        when(customerService.findById(1L)).thenReturn(customerOutDTO);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Иван"))
                .andExpect(jsonPath("$.customerSurname").value("Иванов"));
    }

    @Test
    public void testGetCustomerByIdNotFound() throws Exception {
        when(customerService.findById(anyLong())).thenThrow(new NotFoundException("Customer not found"));

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<CustomerOutDTO> customerList = Arrays.asList(
                new CustomerOutDTO(1L, "Иван", "Иванов"),
                new CustomerOutDTO(2L, "Константин", "Константинов"),
                new CustomerOutDTO(3L, "Филипп", "Филиппов"),
                new CustomerOutDTO(4L, "Кирилл", "Кириллов"),
                new CustomerOutDTO(5L, "Алексей", "Алексеев")
        );

        when(customerService.findAll()).thenReturn(customerList);

        mockMvc.perform(get("/customers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[1].customerId").value(2L))
                .andExpect(jsonPath("$[2].customerId").value(3L))
                .andExpect(jsonPath("$[3].customerId").value(4L))
                .andExpect(jsonPath("$[4].customerId").value(5L));
    }

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerIncDTO customerIncDTO = new CustomerIncDTO("Иван", "Иванов");
        CustomerOutDTO savedCustomer = new CustomerOutDTO(1L, "Иван", "Иванов");

        when(customerService.save(any(CustomerIncDTO.class))).thenReturn(savedCustomer);

        mockMvc.perform(post("/customers/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerIncDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Иван"))
                .andExpect(jsonPath("$.customerSurname").value("Иванов"));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        CustomerUpdDTO customerUpdDTO = new CustomerUpdDTO(2L, "Иван", "Иванов");

        mockMvc.perform(put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerUpdDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        when(customerService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCustomerNotFound() throws Exception {
        when(customerService.delete(anyLong())).thenThrow(new NotFoundException("Customer not found"));

        mockMvc.perform(delete("/customers/77"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));
    }

}
