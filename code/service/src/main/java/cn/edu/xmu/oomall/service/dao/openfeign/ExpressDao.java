package cn.edu.xmu.oomall.service.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.service.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.service.mapper.Param.PostExpressParam;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Consignee;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Express;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ExpressDao {
    private ExpressMapper expressMapper;


    public ExpressDao(ExpressMapper expressMapper){
        this.expressMapper = expressMapper;
    }

    public Express findById(Long id){
        InternalReturnObject<Express> ret = this.expressMapper.getExpressById(id);
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
