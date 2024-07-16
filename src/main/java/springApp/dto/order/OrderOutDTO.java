package springApp.dto.order;

import javax.validation.constraints.NotEmpty;

public class OrderOutDTO {

    @NotEmpty
    private Long orderId;

    @NotEmpty
    private String orderStatus;

    @NotEmpty
    private Long customerId;


    public OrderOutDTO() {
    }

    public OrderOutDTO(Long orderId, String orderStatus, Long customerId) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }


    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

}
