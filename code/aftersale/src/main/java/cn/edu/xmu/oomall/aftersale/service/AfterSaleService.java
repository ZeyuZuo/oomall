package cn.edu.xmu.oomall.aftersale.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.ShopAfterSaleController;
import cn.edu.xmu.oomall.aftersale.controller.dto.ConfirmDto;
import cn.edu.xmu.oomall.aftersale.controller.vo.ConfirmVo;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AfterSaleService {
    private AfterSaleDao afterSaleDao;
    @Autowired
    public AfterSaleService(AfterSaleDao afterSaleDao){
        this.afterSaleDao = afterSaleDao;
    }
    public void confirmAfterSale(Long shopId, Long id, ConfirmDto confirm, UserDto userDto){
        AfterSale aftersale = afterSaleDao.findById(id);
        if(aftersale == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "售后单不存在");
        }
        if(aftersale.getShopId() != shopId){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "售后单不属于该店铺");
        }
        if(!(aftersale.getStatus().equals(AfterSale.WAITING))){
            throw new BusinessException(ReturnNo.STATENOTALLOW, "售后单状态禁止");
        }
        aftersale.confirm(confirm, userDto);
    }

    public AfterSale shopFindById(Long shopId, Long id){
        AfterSale aftersale = afterSaleDao.findById(id);
        if(aftersale == null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "售后单不存在");
        }
        if(aftersale.getShopId() != shopId){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, "售后单不属于该店铺");
        }
        return aftersale;
    }
}
