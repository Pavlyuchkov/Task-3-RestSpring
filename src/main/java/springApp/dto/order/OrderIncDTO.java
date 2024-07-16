package springApp.dto.order;

import springApp.model.Product;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class OrderIncDTO {

    @NotEmpty
    private String orderStatus;

    @NotEmpty
    private Long customerId;

    @NotEmpty
    private List<Product> productList;

    public OrderIncDTO() {
    }

    public OrderIncDTO(String orderStatus, Long customerId, List<Product> productList) {
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.productList = productList;
    }

    public String getOrderStatus() {
        return orderStatus;
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

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
