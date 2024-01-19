package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.ProductMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Product;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
    private ProductMapper productMapper;

    @Autowired
    public ProductDao(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public Product findById(Long id){
        InternalReturnObject<Product> ret = this.productMapper.getProductById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
