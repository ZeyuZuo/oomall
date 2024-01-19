package cn.edu.xmu.oomall.service.mapper.jpa;

import cn.edu.xmu.oomall.service.mapper.po.ProductServicePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductServicePoMapper extends JpaRepository<ProductServicePo, Long>
{

    List<ProductServicePo> findByMaintainerId(Long maintainerId);
    List<ProductServicePo> findByServiceTypeAndProductIdAndShopId(Byte serviceType,Long productId,Long shopId);

}


