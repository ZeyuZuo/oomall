package cn.edu.xmu.oomall.aftersale.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("orderItem-service")
public interface OrderItemMapper {
    @GetMapping("/orderItems/{id}")
    InternalReturnObject<OrderItem> getOrderItemById(@PathVariable Long id);
}
