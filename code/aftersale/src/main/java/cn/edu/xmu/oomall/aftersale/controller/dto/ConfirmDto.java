package cn.edu.xmu.oomall.aftersale.controller.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.aftersale.controller.vo.ConfirmVo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Consignee;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({ConfirmVo.class})
public class ConfirmDto {

    private Boolean confirm;
    private String conclusion;
    private Boolean refundOnly;
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
