package cn.edu.xmu.oomall.aftersale.controller.dto;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@CopyFrom({AfterSale.class})
public class AfterSaleDto extends OOMallObject {
    private Long id;
    private String afterSaleSn;
    private Byte type;
    private String reason;
    private String conclusion;
    private Integer quantity;
    private Byte status;
    private Boolean isDisputing;
    private OrderItem orderItem;
    private Express sendOffExpress;
    private Express sendBackExpress;
    private IdNameTypeDto customer;
    private IdNameTypeDto shop;
    private IdNameTypeDto product;
    private Consignee consignee;


    public AfterSaleDto(AfterSale afterSale) {
        super();
        CloneFactory.copy(this, afterSale);
        this.setOrderItem(afterSale.getOrderItem());
        this.setCustomer(afterSale.getCustomer());
        this.setShop(afterSale.getShop());
        this.setProduct(afterSale.getProduct());
        this.setSendOffExpress(afterSale.getSendOffExpress());
        this.setSendBackExpress(afterSale.getSendBackExpress());
        this.setConsignee(new Consignee(afterSale.getName(), afterSale.getMobile(), afterSale.getRegionId(), afterSale.getAddress()));
    }


    public Long getId() {
        return id;
    }
    public String getAfterSaleSn() {
        return afterSaleSn;
    }
    public Byte getType() {
        return type;
    }
    public String getReason() {
        return reason;
    }
    public String getConclusion() {
        return conclusion;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public Byte getStatus() {
        return status;
    }
    public Boolean getIsDisputing() {
        return isDisputing;
    }
    public OrderItem getOrderItem() {
        return orderItem;
    }
    public IdNameTypeDto getCustomer() {
        return customer;
    }
    public IdNameTypeDto getShop() {
        return shop;
    }
    public IdNameTypeDto getProduct() {
        return product;
    }
    public Consignee getConsignee() {
        return consignee;
    }
    public Express getSendOffExpress() {
        return sendOffExpress;
    }
    public Express getSendBackExpress() {
        return sendBackExpress;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setAfterSaleSn(String afterSaleSn) {
        this.afterSaleSn = afterSaleSn;
    }
    public void setType(Byte type) {
        this.type = type;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public void setIsDisputing(Boolean isDisputing) {
        this.isDisputing = isDisputing;
    }
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
    public void setCustomer(Customer customer) {
        this.customer = IdNameTypeDto.builder().id(customer.getId()).name(customer.getName()).build();
    }
    public void setShop(Shop shop){
        this.shop = IdNameTypeDto.builder().id(shop.getId()).name(shop.getName()).build();
    }
    public void setProduct(Product product){
        this.product = IdNameTypeDto.builder().id(product.getId()).name(product.getName()).build();
    }

    public void setSendOffExpress(Express sendOffExpress){
        this.sendOffExpress = sendOffExpress;
    }
    public void setSendBackExpress(Express sendBackExpress){
        this.sendBackExpress = sendBackExpress;
    }
    public void setConsignee(Consignee consignee){
        this.consignee = consignee;
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
