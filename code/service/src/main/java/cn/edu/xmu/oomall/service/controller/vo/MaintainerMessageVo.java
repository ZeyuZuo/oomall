package cn.edu.xmu.oomall.service.controller.vo;

import cn.edu.xmu.javaee.core.validation.NewGroup;
import cn.edu.xmu.javaee.core.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

/*
   {
        "maintainerName": "string",
        "maintainerMobile": "string"
   }
 */

@Setter
@Getter
public class MaintainerMessageVo {
    @JsonProperty(value = "maintainerName")
    @NotNull(message = "maintainerName不能为空")
    private String maintainerName;

    @JsonProperty(value = "maintainerMobile")
    @NotBlank(message = "maintainerMobile不能为空")
    private String maintainerMobile;

    @JsonProperty(value = "regionId")
    @NotNull(message = "regionId不能为空")
    private Long regionId;

    @JsonProperty(value = "address")
    @NotBlank(message = "address不能为空")
    private String address;


}


