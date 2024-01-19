package cn.edu.xmu.oomall.service.mapper.openfeign.po;

import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;


public class AfterSale {
    private Long id;
    private IdNameTypeDto shop;

    private IdNameTypeDto product;
    private IdNameTypeDto customer;

    public Long getId() {
        return id;
    }
    public Long setId(Long id) {
        return this.id = id;
    }

    public IdNameTypeDto getShop() {
        return shop;
    }
    public void setShop(IdNameTypeDto shop) {
        this.shop = shop;
    }

    public IdNameTypeDto getProduct() {
        return product;
    }
    public void setProduct(IdNameTypeDto product) {
        this.product = product;
    }

    public IdNameTypeDto getCustomer() {
        return customer;
    }
    public void setCustomer(IdNameTypeDto customer) {
        this.customer = customer;
    }
}
