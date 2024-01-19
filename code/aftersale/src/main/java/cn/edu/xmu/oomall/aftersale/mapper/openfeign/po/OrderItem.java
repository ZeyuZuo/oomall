package cn.edu.xmu.oomall.aftersale.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class OrderItem {
    private Long id;
    private Long orderId;
    private String name;
    private Integer quantity;
    private Long price;
    private Long discountPrice;
    private Long paymentId;
    private Long amount;
    private Long divAmount;
}
