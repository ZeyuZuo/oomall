package cn.edu.xmu.oomall.aftersale.controller.vo;

import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Consignee;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class ConfirmVo {
    @JsonProperty(value = "confirm")
    @NotNull(message = "confirm不能为空")
    private Boolean confirm;
    @JsonProperty(value = "conclusion")
    @NotBlank(message = "conclusion不能为空")
    private String conclusion;
    @JsonProperty(value = "refundOnly")
    @NotNull(message = "refundOnly不能为空")
    private Boolean refundOnly;
    @JsonProperty(value = "consignee")
    @NotNull(message = "consignee不能为空")
    private Consignee consignee;

    public Boolean getConfirm() {
        return confirm;
    }
    public String getConclusion() {
        return conclusion;
    }
    public Boolean getRefundOnly() {
        return refundOnly;
    }
    public Consignee getConsignee() {
        return consignee;
    }
    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
    public void setRefundOnly(Boolean refundOnly) {
        this.refundOnly = refundOnly;
    }
    public void setConsignee(Consignee consignee) {
        this.consignee = consignee;
    }
}
