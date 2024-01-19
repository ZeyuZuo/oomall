package cn.edu.xmu.oomall.service.controller.Dto;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.controller.vo.MaintainerMessageVo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({MaintainerMessageVo.class})
public class MaintainerMessageDto {
    private String maintainerName;
    private String maintainerMobile;
    private Long regionId;
    private String address;

}
