package cn.edu.xmu.oomall.service.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.dao.bo.ProductService;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "productservice")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CopyFrom({ProductService.class})
public class ProductServicePo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long serviceId;
    private Long maintainerId;
    private Long regionId;
    private Long shopId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Byte status;    //0-VALID 1-SUSPENDED
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

    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
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

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public LocalDateTime getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime)
    {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }

    public Byte getStatus()
    {
        return status;
    }

    public void setStatus(Byte status)
    {
        this.status = status;
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