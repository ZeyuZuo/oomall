package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import java.io.Serializable;
import java.time.LocalDateTime;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.service.dao.MaintainerDao;
import cn.edu.xmu.oomall.service.dao.ProductServiceDao;
import cn.edu.xmu.oomall.service.dao.ServiceOrderDao;
import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
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

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, doNotUseGetters = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({MaintainerPo.class})
public class Maintainer extends OOMallObject implements Serializable
{

    private Long regionId;
    private String name;
    private String consignee;
    private String phone;
    private String address;
    private Byte status;    //0-VALID 1-SUSPENDED 2-DELETED

    //这里定义了一个用于记录日志的 Logger 对象。
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Maintainer.class);

    @Setter
    @JsonIgnore
    @ToString.Exclude
    private MaintainerDao maintainerDao;

    /**
     * 服务商 共三种状态
     */
    //正常
    @ToString.Exclude
    @JsonIgnore
    public static final Byte VALID = 0;
    //暂停
    @ToString.Exclude
    @JsonIgnore
    public static final Byte SUSPENDED = 1;
    //已删除
    @ToString.Exclude
    @JsonIgnore
    public static final Byte DELETED = 2;

    //类型名称映射：
    @ToString.Exclude
    @JsonIgnore
    public static final Map<Byte, String> TYPENAMES = new HashMap<>()
    {
        {
            put(VALID, "正常");
            put(SUSPENDED, "暂停");
            put(DELETED, "已删除");
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
                    add(DELETED); // 管理员删除
                }
            });
        }
    };

    /**
     * 是否允许状态迁移
     *
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

    // 所有属性的set && get
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        id = id;
    }

    public Long getRegionId()
    {
        return regionId;
    }

    public void setRegionId(Long regionId)
    {
        this.regionId = regionId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getConsignee()
    {
        return consignee;
    }

    public void setConsignee(String consignee)
    {
        this.consignee = consignee;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Byte getStatus()
    {
        return status;
    }

    public void setStatus(Byte status)
    {
        this.status = status;
    }

    public void setGmtCreate(LocalDateTime gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public void setGmtModified(LocalDateTime gmtModified)
    {
        this.gmtModified = gmtModified;
    }

    /**
     * 暂停服务商
     * @param id
     * @param user
     * @return
     */
    @Setter
    @JsonIgnore
    @ToString.Exclude
    private ProductServiceDao productServiceDao;
    public void suspend(Long id,UserDto user)
    {
        List<ProductService> PsList = productServiceDao.findByMaintainerId(id);
        for(ProductService ps:PsList)
        {
            ps.suspend(user);
        }
        this.setStatus(SUSPENDED);
        maintainerDao.save(this,user);
    }

    /**
     * 撤销服务商
     * @return
     */
    private ServiceOrderDao serviceOrderDao;

    public void setServiceOrderDao(ServiceOrderDao serviceOrderDao) {
        this.serviceOrderDao = serviceOrderDao;
    }

    public void cancel (Long id, UserDto user)
    {
        List<ServiceOrder>SOList = serviceOrderDao.findOnGoingByMaintainerId(id);
        //存在进行中的服务单
        if(SOList.size()!=0) {
        throw new BusinessException(ReturnNo.ONGOING_SERVICEORDER_EXIST, "存在进行中的服务单");
        }
        List<ProductService> PsList = productServiceDao.findByMaintainerId(id);
        for(ProductService ps:PsList)
        {
            ps.cancel(user);
        }
        this.setStatus(DELETED);
        maintainerDao.save(this,user);
    }

}