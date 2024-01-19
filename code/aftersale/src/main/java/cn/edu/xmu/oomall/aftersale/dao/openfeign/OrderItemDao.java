package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.OrderItemMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class OrderItemDao {
    private OrderItemMapper orderItemMapper;
    private final static Logger logger = LoggerFactory.getLogger(AfterSaleDao.class);

    @Autowired
    public OrderItemDao(OrderItemMapper orderItemMapper){
        this.orderItemMapper = orderItemMapper;
    }

    public OrderItem findById(Long id){
        InternalReturnObject<OrderItem> ret = this.orderItemMapper.getOrderItemById(id);
        logger.debug("getOrderItemById ret = " + ret + "id = " + id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
