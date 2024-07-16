package springApp.dto.customer;

public class CustomerOutDTO {

    private Long customerId;
    private String customerName;
    private String customerSurname;

    public CustomerOutDTO() {
    }

    public CustomerOutDTO(Long customerId, String customerName, String customerSurname) {
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
