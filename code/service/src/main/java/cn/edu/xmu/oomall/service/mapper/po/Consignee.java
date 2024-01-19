package cn.edu.xmu.oomall.service.mapper.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Setter
@Getter
public class Consignee {
    private String name;
    private String mobile;
    private Long regionId;
    private String address;

    public Consignee(String name, String mobile, Long regionId, String address) {
        this.name = name;
        this.mobile = mobile;
        this.regionId = regionId;
        this.address = address;
    }
}
