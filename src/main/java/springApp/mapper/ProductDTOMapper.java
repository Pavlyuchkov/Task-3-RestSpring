package springApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import springApp.dto.product.ProductIncDTO;
import springApp.dto.product.ProductOutDTO;
import springApp.dto.product.ProductUpdDTO;
import springApp.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductDTOMapper {

    Product toEntityInc(ProductIncDTO productIncDTO);

    Product toEntityUpd(ProductUpdDTO productUpdDTO);

    ProductOutDTO toDTO(Product product);

    ProductUpdDTO toDTOUpd(Product product);
}
