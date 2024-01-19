package cn.edu.xmu.oomall.aftersale.mapper.openfeign.po;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class Refund {
    private Long id;
    private Long amount;
    private Long divAmount;
    private Byte status;
}
