package cn.edu.xmu.oomall.service.mapper.openfeign;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.service.mapper.Param.PostExpressParam;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Express;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("express-service")
public interface ExpressMapper {
    @GetMapping("/internal/expresses/{id}")
    InternalReturnObject<Express> getExpressById(@PathVariable Long id);

    @PostMapping("/internal/shops/{shopId}/packages")
    InternalReturnObject<Express> createExpress(@RequestHeader(name = "authorization", required = true) String authorization,
                                                @PathVariable Long shopId,
                                                @RequestBody PostExpressParam param);
}
