package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.controller.Dto.MaintainerMessageDto;
import cn.edu.xmu.oomall.service.controller.Dto.ServiceOrderCreateDto;
import cn.edu.xmu.oomall.service.controller.vo.MaintainerMessageVo;
import cn.edu.xmu.oomall.service.controller.vo.ServiceOrderCreateVo;
import cn.edu.xmu.oomall.service.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.edu.xmu.javaee.core.util.CloneFactory;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/maintainers/{MaintainerId}", produces = "application/json;charset=UTF-8")
@Transactional
public class MaintainerServiceController {
    private final Logger logger = LoggerFactory.getLogger(MaintainerServiceController.class);

    private final ServiceOrderService serviceorderService;

    @Autowired
    public MaintainerServiceController(ServiceOrderService serviceorderService) {
        this.serviceorderService = serviceorderService;
    }

    /**
     * 服务商接收服务单
     */
    @Audit(departName = "maintainer")
    @PutMapping("/serviceOrder/{ServiceOrderId}/accept")
    public ReturnObject acceptServiceOrder(@PathVariable Long MaintainerId,
                                           @PathVariable Long ServiceOrderId,
                                           @RequestBody @Validated MaintainerMessageVo maintainerMessageVo,
                                           @LoginUser UserDto userDto){
        MaintainerMessageDto dto = CloneFactory.copy(new MaintainerMessageDto(), maintainerMessageVo);
        serviceorderService.acceptServiceOrder(MaintainerId,ServiceOrderId, dto, userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    /**
     * 服务商撤销服务单
     */
    @Audit(departName = "maintainer")
    @PutMapping("/serviceOrder/{ServiceOrderId}/cancel")
    public ReturnObject cancelServiceOrder( @PathVariable Long MaintainerId,
                                            @PathVariable Long ServiceOrderId,
                                            @LoginUser UserDto userDto){

        serviceorderService.cancelServiceOrder(MaintainerId,ServiceOrderId, userDto);
        return new ReturnObject(ReturnNo.OK);
    }
}