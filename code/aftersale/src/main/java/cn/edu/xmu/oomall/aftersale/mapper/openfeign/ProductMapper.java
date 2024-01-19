package cn.edu.xmu.oomall.aftersale.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("product-service")
public interface ProductMapper {
    @GetMapping("/products/{id}")
    InternalReturnObject<Product> getProductById(@PathVariable Long id);
}
