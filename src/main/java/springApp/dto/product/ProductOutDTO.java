package springApp.dto.product;

import javax.validation.constraints.NotEmpty;

public class ProductOutDTO {

    @NotEmpty
    private Long productId;

    @NotEmpty
    private String productName;

    @NotEmpty
    private Long price;

    public ProductOutDTO(Long productId, String productName, Long price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public ProductOutDTO() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
