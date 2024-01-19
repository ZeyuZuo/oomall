package cn.edu.xmu.oomall.aftersale.dao;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.oomall.aftersale.dao.bo.ChangeAfterSale;
import cn.edu.xmu.oomall.aftersale.dao.bo.ReturnAfterSale;
import cn.edu.xmu.oomall.aftersale.dao.bo.ServiceAfterSale;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import cn.edu.xmu.oomall.aftersale.mapper.jpa.AfterSalePoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.po.AfterSalePo;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;

@Repository
public class AfterSaleDao {
    private final static String KEY = "R%d";
    private final static Logger logger = LoggerFactory.getLogger(AfterSaleDao.class);
    private AfterSalePoMapper afterSalePoMapper;
    private CustomerDao customerDao;
    private ExpressDao expressDao;
    private OrderItemDao orderItemDao;
    private ProductDao productDao;
    private RefundDao refundDao;
    private ServiceOrderDao serviceOrderDao;
    private ShopDao shopDao;
    private DisputeDao disputeDao;

    @Autowired
    public void setDisputeDao(DisputeDao disputeDao, CustomerDao customerDao, ExpressDao expressDao, OrderItemDao orderItemDao, ProductDao productDao, RefundDao refundDao, ServiceOrderDao serviceOrderDao, ShopDao shopDao) {
        this.disputeDao = disputeDao;
        this.customerDao = customerDao;
        this.expressDao = expressDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
        this.refundDao = refundDao;
        this.serviceOrderDao = serviceOrderDao;
        this.shopDao = shopDao;
    }

    public void preBuild(AfterSale afterSale){
        afterSale.setCustomerDao(customerDao);
        afterSale.setExpressDao(expressDao);
        afterSale.setOrderItemDao(orderItemDao);
        afterSale.setProductDao(productDao);
        afterSale.setRefundDao(refundDao);
        afterSale.setServiceOrderDao(serviceOrderDao);
        afterSale.setShopDao(shopDao);
        afterSale.setAfterSaleDao(AfterSaleDao.this);
        afterSale.setDisputeDao(disputeDao);
    }
    private abstract class AfterSaleFactory {
        public abstract AfterSale create(AfterSalePo po);
    }

    private class ReturnFactory extends AfterSaleFactory {
        @Override
        public AfterSale create(AfterSalePo po) {
            ReturnAfterSale returnAfterSale = new ReturnAfterSale();
            preBuild(returnAfterSale);
            return CloneFactory.copy(returnAfterSale, po);
        }
    }
    private class ChangeFactory extends AfterSaleFactory {
        @Override
        public AfterSale create(AfterSalePo po) {
            ChangeAfterSale changeAfterSale = new ChangeAfterSale();
            preBuild(changeAfterSale);
            return CloneFactory.copy(changeAfterSale, po);
        }
    }
    private class ServiceFactory extends AfterSaleFactory {
        @Override
        public AfterSale create(AfterSalePo po) {
            ServiceAfterSale serviceAfterSale = new ServiceAfterSale();
            preBuild(serviceAfterSale);
            return CloneFactory.copy(serviceAfterSale, po);
        }
    }
    // 服务注册表
    private static final Map<Byte, AfterSaleFactory> registry = new HashMap<>();

    @Autowired
    @Lazy
    public AfterSaleDao(AfterSalePoMapper afterSalePoMapper,
                        CustomerDao customerDao,
                        ExpressDao expressDao,
                        OrderItemDao orderItemDao,
                        ProductDao productDao,
                        RefundDao refundDao,
                        ServiceOrderDao serviceOrderDao,
                        ShopDao shopDao) {
        this.customerDao = customerDao;
        this.expressDao = expressDao;
        this.orderItemDao = orderItemDao;
        this.productDao = productDao;
        this.refundDao = refundDao;
        this.serviceOrderDao = serviceOrderDao;
        this.shopDao = shopDao;
        this.afterSalePoMapper = afterSalePoMapper;
        registry.put((byte) 0, new ReturnFactory());
        registry.put((byte) 1, new ChangeFactory());
        registry.put((byte) 2, new ServiceFactory());
    }

    // 根据类型获取服务的方法
    public static AfterSale build(AfterSalePo po) {
        AfterSaleFactory factory = registry.get(po.getType());
        if (factory == null) {
            throw new IllegalArgumentException("No service found for type: " + po.getType());
        }
        return factory.create(po);
    }

    public AfterSale findById(Long id) {
        Optional<AfterSalePo> po = this.afterSalePoMapper.findById(id);
        if (po.isPresent()) {
            logger.debug("findById: po = {}", po.get());
            return build(po.get());
        }
        else {
            return null;
        }
    }

    public String save(AfterSale bo, UserDto user){
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        AfterSalePo po = CloneFactory.copy(new AfterSalePo(), bo);
        logger.debug("save: po = {}", po);
        AfterSalePo updatedPo = afterSalePoMapper.save(po);
        if(IDNOTEXIST.equals(updatedPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "售后单", bo.getId()));
        }
        return String.format(KEY, bo.getId());
    }

}
