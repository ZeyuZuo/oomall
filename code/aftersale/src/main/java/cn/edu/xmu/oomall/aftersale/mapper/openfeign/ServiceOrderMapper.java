package cn.edu.xmu.oomall.aftersale.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostServiceOrder;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Product;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.ServiceOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("service-service")
public interface ServiceOrderMapper {
    @GetMapping("/serviceOrders/{id}")
    InternalReturnObject<ServiceOrder> getServiceOrderById(@PathVariable Long id);

    @PostMapping("/internal/shops/{shopId}/afterSales/{id}/createService")
    InternalReturnObject<ServiceOrder> createServiceOrder(@RequestHeader String authorization,
                                                          @PathVariable Long shopId,
                                                          @PathVariable Long id,
                                                          @RequestBody PostServiceOrder param);
}
