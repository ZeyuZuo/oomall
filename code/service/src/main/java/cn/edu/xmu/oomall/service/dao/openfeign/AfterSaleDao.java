package cn.edu.xmu.oomall.service.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.service.mapper.openfeign.AfterSaleMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AfterSaleDao {
    private AfterSaleMapper afterSaleMapper;

    private final static Logger logger = LoggerFactory.getLogger(AfterSaleDao.class);
    @Autowired
    public AfterSaleDao(AfterSaleMapper afterSaleMapper){
        this.afterSaleMapper = afterSaleMapper;
    }
    public AfterSale findById(Long shopId, Long id){
        logger.debug("findById: shopId = "+shopId+" id = "+id);
        InternalReturnObject<AfterSale> ret = this.afterSaleMapper.findById("test",shopId,id); // TODO: replace "test" with authorization
        if(ret== null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, "售后单不存在");
        }
        logger.debug("findById: ret = "+ret);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
