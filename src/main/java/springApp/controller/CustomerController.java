package springApp.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springApp.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            CustomerOutDTO customerDto = customerService.findById(id);
            return new ResponseEntity<>(customerDto, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerOutDTO>> getAllCustomers() {
        List<CustomerOutDTO> customerOutDtoList = customerService.findAll();
        return new ResponseEntity<>(customerOutDtoList, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerIncDTO customerIncDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        else  {
            CustomerOutDTO savedCustomer = customerService.save(customerIncDTO);
            return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerUpdDTO customerUpdDTO, @PathVariable Long id) {
        try {
            customerUpdDTO.setCustomerId(id);
            customerService.update(customerUpdDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}