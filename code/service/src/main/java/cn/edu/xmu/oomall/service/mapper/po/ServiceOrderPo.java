package cn.edu.xmu.oomall.service.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "serviceorder")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({ServiceOrder.class})
public class ServiceOrderPo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long maintainerId;
    private Long regionId;
    private Long productServiceId;
    private Long shopId;
    private Long customerId;
    private Long sendExpressId;
    private String sendExpressBillCode;
    private Long aftersalesId;
    private String serviceOrderSn;
    private Byte type;
    private Byte status;
    private Byte result;
    private String description;
    private String customerAddress;
    private String customerPhone;
    private String customerName;
    private String maintainerAddress;
    private String maintainerName;
    private String maintainerPhone;
    private Byte serviceType;
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

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        id = id;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public Long getMaintainerId()
    {
        return maintainerId;
    }

    public void setMaintainerId(Long maintainerId)
    {
        this.maintainerId = maintainerId;
    }

    public Long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public Long getProductServiceId()
    {
        return productServiceId;
    }

    public void setProductServiceId(Long productServiceId)
    {
        this.productServiceId = productServiceId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Long getSendExpressId()
    {
        return sendExpressId;
    }

    public void setSendExpressId(Long sendExpressId)
    {
        this.sendExpressId = sendExpressId;
    }

    public String getSendExpressBillCode()
    {
        return sendExpressBillCode;
    }

    public void setSendExpressBillCode(String sendExpressBillCode)
    {
        this.sendExpressBillCode = sendExpressBillCode;
    }

    public Long getAftersalesId()
    {
        return aftersalesId;
    }

    public void setAftersalesId(Long aftersalesId)
    {
        this.aftersalesId = aftersalesId;
    }

    public String getServiceOrderSn()
    {
        return serviceOrderSn;
    }

    public void setServiceOrderSn(String serviceOrderSn)
    {
        this.serviceOrderSn = serviceOrderSn;
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

    public Byte getResult()
    {
        return result;
    }

    public void setResult(Byte result)
    {
        this.result = result;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCustomerAddress()
    {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone()
    {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone)
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getMaintainerAddress() {
        return maintainerAddress;
    }

    public void setMaintainerAddress(String maintainerAddress) {
        this.maintainerAddress = maintainerAddress;
    }

    public String getMaintainerName()
    {
        return maintainerName;
    }

    public void setMaintainerName(String maintainerName)
    {
        this.maintainerName = maintainerName;
    }

    public String getMaintainerPhone()
    {
        return maintainerPhone;
    }

    public void setMaintainerPhone(String maintainerPhone)
    {
        this.maintainerPhone = maintainerPhone;
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

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public Byte getServiceType() {
        return serviceType;
    }
}