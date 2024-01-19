package cn.edu.xmu.oomall.aftersale.mapper.po;

import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.javaee.core.aop.CopyFrom;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "aftersale")
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({AfterSale.class})
public class AfterSalePo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Byte type;
    private Byte status;
    private String reason;
    private String conclusion;
    private Integer quantity;
    private Boolean isDisputing;
    private String name;
    private String mobile;
    private Long regionId;
    private String address;
    private String afterSaleSn;

    private Long customerId;
    private Long shopId;
    private Long productId;
    private Long orderItemId;
    private Long sendOffExpressId;
    private Long sendBackExpressId;
    private Long refundId;
    private Long serviceOrderId;
    // 创建者id
    private Long creatorId;
    // 创建者
    private String creatorName;
    // 修改者id
    private Long modifierId;
    // 修改者
    private String modifierName;
    // 创建时间
    private LocalDateTime gmtCreate;
    // 修改时间
    private LocalDateTime gmtModified;

    //所有属性的getter和setter

    public String getAfterSaleSn()
    {
        return afterSaleSn;
    }
    public void setAfterSaleSn(String afterSaleSn)
    {
        this.afterSaleSn = afterSaleSn;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Byte getType()
    {
        return type;
    }

    public void setType(Byte type)
    {
        this.type = type;
    }

    public Byte getStatus()
    {
        return status;
    }

    public void setStatus(Byte status)
    {
        this.status = status;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public String getConclusion()
    {
        return conclusion;
    }

    public void setConclusion(String conclusion)
    {
        this.conclusion = conclusion;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public Long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Boolean getIsDisputing() {
        return isDisputing;
    }
    public void setIsDisputing(Boolean isDisputing) {
        this.isDisputing = isDisputing;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getOrderItemId()
    {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId)
    {
        this.orderItemId = orderItemId;
    }

    public Long getSendOffExpressId()
    {
        return sendOffExpressId;
    }

    public void setSendOffExpressId(Long sendOffExpressId)
    {
        this.sendOffExpressId = sendOffExpressId;
    }

    public Long getSendBackExpressId()
    {
        return sendBackExpressId;
    }

    public void setSendBackExpressId(Long sendBackExpressId)
    {
        this.sendBackExpressId = sendBackExpressId;
    }

    public Long getRefundId()
    {
        return refundId;
    }

    public void setRefundId(Long refundId)
    {
        this.refundId = refundId;
    }

    public Long getServiceOrderId()
    {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId)
    {
        serviceOrderId = serviceOrderId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public String getCreatorName()
    {
        return creatorName;
    }

    public void setCreatorName(String creatorName)
    {
        this.creatorName = creatorName;
    }

    public Long getModifierId()
    {
        return modifierId;
    }

    public void setModifierId(Long modifierId)
    {
        this.modifierId = modifierId;
    }

    public String getModifierName()
    {
        return modifierName;
    }

    public void setModifierName(String modifierName)
    {
        this.modifierName = modifierName;
    }

    public LocalDateTime getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified()
    {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified)
    {
        this.gmtModified = gmtModified;
    }
}
