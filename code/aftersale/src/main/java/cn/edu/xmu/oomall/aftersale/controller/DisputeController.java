package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.aftersale.controller.dto.AfterSaleDto;
import cn.edu.xmu.oomall.aftersale.controller.dto.ConfirmDto;
import cn.edu.xmu.oomall.aftersale.controller.dto.DisputeIdDto;
import cn.edu.xmu.oomall.aftersale.controller.vo.ConfirmVo;
import cn.edu.xmu.oomall.aftersale.controller.vo.ReasonVo;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.oomall.aftersale.dao.bo.Dispute;
import cn.edu.xmu.oomall.aftersale.service.AfterSaleService;
import cn.edu.xmu.oomall.aftersale.service.DisputeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class DisputeController {
    private DisputeService disputeService;
    private final static Logger logger = LoggerFactory.getLogger(ShopAfterSaleController.class);

    @Autowired
    public DisputeController(DisputeService disputeService){
        this.disputeService = disputeService;
    }

    @PostMapping("aftersales/{id}/disputes")
    public ReturnObject createDispute(@PathVariable Long id,
                                      @RequestBody ReasonVo reasonVo,
                                      @LoginUser UserDto userDto){
        Dispute dispute = this.disputeService.createDispute(id,reasonVo.getReason(),userDto);
        DisputeIdDto disputeIdDto = CloneFactory.copy(new DisputeIdDto(),dispute);
        return new ReturnObject(ReturnNo.CREATED, disputeIdDto);
    }
}
