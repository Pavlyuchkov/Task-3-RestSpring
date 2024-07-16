package springApp.dto.order;

import springApp.model.Product;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderUpdDTO {

    @NotEmpty
    private Long orderId;

    @NotEmpty
    private String orderStatus;

    @NotEmpty
    private Long customerId;

    @NotEmpty
    private List<Product> productList;

    public OrderUpdDTO() {
    }

    public OrderUpdDTO(Long orderId, String orderStatus, Long customerId, List<Product> productList) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.productList = productList;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
