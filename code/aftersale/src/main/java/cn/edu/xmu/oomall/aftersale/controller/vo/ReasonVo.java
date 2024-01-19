package cn.edu.xmu.oomall.aftersale.controller.vo;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class ReasonVo {
    @JsonProperty(value = "reason")
    @NotNull(message = "仲裁理由不能为空")
    private String reason;

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
