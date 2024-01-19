package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.dto.ConfirmDto;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.DisputeDao;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.*;
import cn.edu.xmu.oomall.aftersale.mapper.po.AfterSalePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import static com.alibaba.druid.sql.ast.SQLPartitionValue.Operator.List;


@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({AfterSalePo.class})
public abstract class AfterSale extends OOMallObject implements Serializable {

    protected Byte type; // 售后类型, 0退货 1换货 2维修
    protected Byte status;
    protected String reason;
    protected String conclusion;
    protected Integer quantity;
    protected Boolean isDisputing;
    protected String name;
    protected String mobile;
    protected Long regionId;
    protected String address;
    protected String afterSaleSn;

    public Long getId(){return this.id;}
    public void setId(Long id){this.id=id;}
    public Byte getType(){return this.type;}
    public void setType(Byte type){this.type=type;}
    public Byte getStatus(){return this.status;}
    public void setStatus(Byte status){this.status=status;}
    public String getReason(){return this.reason;}
    public void setReason(String reason){this.reason=reason;}
    public String getConclusion(){return this.conclusion;}
    public void setConclusion(String conclusion){this.conclusion=conclusion;}
    public Integer getQuantity(){return this.quantity;}
    public void setQuantity(Integer quantity){this.quantity=quantity;}
    public Boolean getIsDisputing(){return this.isDisputing;}
    public void setIsDisputing(Boolean isDisputing){this.isDisputing=isDisputing;}
    public String getName(){return this.name;}
    public void setName(String name){this.name=name;}
    public String getMobile(){return this.mobile;}
    public void setMobile(String mobile){this.mobile=mobile;}
    public Long getRegionId(){return this.regionId;}
    public void setRegionId(Long regionId){this.regionId=regionId;}
    public String getAddress(){return this.address;}
    public void setAddress(String address){this.address=address;}
    public LocalDateTime getGmtCreate() {return gmtCreate;}
    public void setGmtCreate(LocalDateTime gmtCreate) {this.gmtCreate=gmtCreate;}
    public LocalDateTime getGmtModified() {return gmtModified;}
    public void setGmtModified(LocalDateTime gmtModified) {this.gmtModified=gmtModified;}
    public Consignee getConsignee() {return new Consignee(this.name, this.mobile, this.regionId, this.address);}
    public void setConsignee(Consignee consignee) {
        this.name = consignee.getName();
        this.mobile = consignee.getMobile();
        this.regionId = consignee.getRegionId();
        this.address = consignee.getAddress();
    }

    //customer
    protected Long customerId;
    protected Customer customer;
    protected CustomerDao customerDao;
    public Long getCustomerId(){return this.customerId;}
    public void setCustomerId(Long customerId){this.customerId=customerId;}
    public void setCustomerDao(CustomerDao customerDao){this.customerDao=customerDao;}
    public Customer getCustomer(){
        if(this.customer==null){
            this.customer=customerDao.findById(this.customerId);
        }
        return this.customer;
    }

    //orderItem
    protected Long orderItemId;
    protected OrderItem orderItem;
    protected OrderItemDao orderItemDao;
    public Long getOrderItemId(){return this.orderItemId;}
    public void setOrderItemId(Long orderItemId){this.orderItemId=orderItemId;}
    public void setOrderItemDao(OrderItemDao orderItemDao){this.orderItemDao=orderItemDao;}
    public OrderItem getOrderItem(){
        if(this.orderItem==null){
            this.orderItem=orderItemDao.findById(this.orderItemId);
        }
        return this.orderItem;
    }

    //寄出Express
    protected Long sendOffExpressId;
    protected Express sendOffExpress;
    public Long getSendOffExpressId(){return this.sendOffExpressId;}
    public void setSendOffExpressId(Long sendOffExpressId){this.sendOffExpressId=sendOffExpressId;}

    //寄回Express
    protected Long sendBackExpressId;
    protected Express sendBackExpress;
    public Long getSendBackExpressId(){return this.sendBackExpressId;}
    public void setSendBackExpressId(Long sendBackExpressId){this.sendBackExpressId=sendBackExpressId;}

    protected ExpressDao expressDao;
    public void setExpressDao(ExpressDao expressDao){this.expressDao=expressDao;}
    public Express getSendOffExpress(){
        if(this.sendOffExpress==null){
            this.sendOffExpress=expressDao.findById(this.sendOffExpressId);
        }
        return this.sendOffExpress;
    }
    public Express getSendBackExpress(){
        if(this.sendBackExpress==null){
            this.sendBackExpress=expressDao.findById(this.sendBackExpressId);
        }
        return this.sendBackExpress;
    }

    //Service Order
    protected Long serviceOrderId;
    protected ServiceOrder serviceOrder;
    protected ServiceOrderDao serviceOrderDao;
    public Long getServiceOrderId(){return this.serviceOrderId;}
    public void setServiceOrderId(Long serviceOrderId){this.serviceOrderId=serviceOrderId;}
    public void setServiceOrderDao(ServiceOrderDao serviceOrderDao){this.serviceOrderDao=serviceOrderDao;}
    public ServiceOrder getServiceOrder(){
        if(this.serviceOrder==null){
            this.serviceOrder=serviceOrderDao.findById(this.serviceOrderId);
        }
        return this.serviceOrder;
    }

    //Shop
    protected Long shopId;
    protected Shop shop;
    protected ShopDao shopDao;
    public Long getShopId(){return this.shopId;}
    public void setShopId(Long shopId){this.shopId=shopId;}
    public void setShopDao(ShopDao shopDao){this.shopDao=shopDao;}
    public Shop getShop(){
        if(this.shop==null){
            this.shop=shopDao.findById(this.shopId);
        }
        return this.shop;
    }

    //Product
    protected Long productId;
    protected Product product;
    protected ProductDao productDao;
    public Long getProductId(){return this.productId;}
    public void setProductId(Long productId){this.productId=productId;}
    public void setProductDao(ProductDao productDao){this.productDao=productDao;}
    public Product getProduct(){
        if(this.product==null){
            this.product=productDao.findById(this.productId);
        }
        return this.product;
    }

    //Refund
    protected Long refundId;
    protected Refund refund;
    protected RefundDao refundDao;
    public Long getRefundId(){return this.refundId;}
    public void setRefundId(Long refundId){this.refundId=refundId;}
    public void setRefundDao(RefundDao refundDao){this.refundDao=refundDao;}
    public Refund getRefund(){
        if(this.refund == null){
            this.refund = refundDao.findById(this.shopId, this.refundId);
        }
        return this.refund;
    }

    @ToString.Exclude
    @JsonIgnore
    protected final static Logger logger = LoggerFactory.getLogger(AfterSale.class);

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected AfterSaleDao afterSaleDao;
    public void setAfterSaleDao(AfterSaleDao afterSaleDao){this.afterSaleDao=afterSaleDao;}

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected DisputeDao disputeDao;
    public void setDisputeDao(DisputeDao disputeDao){this.disputeDao=disputeDao;}

    /**
     * 共三种类型
     */
    //退货
    @ToString.Exclude
    @JsonIgnore
    public static final Byte RETURN = 0;
    //换货
    @ToString.Exclude
    @JsonIgnore
    public static final Byte CHANGE = 1;
    //服务
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SERVICE = 2;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> TYPENAMES = new HashMap<>() {
        {
            put(RETURN, "退货");
            put(CHANGE, "换货");
            put(SERVICE, "维修");
        }
    };

    /**
     * 共四种状态
     */
    //待审核
    @ToString.Exclude
    @JsonIgnore
    public static final Byte WAITING = 0;
    //进行中
    @ToString.Exclude
    @JsonIgnore
    public static final Byte GOING = 1;
    //已取消
    @ToString.Exclude
    @JsonIgnore
    public static final Byte CANCELLED = 2;
    //已完成
    @ToString.Exclude
    @JsonIgnore
    public static final Byte FINISHED = 3;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> STATUSNAMES = new HashMap<>() {
        {
            put(WAITING, "待审核");
            put(GOING, "进行中");
            put(CANCELLED, "已取消");
            put(FINISHED, "已完成");
        }
    };

    /**
     * 允许的状态迁移
     */
    @JsonIgnore
    @ToString.Exclude
    protected static final Map<Byte, Set<Byte>> toStatus = new HashMap<>(){
        {
            put(WAITING, new HashSet<>() {
                {
                    add(GOING);
                    add(CANCELLED); // 顾客取消
                    add(FINISHED); // 仅退款
                }
            });
            put(GOING, new HashSet<>() {
                {
                    add(FINISHED);
                    add(CANCELLED); // 商户取消
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

    public abstract void process(ConfirmDto confirm);
    public void confirm(ConfirmDto confirm, UserDto userDto) {
        if(confirm.getConfirm()) {
            logger.debug("confirm.getConfirm() = "+confirm.getConfirm());
            this.process(confirm);
        }
        this.setConclusion(confirm.getConclusion());

        this.afterSaleDao.save(this, userDto);
    }
    public Dispute createDispute(String reason, UserDto userDto) {
        List<Dispute> disputeList = this.disputeDao.findByASId(this.getId());

        for(Dispute dispute : disputeList) {
            if(!(Objects.equals(dispute.getStatus(), Dispute.CANCELLED))) {
                throw new BusinessException(ReturnNo.AFTERSALE_INARBITRATION, "售后单在仲裁中");
            }
        }

        Dispute bo = new Dispute(this, reason);
        this.disputeDao.insert(bo, userDto);
        this.isDisputing = true;
        this.afterSaleDao.save(this, userDto);
        return bo;
    }

}
