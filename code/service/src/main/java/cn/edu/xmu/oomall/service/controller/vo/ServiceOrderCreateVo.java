package cn.edu.xmu.oomall.service.controller.vo;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/*
{
  "errno": 0,
  "errmsg": "成功",
  "data": {
    "id": 0,
    "type": 0,
    "consignee": {
    	"name": "string",
    	"mobile": "string",
    	"regionID": 0,
    	"address": "string"
  	}
  }
}
 */

public class ServiceOrderCreateVo {

    @NotNull(message = "type不能为空")
    private Byte type;

    @NotNull(message = "consignee不能为空")
    private Consignee consignee;

    // type的get和set方法
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    // consignee的get和set方法
    public Consignee getConsignee() {
        return consignee;
    }

    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }

    // 内部类Consignee
    public static class Consignee {
        @NotBlank(message = "name不能为空")
        private String name;

        @NotBlank(message = "mobile不能为空")
        private String mobile;

        @NotNull(message = "regionID不能为空")
        private Long regionID;

        @NotBlank(message = "address不能为空")
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
