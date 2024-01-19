package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.dto.ConfirmDto;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostRefundParam;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.*;
import cn.edu.xmu.oomall.aftersale.mapper.po.AfterSalePo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({AfterSalePo.class})
public class ReturnAfterSale extends AfterSale{

//    @Autowired
//    public ReturnAfterSale(ExpressDao expressDao, OrderItemDao orderItemDao, CustomerDao customerDao, ShopDao shopDao, ProductDao productDao, ServiceOrderDao serviceOrderDao, RefundDao refundDao){
//        this.expressDao = expressDao;
//        this.orderItemDao = orderItemDao;
//        this.customerDao = customerDao;
//        this.shopDao = shopDao;
//        this.productDao = productDao;
//        this.serviceOrderDao = serviceOrderDao;
//        this.refundDao = refundDao;
//    }

    @Override
    public void process(ConfirmDto confirmDto){
        // TODO
        // 退款需要生成退款单，物流单
        if(confirmDto.getRefundOnly()){
            // 仅退款
            OrderItem orderItem = this.getOrderItem();
            logger.debug("orderItem: " + orderItem);
            PostRefundParam param = PostRefundParam.builder()
                    .amount(orderItem.getAmount())
                    .divAmount(orderItem.getDivAmount())
                    .build();

            // 生成退款单
            Refund refund = this.refundDao.createRefund(this.getShopId(), orderItem.getPaymentId(), param);
            this.setRefundId(refund.getId());
            if(this.allowStatus(FINISHED))
                this.setStatus(FINISHED);
            else
                throw new BusinessException(ReturnNo.STATENOTALLOW, "售后单状态禁止");
        }
        else{
            // 退货退款, 只需要生成物流单
            Consignee sender = new Consignee();
            sender.setName(this.getName());
            sender.setMobile(this.getMobile());
            sender.setRegionId(this.getRegionId());
            sender.setAddress(this.getAddress());

            Consignee receiver = confirmDto.getConsignee();
            Express express = this.expressDao.createExpress(sender, receiver, this.getShopId());

            this.setSendOffExpressId(express.getId());
            if(this.allowStatus(GOING))
                this.setStatus(GOING);
            else
                throw new BusinessException(ReturnNo.STATENOTALLOW, "售后单状态禁止");

        }
    }

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
    public Long getCustomerId(){return this.customerId;}
    public void setCustomerId(Long customerId){this.customerId=customerId;}
    public Customer getCustomer(){
        if(this.customer==null){
            this.customer=customerDao.findById(this.customerId);
        }
        return this.customer;
    }
    public Long getOrderItemId(){return this.orderItemId;}
    public void setOrderItemId(Long orderItemId){this.orderItemId=orderItemId;}
    public OrderItem getOrderItem(){
        if(this.orderItem==null){
            this.orderItem=orderItemDao.findById(this.orderItemId);
        }
        return this.orderItem;
    }
    public Long getSendOffExpressId(){return this.sendOffExpressId;}
    public void setSendOffExpressId(Long sendOffExpressId){this.sendOffExpressId=sendOffExpressId;}

    public Long getSendBackExpressId(){return this.sendBackExpressId;}
    public void setSendBackExpressId(Long sendBackExpressId){this.sendBackExpressId=sendBackExpressId;}

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
    public Long getServiceOrderId(){return this.serviceOrderId;}
    public void setServiceOrderId(Long serviceOrderId){this.serviceOrderId=serviceOrderId;}
    public ServiceOrder getServiceOrder(){
        if(this.serviceOrder==null){
            this.serviceOrder=serviceOrderDao.findById(this.serviceOrderId);
        }
        return this.serviceOrder;
    }
    public Long getShopId(){return this.shopId;}
    public void setShopId(Long shopId){this.shopId=shopId;}
    public Shop getShop(){
        if(this.shop==null){
            this.shop=shopDao.findById(this.shopId);
        }
        return this.shop;
    }
    public Long getProductId(){return this.productId;}
    public void setProductId(Long productId){this.productId=productId;}
    public Product getProduct(){
        if(this.product==null){
            this.product=productDao.findById(this.productId);
        }
        return this.product;
    }
    public Long getRefundId(){return this.refundId;}
    public void setRefundId(Long refundId){this.refundId=refundId;}
    public Refund getRefund(){
        if(this.refund == null){
            this.refund = refundDao.findById(this.shopId, this.refundId);
        }
        return this.refund;
    }
}
