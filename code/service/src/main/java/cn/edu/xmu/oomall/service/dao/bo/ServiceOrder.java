package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.MaintainerDao;
import cn.edu.xmu.oomall.service.dao.openfeign.AfterSaleDao;
import cn.edu.xmu.oomall.service.dao.ProductServiceDao;
import cn.edu.xmu.oomall.service.dao.ServiceOrderDao;
import cn.edu.xmu.oomall.service.dao.openfeign.ExpressDao;
import cn.edu.xmu.oomall.service.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Region;
import cn.edu.xmu.oomall.service.controller.Dto.MaintainerMessageDto;
import cn.edu.xmu.oomall.service.controller.Dto.ServiceOrderCreateDto;
import cn.edu.xmu.oomall.service.controller.Dto.SimpleServiceOrderDto;
import cn.edu.xmu.oomall.service.mapper.po.ServiceOrderPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ServiceOrderPo.class})
public abstract class ServiceOrder extends OOMallObject implements Serializable{
    private final static Logger logger = LoggerFactory.getLogger(ServiceOrder.class);
    protected Long id;
    protected Byte type;
    protected Byte serviceType;
    protected Byte status;
    protected Byte result;//0未处理 1已处理
    protected String description;
    protected String serviceOrderSn;
    //product
    protected Long productId;
    protected Long productServiceId;
    //customer
    protected Long customerId;
    protected String customerPhone;
    protected String customerName;
    protected String customerAddress;
    //maintainer
    protected Long maintainerId;
    protected String maintainerPhone;
    protected String maintainerName;
    protected String maintainerAddress;

    //shop
    protected Long shopId;
    //region
    protected Long regionId;
    //Express
    protected Long sendExpressId;
    protected String sendExpressBillCode;
    protected Long receiveExpressId;
    protected String receiveExpressBillCode;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected AfterSaleDao afterSaleDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected ServiceOrderDao serviceOrderDao;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected RegionDao regionDao;
    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected ProductServiceDao productServiceDao;

    @Setter
    @JsonIgnore
    @ToString.Exclude
    protected ExpressDao expressDao;

    /**
     * serviceOrder 类型 寄修 上门
     */
    //寄修
    @ToString.Exclude
    @JsonIgnore
    public static final Byte  SENDSERVICE= 0;
    //上门
    @ToString.Exclude
    @JsonIgnore
    public static final Byte ONSPOTSERVICE = 1;

    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> TYPENAMES = new HashMap<>() {
        {
            put(SENDSERVICE, "寄修");
            put(ONSPOTSERVICE, "上门");
        }
    };



    /**
     * serviceOrder 4种状态
     */
    //待接收
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
            put(WAITING, "待接收");
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
                    add(GOING);//服务商接收
                    add(CANCELLED); // 商户取消
                }
            });
            put(GOING, new HashSet<>() {
                {
                    add(FINISHED);
                    add(WAITING);//服务商撤销
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

    /**
     * 额外的方法
     */
    /**
     * 商铺创建服务单 ---createServiceOrder
     * @param serviceOrderCreateDto
     * @param user
     * @return
     */
    public ServiceOrder createServiceOrder(AfterSale afterSale, ServiceOrderCreateDto serviceOrderCreateDto, UserDto user){
        // 设置serviceOrder的属性
        this.setType(serviceOrderCreateDto.getType());//type
        this.setRegionId(serviceOrderCreateDto.getConsignee().getRegionID());
        this.setCustomerAddress(serviceOrderCreateDto.getConsignee().getAddress());
        this.setCustomerName(serviceOrderCreateDto.getConsignee().getName());
        this.setCustomerPhone(serviceOrderCreateDto.getConsignee().getMobile());
        this.setCustomerId(afterSale.getCustomer().getId());
        this.setProductId(afterSale.getProduct().getId());

        this.setStatus(WAITING);

        this.serviceOrderDao.insert(this,user);//serviceOrder插入
        return this;
    }
    /**
     * 服务单被服务商接收 --acceptedByMaintainerId
     */
    public abstract void acceptedByMaintainerId(Long mid, MaintainerMessageDto m_message, UserDto user);
    // 服务单是否可被服务商接收 --isAcceptable(mid)
    Boolean isAcceptable(Long mid) {// !!!
        Boolean flag = false;
        Region region = this.regionDao.findById(this.getRegionId());
        List<Region>  fatherRegions = this.regionDao.findParentsById(region.getId());
        List<ProductService> productServiceList = this.productServiceDao.findByServiceTypeAndProductIdAndShopId(this.getServiceType(),this.getProductId(),this.getShopId());
        logger.debug("productServiceList = "+productServiceList);
        for(ProductService productService : productServiceList){
            if(!productService.getMaintainerId().equals(mid)){
                continue;
            }else{
                if(productService.getRegionId().equals(this.getRegionId())){
                    flag = true;
                    break;
                }else{
                    for(Region fatherRegion : fatherRegions){
                        if(fatherRegion.getId().equals(productService.getRegionId())){
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }
    /**
     * 服务商取消服务单
     */
    public abstract void cancelByMaintainer(Long maintainerId, UserDto user);

    /**
     * 所有属性的getter和setter方法
     */
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