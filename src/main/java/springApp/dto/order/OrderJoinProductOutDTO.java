package springApp.dto.order;

import springApp.dto.product.ProductOutDTO;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderJoinProductOutDTO {

    @NotEmpty
    private Long orderId;

    @NotEmpty
    private String orderStatus;

    @NotEmpty
    private Long customerId;

    @NotEmpty
    private List<ProductOutDTO> productList;

    public OrderJoinProductOutDTO() {
    }

    public OrderJoinProductOutDTO(Long orderId, String orderStatus, Long customerId, List<ProductOutDTO> productList) {
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

    public Long getCustomerId() {
        return customerId;
    }

    public List<ProductOutDTO> getProductList() {
        return productList;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setProductList(List<ProductOutDTO> productList) {
        this.productList = productList;
    }
}
