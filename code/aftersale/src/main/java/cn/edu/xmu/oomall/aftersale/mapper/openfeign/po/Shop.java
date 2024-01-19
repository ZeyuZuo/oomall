package cn.edu.xmu.oomall.aftersale.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class Shop {
    private Long id;
    private Byte status;
    private Consignee consignee;
    private String name;
}
