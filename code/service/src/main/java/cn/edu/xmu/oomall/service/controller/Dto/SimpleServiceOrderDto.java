package cn.edu.xmu.oomall.service.controller.Dto;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({ServiceOrder.class})
public class SimpleServiceOrderDto {
    /*    "id": "Long",
          "type": "Byte",
          "consignee": "string"
    */
    private Long id;
    private Byte type;
    private String customerName;

    // id的get和set方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // type的get和set方法
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    // consignee的get和set方法
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}