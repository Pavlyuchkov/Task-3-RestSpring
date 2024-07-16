package springApp.dto.customer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CustomerIncDTO {

    @NotEmpty
    @Size(min = 3, max = 20, message = "Введите Ваше имя!")
    private String customerName;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Введите Вашу фамилию!")
    private String customerSurname;

    public CustomerIncDTO() {
    }

    public CustomerIncDTO(String customerName, String customerSurname) {
        this.customerName = customerName;
        this.customerSurname = customerSurname;
    }

    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }
}
