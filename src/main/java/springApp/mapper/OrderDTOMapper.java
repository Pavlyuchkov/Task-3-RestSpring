package springApp.mapper;

import org.mapstruct.*;
import springApp.dto.order.OrderIncDTO;
import springApp.dto.order.OrderJoinProductOutDTO;
import springApp.dto.order.OrderOutDTO;
import springApp.dto.order.OrderUpdDTO;
import springApp.model.Order;
import springApp.model.Product;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDTOMapper {

    Order toEntityInc(OrderIncDTO orderIncDTO);

    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "productList", source = "products")
    OrderUpdDTO toDTOUpd(Order order);

    @Mapping(target = "customerId", source = "customer.customerId")
    OrderOutDTO toDTO(Order order);

    @Mapping(target = "customerId", source = "customer.customerId")
    @Mapping(target = "productList", source = "products")
    OrderJoinProductOutDTO toDTOJoinProduct(Order order);

}
