package springApp.mapper;

import org.mapstruct.MappingConstants;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.model.Customer;
import org.mapstruct.Mapper;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerDTOMapper {

    Customer toEntityInc(CustomerIncDTO customerIncDTO);

    Customer toEntityUpd(CustomerUpdDTO customerUpdDTO);

    CustomerOutDTO toDTO(Customer customer);

    CustomerUpdDTO toDTOUpd(Customer customer);

}