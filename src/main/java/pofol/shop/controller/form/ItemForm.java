package pofol.shop.controller.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
public class ItemForm {
    private Long id;
    @NotEmpty(message = "이름은 필수입니다")
    private String itemName;
    @NotEmpty(message = "저자는 필수입니다")
    private String author;
    private String description;
    @Min(value = 0, message = "가격은 0이상이어야 합니다")
    private int price;
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
    private int quantity;
}
