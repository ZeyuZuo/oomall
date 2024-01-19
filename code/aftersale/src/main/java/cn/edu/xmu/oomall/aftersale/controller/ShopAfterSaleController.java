package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.aftersale.controller.dto.AfterSaleDto;
import cn.edu.xmu.oomall.aftersale.controller.dto.ConfirmDto;
import cn.edu.xmu.oomall.aftersale.controller.vo.ConfirmVo;
import cn.edu.xmu.oomall.aftersale.dao.AfterSaleDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.AfterSale;
import cn.edu.xmu.oomall.aftersale.service.AfterSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
@Transactional
public class ShopAfterSaleController{
    private AfterSaleService afterSaleService;
    private final static Logger logger = LoggerFactory.getLogger(ShopAfterSaleController.class);

    @Autowired
    public ShopAfterSaleController(AfterSaleService afterSaleService){
        this.afterSaleService = afterSaleService;
    }

    // 审核售后单
    @PostMapping("aftersales/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject confirmAfterSale(@PathVariable Long shopId,
                                         @PathVariable Long id,
                                         @RequestBody ConfirmVo confirm,
                                         @LoginUser UserDto userDto){

        ConfirmDto confirmDto = CloneFactory.copy(new ConfirmDto(),confirm);
        this.afterSaleService.confirmAfterSale(shopId,id,confirmDto,userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    @GetMapping("aftersales/{id}")
    @Audit(departName = "shops")
    public ReturnObject getAfterSaleById(@PathVariable Long shopId,
                                         @PathVariable Long id){

        AfterSale afterSale = this.afterSaleService.shopFindById(shopId,id);
        AfterSaleDto afterSaleDto = new AfterSaleDto(afterSale);
        return new ReturnObject(afterSaleDto);
    }
}
