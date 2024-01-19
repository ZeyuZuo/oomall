package cn.edu.xmu.oomall.aftersale.dao.openfeign;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.OrderItemMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostExpressParam;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Consignee;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Express;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExpressDao {
    private ExpressMapper expressMapper;
    private final static Logger logger = LoggerFactory.getLogger(ExpressDao.class);

    @Autowired
    public ExpressDao(ExpressMapper expressMapper){
        this.expressMapper = expressMapper;
    }

    public Express findById(Long id){
        InternalReturnObject<Express> ret = this.expressMapper.getExpressById(id);
        logger.debug("ExpressDao.findById: ret = " + ret.getData());
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }

    public Express createExpress(Consignee sender, Consignee receiver, Long shopId){
        PostExpressParam param = new PostExpressParam();
        param.setSender(sender);
        param.setReceiver(receiver);
        param.setShopLogisticId(0L);
        InternalReturnObject<Express> ret = this.expressMapper.createExpress("test", shopId, param); // TODO
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }

}
