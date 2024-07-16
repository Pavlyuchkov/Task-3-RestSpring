package springApp.dto.product;

import javax.validation.constraints.NotEmpty;

public class ProductIncDTO {

    @NotEmpty
    private String productName;

    @NotEmpty
    private Long price;

    public ProductIncDTO() {
    }

    public ProductIncDTO(String productName, Long price) {
        this.productName = productName;
        this.price = price;
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
