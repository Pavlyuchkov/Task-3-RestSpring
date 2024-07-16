package springApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import springApp.dto.customer.CustomerIncDTO;
import springApp.dto.customer.CustomerOutDTO;
import springApp.dto.customer.CustomerUpdDTO;
import springApp.exception.NotFoundException;
import springApp.mapper.CustomerDTOMapper;
import springApp.model.Customer;
import org.springframework.stereotype.Service;
import springApp.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper customerDTOMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerDTOMapper customerDTOMapper) {
        this.customerRepository = customerRepository;
        this.customerDTOMapper = customerDTOMapper;
    }

    @Transactional
    public CustomerOutDTO save(CustomerIncDTO customerIncDTO) {
        Customer customer = customerDTOMapper.toEntityInc(customerIncDTO);
        customer = customerRepository.save(customer);
        return customerDTOMapper.toDTO(customer);
    }

    @Transactional
    public CustomerUpdDTO update(CustomerUpdDTO customerUpdDTO) throws NotFoundException {
        checkCustomerExist(customerUpdDTO.getCustomerId());
        Customer customer = customerDTOMapper.toEntityUpd(customerUpdDTO);
        customerRepository.save(customer);
        return customerDTOMapper.toDTOUpd(customer);
    }

    public CustomerOutDTO findById(Long customerId) throws NotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new NotFoundException("This Customer does not exist!"));
        return customerDTOMapper.toDTO(customer);
    }

    public List<CustomerOutDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long customerId) throws NotFoundException {
        checkCustomerExist(customerId);
        customerRepository.deleteById(customerId);
        return true;
    }

    private void checkCustomerExist(Long customerId) throws NotFoundException {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("This Customer does not exist!");
        }
    }
}