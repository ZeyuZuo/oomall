package cn.edu.xmu.oomall.aftersale.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.ShopAfterSaleController;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.DisputeDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.oomall.aftersale.dao.bo.Dispute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DisputeService {
    private DisputeDao disputeDao;
    private AfterSaleDao afterSaleDao;
    private final static Logger logger = LoggerFactory.getLogger(ShopAfterSaleController.class);
    @Autowired
    public DisputeService(DisputeDao disputeDao, AfterSaleDao afterSaleDao){
        this.disputeDao = disputeDao;
        this.afterSaleDao = afterSaleDao;
    }

    public Dispute createDispute(Long id, String reason, UserDto userDto){
        AfterSale aftersale = afterSaleDao.findById(id);
        logger.debug("aftersale: "+aftersale);
        if(aftersale == null){//未找到aftersale
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "售后单不存在");
        }
        if(!Objects.equals(aftersale.getCustomerId(), userDto.getId())){//顾客id不匹配
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "售后单不属于该顾客");
        }
        if(!Objects.equals(aftersale.getStatus(), AfterSale.FINISHED)){//aftersale不是已完成状态
            throw new BusinessException(ReturnNo.STATENOTALLOW, "售后单状态禁止");
        }
        return aftersale.createDispute(reason,userDto);
    }
}
