package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.bo.Maintainer;
import cn.edu.xmu.oomall.service.dao.bo.ProductService;
import cn.edu.xmu.oomall.service.mapper.jpa.MaintainerPoMapper;
import cn.edu.xmu.oomall.service.mapper.jpa.ProductServicePoMapper;
import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
import cn.edu.xmu.oomall.service.mapper.po.ProductServicePo;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;


@Repository
public class ProductServiceDao {
    private final static String KEY = "R%d";
    private final static Logger logger = LoggerFactory.getLogger(ProductServiceDao.class);

    private ProductServicePoMapper ProductServicePoMapper;
    @Autowired
    public ProductServiceDao(ProductServicePoMapper ProductServicePoMapper) {
        this.ProductServicePoMapper = ProductServicePoMapper;
    }
    public List<ProductService> findByMaintainerId(Long Mid) {
        List<ProductServicePo> poList = this.ProductServicePoMapper.findByMaintainerId(Mid);

        // 检查poList是否为null
        if (poList == null) {
            return null;
        }

        List<ProductService> boList = new ArrayList<>();

        for (ProductServicePo po : poList) {
            ProductService bo = CloneFactory.copy(new ProductService(), po);
            bo.setProductServiceDao(this);
            boList.add(bo);
        }

        return boList;
    }

    public String save(ProductService bo, UserDto user){
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        ProductServicePo po = CloneFactory.copy(new ProductServicePo(), bo);
        logger.debug("save: po = {}", po);
        ProductServicePo updatedPo = ProductServicePoMapper.save(po);
        if(IDNOTEXIST.equals(updatedPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务", bo.getId()));
        }
        return String.format(KEY, bo.getId());
    }

    /**
     * 获取ProductServiceList
     * @param serviceType
     * @param productId
     * @param shopId
     * @return
     */
    public List<ProductService> findByServiceTypeAndProductIdAndShopId(Byte serviceType,Long productId,Long shopId){
        List<ProductServicePo> poList = this.ProductServicePoMapper.findByServiceTypeAndProductIdAndShopId(serviceType,productId,shopId);

        // 检查poList是否为null
        if (poList == null) {
            return null;
        }
        List<ProductService> boList = new ArrayList<>();
        for (ProductServicePo po : poList) {
            ProductService bo = CloneFactory.copy(new ProductService(), po);
            boList.add(bo);
        }
        return boList;
    }

}