package cn.edu.xmu.oomall.aftersale.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Shop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("shop-service")
public interface ShopMapper {
    @GetMapping("/shops/{id}")
    InternalReturnObject<Shop> getShopById(@PathVariable Long id);
}
