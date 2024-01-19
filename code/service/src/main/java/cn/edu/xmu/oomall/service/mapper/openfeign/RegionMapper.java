package cn.edu.xmu.oomall.service.mapper.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Region;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@FeignClient("region-service")
public interface RegionMapper {
    @GetMapping("/regions/{id}/")
    InternalReturnObject<Region> getRegionById(@PathVariable Long id);
    @GetMapping("/internal/regions/{id}/parents")
    InternalReturnObject<List<Region>> getParentsRegions(@PathVariable Long id);

}
