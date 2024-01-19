package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Shop;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao {
    private ShopMapper shopMapper;
    @Autowired
    public ShopDao(ShopMapper shopMapper){
        this.shopMapper = shopMapper;
    }

    public Shop findById(Long id){
        InternalReturnObject<Shop> ret = this.shopMapper.getShopById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
