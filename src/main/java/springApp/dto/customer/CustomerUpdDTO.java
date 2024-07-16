package springApp.dto.customer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CustomerUpdDTO {

    @NotEmpty
    private Long customerId;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Введите Ваше имя!")
    private String customerName;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Введите Вашу фамилию!")
    private String customerSurname;

    public CustomerUpdDTO() {
    }

    public CustomerUpdDTO(Long customerId, String customerName, String customerSurname) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }
}
