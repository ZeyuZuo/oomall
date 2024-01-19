package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.controller.Dto.ServiceOrderCreateDto;
import cn.edu.xmu.oomall.service.controller.Dto.SimpleServiceOrderDto;
import cn.edu.xmu.oomall.service.controller.vo.ServiceOrderCreateVo;
import cn.edu.xmu.oomall.service.dao.bo.ServiceOrder;
import cn.edu.xmu.oomall.service.service.ServiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cn.edu.xmu.javaee.core.util.CloneFactory;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/internal", produces = "application/json;charset=UTF-8")
@Transactional
public class InternalServiceController {

    private final Logger logger = LoggerFactory.getLogger(InternalServiceController.class);

    private final ServiceOrderService serviceorderService;

    @Autowired
    public InternalServiceController(ServiceOrderService serviceorderService) {
        this.serviceorderService = serviceorderService;
    }

    /**
     * 创建服务单
     */
    @Audit(departName = "shops")
    @PostMapping("/shops/{shopId}/aftersales/{afterSaleId}/createService")
    public ReturnObject createServiceOrder(@PathVariable Long shopId,
                                      @PathVariable Long afterSaleId,
                                      @RequestBody @Validated ServiceOrderCreateVo ServiceOrderCreateVo,
                                      @LoginUser UserDto userDto){
        ServiceOrderCreateDto dto = CloneFactory.copy(new ServiceOrderCreateDto(), ServiceOrderCreateVo);
        ServiceOrder serviceOrder = serviceorderService.createServiceOrder(shopId, afterSaleId, dto, userDto);
        return new ReturnObject(CloneFactory.copy(new SimpleServiceOrderDto(), serviceOrder));
    }

}