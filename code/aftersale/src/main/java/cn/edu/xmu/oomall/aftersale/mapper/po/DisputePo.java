package cn.edu.xmu.oomall.aftersale.mapper.po;

import cn.edu.xmu.oomall.aftersale.dao.bo.Dispute;
import cn.edu.xmu.javaee.core.aop.CopyFrom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "dispute")
@ToString
@CopyFrom({Dispute.class})
public class DisputePo
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String C_Description;
    private String S_Description;
    private Byte status;
    private Byte result;//仲裁结果 0不同意 1同意
    private LocalDateTime finishDate;
    private LocalDateTime acceptDate;
    private LocalDateTime applyDate;
    private LocalDateTime replyDate;

    //afterSale
    private Long afterSaleId;

    //customer
    private Long customerId;

    //shop
    private Long shopId;
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
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getC_Description()
    {
        return C_Description;
    }

    public void setC_Description(String c_Description)
    {
        C_Description = c_Description;
    }

    public String getS_Description()
    {
        return S_Description;
    }

    public void setS_Description(String s_Description)
    {
        S_Description = s_Description;
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

    public LocalDateTime getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime FinishDate)
    {
        this.finishDate = finishDate;
    }

    public LocalDateTime getAcceptDate()
    {
        return acceptDate;
    }

    public void setAcceptDate(LocalDateTime acceptDate)
    {
        this.acceptDate = acceptDate;
    }

    public LocalDateTime getReplyDate()
    {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate)
    {
        this.replyDate = replyDate;
    }

    //customer
    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }


    //shop
    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    //afterSale
    public Long getAfterSaleId()
    {
        return afterSaleId;
    }

    public void setAfterSaleId(Long afterSaleId)
    {
        this.afterSaleId = afterSaleId;
    }


    public LocalDateTime getApplyDate()
    {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate)
    {
        this.applyDate = applyDate;
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
