package cn.edu.xmu.oomall.service.mapper.openfeign.po;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Consignee {
    private String name;
    private String mobile;
    private Long regionId;
    private String address;

}
