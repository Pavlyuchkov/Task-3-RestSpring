package springApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.exception.NotFoundException;
import springApp.mapper.ProductDTOMapper;
import springApp.model.Product;
import springApp.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductDTOMapper productDTOMapper) {
        this.productRepository = productRepository;
        this.productDTOMapper = productDTOMapper;
    }

    @Transactional
    public ProductOutDTO save(ProductIncDTO productIncDTO) {
        Product product = productDTOMapper.toEntityInc(productIncDTO);
        product = productRepository.save(product);
        return productDTOMapper.toDTO(product);
    }

    @Transactional
    public ProductUpdDTO update(ProductUpdDTO productUpdDTO) throws NotFoundException {
        checkProductExist(productUpdDTO.getProductId());
        Product product = productDTOMapper.toEntityUpd(productUpdDTO);
        productRepository.save(product);
        return productDTOMapper.toDTOUpd(product);
    }

    public ProductOutDTO findById(Long productId) throws NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NotFoundException("This Product does not exist!"));
        return productDTOMapper.toDTO(product);
    }

    public List<ProductOutDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long productId) throws NotFoundException {
        checkProductExist(productId);
        productRepository.deleteById(productId);
        return true;
    }

    private void checkProductExist(Long productId) throws NotFoundException {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("This Product does not exist!");
        }
    }


}
