package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.dao.MaintainerDao;
import cn.edu.xmu.oomall.service.dao.ProductServiceDao;
import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
import cn.edu.xmu.oomall.service.mapper.po.ProductServicePo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ProductServicePo.class})
public class ProductService extends OOMallObject implements Serializable
{

    private Long id;
    private Long productId;
    private Long serviceId;
    private Long maintainerId;
    private Long regionId;
    private Long shopId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Byte serviceType;
    private Byte status;    //0-VALID 1-SUSPENDED

    //这里定义了一个用于记录日志的 Logger 对象。
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductServiceDao productServiceDao;

    /**
     * 服务 共两种状态
     */
    //正常
    @ToString.Exclude
    @JsonIgnore
    public static final Byte VALID = 0;
    //暂停
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SUSPENDED = 1;

    @ToString.Exclude
    @JsonIgnore
    public static final Byte CANCELED = 2 ;

    //类型名称映射：
    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> TYPENAMES = new HashMap<>()
    {
        {
            put(VALID, "正常");
            put(SUSPENDED, "暂停");
            put(CANCELED, "取消");
        }
    };

    //允许的状态迁移
    @JsonIgnore
    @ToString.Exclude
    private static final Map<Byte, Set<Byte>> toStatus = new HashMap<>()
    {
        {
            put(VALID, new HashSet<>()
            {
                {
                    add(SUSPENDED);
                }
            });
            put(SUSPENDED, new HashSet<>()
            {
                {
                    add(VALID);
                    add(CANCELED);
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     * @param status
     * @return
     */
    public boolean allowStatus(Byte status)
    {
        boolean ret = false;
        if (null != status && null != this.status)
        {
            Set<Byte> allowStatusSet = toStatus.get(this.status);
            if (null != allowStatusSet)
            {
                ret = allowStatusSet.contains(status);
            }
        }
        return ret;
    }

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

    public void setServiceType(Byte serviceType) {
        this.serviceType = serviceType;
    }

    public Byte getServiceType() {
        return serviceType;
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

    public void setGmtModified(LocalDateTime gmtModified)
    {
        this.gmtModified = gmtModified;
    }

    public void setGmtCreate(LocalDateTime gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 暂停productService
     */
    public void suspend(UserDto user){
        if(!allowStatus(SUSPENDED))
        {
            return;
        }
        setStatus(SUSPENDED);
        this.productServiceDao.save(this,user);
    }

    public void cancel(UserDto user){
        if(!allowStatus(CANCELED))
        {
            return;
        }
        setStatus(CANCELED);
        this.productServiceDao.save(this,user);
    }

}