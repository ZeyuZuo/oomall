package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.bo.Maintainer;
import cn.edu.xmu.oomall.service.mapper.jpa.MaintainerPoMapper;
import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;

@Repository
public class MaintainerDao {
    private final static String KEY = "R%d";
    private final static Logger logger = LoggerFactory.getLogger(MaintainerDao.class);

    private MaintainerPoMapper maintainerPoMapper;
    private ProductServiceDao productServiceDao;
    private ServiceOrderDao serviceOrderDao;
    @Autowired
    public MaintainerDao(MaintainerPoMapper maintainerPoMapper,
                         ProductServiceDao productServiceDao,
                         ServiceOrderDao serviceOrderDao) {
        this.maintainerPoMapper = maintainerPoMapper;
        this.productServiceDao = productServiceDao;
        this.serviceOrderDao = serviceOrderDao;
    }
    public Maintainer findById(Long id) {
        Optional<MaintainerPo> po = this.maintainerPoMapper.findById(id);
        if (po.isPresent()) {
            MaintainerPo maintainerPo = po.get();
            Maintainer bo = CloneFactory.copy(new Maintainer(), maintainerPo);
            bo.setMaintainerDao(this);
            bo.setProductServiceDao(this.productServiceDao);
            bo.setServiceOrderDao(this.serviceOrderDao);
            return bo;
        }
        else {
            return null;
        }
    }

    public String save(Maintainer bo, UserDto user){
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());
        MaintainerPo po = CloneFactory.copy(new MaintainerPo(), bo);
        logger.debug("save: po = {}", po);
        MaintainerPo updatedPo = maintainerPoMapper.save(po);
        if(IDNOTEXIST.equals(updatedPo.getId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务商", bo.getId()));
        }
        return String.format(KEY, bo.getId());
    }



}