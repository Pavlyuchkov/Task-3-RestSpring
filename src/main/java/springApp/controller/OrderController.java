package springApp.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springApp.dto.order.OrderIncDTO;
import springApp.dto.order.OrderJoinProductOutDTO;
import springApp.dto.order.OrderOutDTO;
import springApp.dto.order.OrderUpdDTO;
import springApp.exception.NotFoundException;
import springApp.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderOutDTO>> findAll() {
        List<OrderOutDTO> orderOutDTOList = orderService.findAll();
        return new ResponseEntity<>(orderOutDTOList, HttpStatus.OK);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<OrderOutDTO>> findAllById(@PathVariable Long id) {
        List<OrderOutDTO> orderOutDTOList = orderService.findAllById(id);
        return new ResponseEntity<>(orderOutDTOList, HttpStatus.OK);
    }

    @GetMapping("/with_product/all")
    public ResponseEntity<List<OrderJoinProductOutDTO>> findAllJoinProduct() {
        List<OrderJoinProductOutDTO> orderJoinProductOutDTO = orderService.findAllJoinProduct();
        return new ResponseEntity<>(orderJoinProductOutDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            OrderOutDTO orderOutDTO = orderService.findById(id);
            return new ResponseEntity<>(orderOutDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/with_product/{id}")
    public ResponseEntity<?> findByIdJoinProduct(@PathVariable Long id) {
        try {
            OrderJoinProductOutDTO orderJoinProductOutDTO = orderService.findByIdJoinProduct(id);
            return new ResponseEntity<>(orderJoinProductOutDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createOrder(@RequestBody OrderIncDTO orderIncDTO) {
        try {
            OrderOutDTO savedOrder = orderService.save(orderIncDTO);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@RequestBody OrderUpdDTO orderUpdDTO, @PathVariable Long id) {
        try {
            orderUpdDTO.setOrderId(id);
            orderService.update(id, orderUpdDTO);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            if (orderService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
