package cn.edu.xmu.oomall.service.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient("afterSale-service")
public interface AfterSaleMapper {
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    InternalReturnObject<AfterSale> findById(@RequestHeader String authorization,
                                             @PathVariable Long shopId,
                                             @PathVariable Long id);
}
