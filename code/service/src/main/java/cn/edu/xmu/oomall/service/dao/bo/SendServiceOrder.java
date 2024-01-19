package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.MaintainerDao;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Express;
import cn.edu.xmu.oomall.service.dao.openfeign.ExpressDao;
import cn.edu.xmu.oomall.service.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.service.controller.Dto.MaintainerMessageDto;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Consignee;
import cn.edu.xmu.oomall.service.mapper.po.ServiceOrderPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ServiceOrderPo.class})
public class SendServiceOrder extends ServiceOrder {
    @Override
    public void acceptedByMaintainerId(Long mid, MaintainerMessageDto m_message, UserDto user) throws BusinessException {
        Boolean passed = this.isAcceptable(mid);
        if(!passed){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "服务单不可被接收");
        }

        Consignee sender=new Consignee();
        sender.setName(this.getCustomerName());
        sender.setAddress(this.getCustomerAddress());
        sender.setMobile(this.getCustomerPhone());
        sender.setRegionId(this.getRegionId());


       Consignee receiver=new Consignee();
       receiver.setName(m_message.getMaintainerName());
       receiver.setAddress(m_message.getAddress());
       receiver.setMobile(m_message.getMaintainerMobile());
       receiver.setRegionId(m_message.getRegionId());

        Express express = this.expressDao.createExpress(sender, receiver, this.getShopId());

        this.setSendExpressId(express.getId());
        this.setMaintainerId(mid);
        this.setStatus(GOING);
        this.serviceOrderDao.save(this,user);//serviceOrder保存
    }

    public void cancelByMaintainer(Long maintainerId, UserDto user){

        Express express = this.expressDao.findById(this.getSendExpressId());

        if(express.getStatus()==2)
        {
            Express Returnexpress = this.expressDao.createExpress(express.getReceiver(), express.getSender(), this.getShopId());
            this.setReceiveExpressId(Returnexpress.getId());
        }

        this.setStatus(WAITING);
        this.setMaintainerId(null);
        this.serviceOrderDao.save(this,user);

    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Byte getType() {
        return type;
    }
    public void setType(Byte type) {
        this.type = type;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getServiceOrderSn() {
        return serviceOrderSn;
    }
    public void setServiceOrderSn(String serviceOrderSn) {
        this.serviceOrderSn = serviceOrderSn;
    }
    //product
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getProductServiceId() {
        return productServiceId;
    }
    public void setProductServiceId(Long productServiceId) {
        this.productServiceId = productServiceId;
    }
    //customer
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getMaintainerAddress() {
        return maintainerAddress;
    }

    public void setMaintainerAddress(String maintainerAddress) {
        this.maintainerAddress = maintainerAddress;
    }

    //maintainer
    public Long getMaintainerId() {
        return maintainerId;
    }
    public void setMaintainerId(Long maintainerId) {
        this.maintainerId = maintainerId;
    }
    public String getMaintainerPhone() {
        return maintainerPhone;
    }
    public void setMaintainerPhone(String maintainerPhone) {
        this.maintainerPhone = maintainerPhone;
    }
    public String getMaintainerName() {
        return maintainerName;
    }
    public void setMaintainerName(String maintainerName) {
        this.maintainerName = maintainerName;
    }

    //shop
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    //region
    public Long getRegionId() {
        return regionId;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }
    //Express
    public Long getSendExpressId() {
        return sendExpressId;
    }
    public void setSendExpressId(Long sendExpressId) {
        this.sendExpressId = sendExpressId;
    }
    public String getSendExpressBillCode() {
        return sendExpressBillCode;
    }
    public void setSendExpressBillCode(String sendExpressBillCode) {
        this.sendExpressBillCode = sendExpressBillCode;
    }
    public Long getReceiveExpressId() {
        return receiveExpressId;
    }
    public void setReceiveExpressId(Long receiveExpressId) {
        this.receiveExpressId = receiveExpressId;
    }
    public String getReceiveExpressBillCode() {
        return receiveExpressBillCode;
    }
    public void setReceiveExpressBillCode(String receiveExpressBillCode) {
        this.receiveExpressBillCode = receiveExpressBillCode;
    }
    public Byte getServiceType() {
        return serviceType;
    }
    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public void setGmtCreate(LocalDateTime gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtModified(LocalDateTime gmtModified)
    {
        this.gmtModified = gmtModified;
    }
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }


}
