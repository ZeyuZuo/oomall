package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.OrderItemMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.ServiceOrderMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostServiceOrder;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.ServiceOrder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceOrderDao {
    private ServiceOrderMapper serviceOrderMapper;
    @Autowired
    public ServiceOrderDao(ServiceOrderMapper orderItemMapper){
        this.serviceOrderMapper = orderItemMapper;
    }

    public ServiceOrder findById(Long id){
        InternalReturnObject<ServiceOrder> ret = this.serviceOrderMapper.getServiceOrderById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }

    public ServiceOrder createServiceOrder(Long shopId, Long id, PostServiceOrder param){
        InternalReturnObject<ServiceOrder> ret = this.serviceOrderMapper.createServiceOrder("test", shopId, id, param);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
