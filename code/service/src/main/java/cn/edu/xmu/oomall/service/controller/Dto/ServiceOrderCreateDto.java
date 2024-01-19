package cn.edu.xmu.oomall.service.controller.Dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.controller.vo.ServiceOrderCreateVo;
import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ServiceOrderCreateVo.class})
public class ServiceOrderCreateDto {

    private Byte type;


    private ServiceOrderCreateVo.Consignee consignee;

    // type的get和set方法
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    // consignee的get和set方法
    public ServiceOrderCreateVo.Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(ServiceOrderCreateVo.Consignee consignee) {
        this.consignee = consignee;
    }

    // 内部类Consignee
    public static class Consignee {

        private String name;


        private String mobile;


        private Long regionID;


        private String address;

        // name的get和set方法
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // mobile的get和set方法
        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        // regionID的get和set方法
        public Long getRegionID() {
            return regionID;
        }

        public void setRegionID(Long regionID) {
            this.regionID = regionID;
        }

        // address的get和set方法
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
