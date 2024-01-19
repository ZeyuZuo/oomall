package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.service.MaintainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@Transactional
public class AdminServiceController {
    private final Logger logger = LoggerFactory.getLogger(AdminServiceController.class);

    private final MaintainerService maintainerService;

    @Autowired
    public AdminServiceController(MaintainerService maintainerService) {
        this.maintainerService = maintainerService;
    }

    /**
     * 暂停服务商
     */
    @PutMapping("/maintainers/{id}/suspend")
    @Audit(departName = "admin")
    public ReturnObject suspendMaintainerById(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user) {
        if (!PLATFORM.equals(shopId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务", id, shopId));
        }
        this.maintainerService.suspendMaintainer(id, user);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 撤销服务商
     */
    @DeleteMapping("/maintainers/{id}/cancel")
    @Audit(departName = "admin")
    public ReturnObject cancelMaintainerById(@PathVariable Long shopId, @PathVariable Long id, @LoginUser UserDto user) {
        if (!PLATFORM.equals(shopId)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务", id, shopId));
        }
        this.maintainerService.cancelMaintainer(id, user);
        return new ReturnObject(ReturnNo.OK);
    }


}