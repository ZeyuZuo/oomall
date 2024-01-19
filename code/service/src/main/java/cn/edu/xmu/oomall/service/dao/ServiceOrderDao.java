package cn.edu.xmu.oomall.service.dao;


import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.service.dao.bo.OnSpotServiceOrder;
import cn.edu.xmu.oomall.service.dao.bo.ProductService;
import cn.edu.xmu.oomall.service.dao.bo.SendServiceOrder;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import cn.edu.xmu.oomall.service.dao.openfeign.AfterSaleDao;
import cn.edu.xmu.oomall.service.dao.openfeign.ExpressDao;
import cn.edu.xmu.oomall.service.dao.openfeign.RegionDao;
import cn.edu.xmu.oomall.service.mapper.jpa.ServiceOrderPoMapper;
import cn.edu.xmu.oomall.service.mapper.po.ProductServicePo;
import cn.edu.xmu.oomall.service.mapper.po.ServiceOrderPo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.lettuce.core.internal.Exceptions;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;

@Repository
public class ServiceOrderDao {
    private final static String KEY = "R%d";
    private final static Logger logger = LoggerFactory.getLogger(ServiceOrderDao.class);
    private ServiceOrderPoMapper serviceorderPoMapper;
    private ProductServiceDao productServiceDao;
    private RegionDao regionDao;
    private AfterSaleDao afterSaleDao;
    private MaintainerDao maintainerDao;
    public MaintainerDao getMaintainerDao() {
        return maintainerDao;
    }
    private ExpressDao expressDao;

    private void prebuild(ServiceOrder bo) {
        bo.setServiceOrderDao(this);
        bo.setRegionDao(regionDao);
        bo.setAfterSaleDao(afterSaleDao);
        bo.setProductServiceDao(productServiceDao);
        bo.setExpressDao(expressDao);
    }
    private abstract class ServiceOrderFactory {
        public abstract ServiceOrder create(ServiceOrderPo po);
        public abstract ServiceOrder newServiceOrder();
    }

    private class SendFactory extends ServiceOrderFactory {
        @Override
        public ServiceOrder create(ServiceOrderPo po) {
            SendServiceOrder bo = CloneFactory.copy(new SendServiceOrder(), po);
            prebuild(bo);
            return bo;
        }
        public ServiceOrder newServiceOrder() {
            return new SendServiceOrder();
        }
    }
    private class OnSpotFactory extends ServiceOrderFactory {
        @Override
        public ServiceOrder create(ServiceOrderPo po) {
            OnSpotServiceOrder bo = CloneFactory.copy(new OnSpotServiceOrder(), po);
            prebuild(bo);
            return bo;
        }
        public ServiceOrder newServiceOrder() {
            return new OnSpotServiceOrder();
        }
    }
    //根据类型获取不同的服务单 ------
    public ServiceOrder NEWServiceOrder(byte type) {
        ServiceOrderFactory factory = registry.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No service found for type: " + type);
        }
        return factory.newServiceOrder();
    }

    // 服务注册表
    private static final Map<Byte, ServiceOrderFactory> registry = new HashMap<>();

    @Autowired
    public ServiceOrderDao(ServiceOrderPoMapper serviceorderPoMapper,
                           ProductServiceDao productServiceDao,
                           RegionDao regionDao,
                           AfterSaleDao afterSaleDao,
                           ExpressDao expressDao) {
        this.afterSaleDao = afterSaleDao;
        this.regionDao = regionDao;
        this.productServiceDao = productServiceDao;
        this.serviceorderPoMapper = serviceorderPoMapper;
        this.expressDao = expressDao;
        registry.put((byte) 0, new SendFactory());
        registry.put((byte) 1, new OnSpotFactory());
    }

    // 根据服务类型获取不同的服务单
    public static ServiceOrder build(ServiceOrderPo po) {
        ServiceOrderFactory factory = registry.get(po.getType());
        if (factory == null) {
            throw new IllegalArgumentException("No service found for type: " + po.getType());
        }
        return factory.create(po);
    }

    public ServiceOrder findById(Long id) {
        Optional<ServiceOrderPo> po = this.serviceorderPoMapper.findById(id);
        if (po.isPresent()) {
            return build(po.get());
        }
        else {
            return null;
        }
    }

    public List<ServiceOrder> findOnGoingByMaintainerId(Long id) {
        List<ServiceOrderPo> poList = this.serviceorderPoMapper.findByMaintainerIdAndStatus(id, ServiceOrder.GOING);
        List<ServiceOrder> boList = new ArrayList<>();

        for (ServiceOrderPo po : poList) {
            ServiceOrder bo = build(po);
            boList.add(bo);
        }
        return boList;
    }

    public String save(ServiceOrder bo, UserDto user){
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        ServiceOrderPo po = CloneFactory.copy(new ServiceOrderPo(), bo);
        logger.debug("save: po = {}", po);
        ServiceOrderPo updatedPo = serviceorderPoMapper.save(po);
        if(IDNOTEXIST.equals(updatedPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务单", bo.getId()));
        }
        return String.format(KEY, bo.getId());
    }

    /**
     * 插入新的服务单
     * @param serviceOrder
     * @param user
     */
    public ServiceOrder insert(ServiceOrder serviceOrder,UserDto user){
        serviceOrder.setId(null);
        serviceOrder.setCreator(user);
        serviceOrder.setGmtCreate(LocalDateTime.now());
        ServiceOrderPo po = CloneFactory.copy(new ServiceOrderPo(), serviceOrder);
        logger.debug("insert: po = {}", po);
        ServiceOrderPo save = this.serviceorderPoMapper.save(po);
        serviceOrder.setId(save.getId());
        return serviceOrder;
    }
    /**
     * 根据售后单id查找服务单
     */
    public ServiceOrder findByAftersalesOrderId(Long afterSaleId){
        logger.debug("findByAftersalesOrderId: afterSaleId = {}", afterSaleId);
        ServiceOrderPo po = this.serviceorderPoMapper.findByAftersalesId(afterSaleId);
        logger.debug("findByAftersalesOrderId: po = {}", po);
        if(po == null){
            return null;
        }
        return build(po);
    }

}
