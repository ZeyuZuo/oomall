package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.CustomerDao;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.ShopDao;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.aftersale.mapper.po.DisputePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({DisputePo.class})
public class Dispute extends OOMallObject implements Serializable {
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
    private AfterSale afterSale;
    private AfterSaleDao afterSaleDao;

    //customer
    private Long customerId;
    private Customer customer;
    private CustomerDao customerDao;

    //shop
    private Long shopId;
    private Shop shop;
    private ShopDao shopDao;

    /**
     * 共五种状态
     */
    //待审核
    @ToString.Exclude
    @JsonIgnore
    public static final Byte PRE_AUDIT = 0;
    //待应诉
    @ToString.Exclude
    @JsonIgnore
    public static final Byte PRE_SUIT = 1;
    //处理中
    @ToString.Exclude
    @JsonIgnore
    public static final Byte GOING = 2;
    //已完成
    @ToString.Exclude
    @JsonIgnore
    public static final Byte FINISHED = 3;
    //被取消
    @ToString.Exclude
    @JsonIgnore
    public static final Byte CANCELLED = 4;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<>() {
        {
            put(PRE_AUDIT, "待审核");
            put(PRE_SUIT, "待应诉");
            put(GOING, "处理中");
            put(FINISHED, "已完成");
            put(CANCELLED, "被取消");
        }
    };

    /**
     * 允许的状态迁移
     */
    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(PRE_AUDIT, new HashSet<>() {
                {
                    add(PRE_SUIT);
                    add(CANCELLED); // 顾客取消
                }
            });
            put(PRE_SUIT, new HashSet<>() {
                {
                    add(GOING);
                    add(CANCELLED); // 商户取消
                }
            });
            put(GOING, new HashSet<>() {
                {
                    add(FINISHED);
                    add(CANCELLED);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     * @param status
     * @return
     */
    public boolean allowStatus(Byte status) {
        boolean ret = false;

        if (null != status && null != this.status) {
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (null != allowStatusSet) {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

    public Dispute(AfterSale afterSale, String reason){
        // TODO
        this.C_Description = reason;
        this.status = PRE_AUDIT;
        this.applyDate = LocalDateTime.now();
        this.afterSaleId = afterSale.getId();
        this.customer = afterSale.getCustomer();
        this.shop = afterSale.getShop();
    }

    //所有属性的getter和setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getC_Description() {
        return C_Description;
    }
    public void setC_Description(String c_Description) {
        C_Description = c_Description;
    }

    public String getS_Description() {
        return S_Description;
    }
    public void setS_Description(String s_Description) {
        S_Description = s_Description;
    }

    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getResult() {
        return result;
    }
    public void setResult(Byte result) {
        this.result = result;
    }

    public LocalDateTime getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(LocalDateTime acceptDate) {
        this.acceptDate = acceptDate;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    //customer
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        if(this.customer == null){
            this.customer = customerDao.findById(customerId);
        }
        return this.customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //shop
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Shop getShop() {
        if(this.shop == null){
            this.shop = shopDao.findById(shopId);
        }
        return this.shop;
    }
    public void setShop(Shop shop) {
        this.shop = shop;
    }
    //afterSale
    public Long getAfterSaleId() {
        return afterSaleId;
    }
    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
    public AfterSale getAfterSale() {
        if(this.afterSale == null){
            this.afterSale = afterSaleDao.findById(afterSaleId);
        }
        return this.afterSale;
    }
    public void setAfterSale(AfterSale afterSale) {
        this.afterSale = afterSale;
    }

    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }
}


