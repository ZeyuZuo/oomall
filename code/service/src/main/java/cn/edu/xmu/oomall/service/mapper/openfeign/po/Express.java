package cn.edu.xmu.oomall.service.mapper.openfeign.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class Express {
    private Long id;
    private String billCode;
    private Byte status;
    private Consignee sender;
    private Consignee receiver;
}
