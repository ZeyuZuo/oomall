package cn.edu.xmu.oomall.service.service;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.openfeign.AfterSaleDao;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
import cn.edu.xmu.oomall.service.controller.Dto.MaintainerMessageDto;
import cn.edu.xmu.oomall.service.controller.Dto.ServiceOrderCreateDto;
import cn.edu.xmu.oomall.service.controller.Dto.SimpleServiceOrderDto;
import cn.edu.xmu.oomall.service.dao.ServiceOrderDao;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceOrderService {
    private AfterSaleDao afterSaleDao;
    private ServiceOrderDao serviceOrderDao;

    private final static Logger logger = LoggerFactory.getLogger(ServiceOrderService.class);

    @Autowired
    public ServiceOrderService(AfterSaleDao afterSaleDao, ServiceOrderDao serviceOrderDao) {
        this.afterSaleDao = afterSaleDao;
        this.serviceOrderDao = serviceOrderDao;
    }
    /**
     * 商户创建服务单
     * @param shopId
     * @param id
     * @param serviceOrderCreateDto
     * @param userDto
     * @return
     */
    public ServiceOrder createServiceOrder(Long shopId, Long id, ServiceOrderCreateDto serviceOrderCreateDto, UserDto userDto) {
        AfterSale afterSale = this.afterSaleDao.findById(shopId,id);
        if(afterSale == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "售后单不存在");
        }
        if(!Objects.equals(afterSale.getShop().getId(), shopId)){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "售后单不属于该店铺");
        }
        ServiceOrder serviceOrder = this.serviceOrderDao.findByAftersalesOrderId(id);
        if(serviceOrder != null){
            throw new BusinessException(ReturnNo.AFTERSALE_EXIST_SERVICEORDER, "售后单已有服务单");
        }
        ServiceOrder newServiceOrder = this.serviceOrderDao.NEWServiceOrder(serviceOrderCreateDto.getType());
        newServiceOrder.setServiceOrderDao(this.serviceOrderDao);
        newServiceOrder = newServiceOrder.createServiceOrder(afterSale, serviceOrderCreateDto,userDto);
        return newServiceOrder;
    }

    /**
     * 服务商接收服务单
     * @param maintainerId
     * @param id
     * @param m_message
     * @param userDto
     * @return
     */
    public void acceptServiceOrder(Long maintainerId, Long id,MaintainerMessageDto m_message, UserDto userDto) {
        ServiceOrder serviceOrder = this.serviceOrderDao.findById(id);
        if(serviceOrder == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "服务单不存在");
        }
        if(!serviceOrder.getStatus().equals(ServiceOrder.WAITING)){//服务单已经被接收
            throw new BusinessException(ReturnNo.STATENOTALLOW, "服务单不是待接收状态");
        }
        serviceOrder.acceptedByMaintainerId(maintainerId, m_message, userDto);
    }

    /**
     * 服务商撤销服务单
     * @param maintainerId
     * @param serviceOrderId
     * @param userDto
     * @return
     */
    public void cancelServiceOrder(Long maintainerId,Long serviceOrderId, UserDto userDto) {
        ServiceOrder serviceOrder = this.serviceOrderDao.findById(serviceOrderId);
        if(serviceOrder == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "服务单不存在");
        }
        if(!serviceOrder.getMaintainerId().equals(maintainerId)){//不是该服务商的服务单
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "不是该服务商的服务单");
        }
        serviceOrder.cancelByMaintainer(maintainerId, userDto);
    }
}