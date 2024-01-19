package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.oomall.aftersale.mapper.openfeign.RefundMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostRefundParam;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Refund;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.OrderItemMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;

@Repository
public class RefundDao {
    private RefundMapper refundMapper;
    @Autowired
    public RefundDao(RefundMapper refundMapper){
        this.refundMapper = refundMapper;
    }

    public Refund findById(Long shopId, Long id){
        InternalReturnObject<Refund> ret = this.refundMapper.getRefundById(shopId, id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }

    public Refund createRefund(Long shopId, Long id, PostRefundParam param){
        InternalReturnObject<Refund> ret = this.refundMapper.createRefund("test", shopId, id, param);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
