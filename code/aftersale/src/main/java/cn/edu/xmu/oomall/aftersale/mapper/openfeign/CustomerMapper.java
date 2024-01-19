package cn.edu.xmu.oomall.aftersale.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("customer-service")
public interface CustomerMapper {
    @GetMapping("/customers/{id}")
    InternalReturnObject<Customer> getCustomerById(@PathVariable Long id);
}
