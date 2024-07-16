package springApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.exception.NotFoundException;
import springApp.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductOutDTO productOutDTO = productService.findById(id);
            return new ResponseEntity<>(productOutDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductOutDTO>> getAllProducts() {
        List<ProductOutDTO> productOutDTOList = productService.findAll();
        return new ResponseEntity<>(productOutDTOList, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@RequestBody ProductIncDTO productIncDTO) {
        try {
            ProductOutDTO savedProduct = productService.save(productIncDTO);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdDTO productUpdDTO, @PathVariable Long id) {
        try {
            productUpdDTO.setProductId(id);
            productService.update(productUpdDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
