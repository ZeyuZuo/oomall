package cn.edu.xmu.oomall.aftersale.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.params.PostRefundParam;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.OrderItem;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Product;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Refund;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("payment-service")
public interface RefundMapper {
    @PostMapping("/internal/shops/{shopId}/payments/{id}/refunds")
    InternalReturnObject<Refund> createRefund(@RequestHeader (name = "authorization", required = true) String authorization,
                                              @PathVariable Long shopId,
                                              @PathVariable Long id,
                                              @RequestBody PostRefundParam param);

    @GetMapping("/shops/{shopId}/refunds/{id}")
    InternalReturnObject<Refund> getRefundById(@PathVariable Long shopId, @PathVariable Long id);
}
