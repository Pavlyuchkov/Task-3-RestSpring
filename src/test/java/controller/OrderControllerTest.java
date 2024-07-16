package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springApp.controller.OrderController;
import springApp.dto.order.OrderIncDTO;
import springApp.dto.order.OrderJoinProductOutDTO;
import springApp.dto.order.OrderOutDTO;
import springApp.dto.order.OrderUpdDTO;
import springApp.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testFindAll() throws Exception {
        List<OrderOutDTO> orders = new ArrayList<>();
        orders.add(new OrderOutDTO());

        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindAllById() throws Exception {
        List<OrderOutDTO> orders = new ArrayList<>();
        orders.add(new OrderOutDTO());

        when(orderService.findAllById(anyLong())).thenReturn(orders);

        mockMvc.perform(get("/orders/all/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindAllJoinProduct() throws Exception {
        List<OrderJoinProductOutDTO> orders = new ArrayList<>();
        orders.add(new OrderJoinProductOutDTO());

        when(orderService.findAllJoinProduct()).thenReturn(orders);

        mockMvc.perform(get("/orders/with_product/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testFindById() throws Exception {
        OrderOutDTO order = new OrderOutDTO();

        when(orderService.findById(anyLong())).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testFindByIdNotFound() throws Exception {
        when(orderService.findById(anyLong())).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    public void testFindByIdJoinProduct() throws Exception {
        OrderJoinProductOutDTO order = new OrderJoinProductOutDTO();

        when(orderService.findByIdJoinProduct(anyLong())).thenReturn(order);

        mockMvc.perform(get("/orders/with_product/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testFindByIdJoinProductNotFound() throws Exception {
        when(orderService.findByIdJoinProduct(anyLong())).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(get("/orders/with_product/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        OrderOutDTO order = new OrderOutDTO();
        OrderIncDTO orderIncDTO = new OrderIncDTO();

        when(orderService.save(any(OrderIncDTO.class))).thenReturn(order);

        mockMvc.perform(post("/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderIncDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(order.getOrderId()));
    }

    @Test
    public void testCreateOrderBadRequest() throws Exception {
        OrderIncDTO orderIncDTO = new OrderIncDTO();

        when(orderService.save(any(OrderIncDTO.class))).thenThrow(new RuntimeException("Incorrect Input..."));

        mockMvc.perform(post("/orders/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderIncDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect Input..."));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        OrderUpdDTO orderUpdDTO = new OrderUpdDTO();

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateOrderNotFound() throws Exception {
        OrderUpdDTO orderUpdDTO = new OrderUpdDTO();

        when(orderService.update(anyLong(), any(OrderUpdDTO.class))).thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Order not found"));
    }

    @Test
    public void testUpdateOrderBadRequest() throws Exception {
        OrderUpdDTO orderUpdDTO = new OrderUpdDTO();

        when(orderService.update(anyLong(), any(OrderUpdDTO.class))).thenThrow(new RuntimeException("Incorrect Input..."));

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect Input..."));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        when(orderService.delete(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOrderNotFound() throws Exception {
        when(orderService.delete(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNotFound());
    }

}
