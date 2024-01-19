package cn.edu.xmu.oomall.service.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.MaintainerDao;
import cn.edu.xmu.oomall.service.dao.ProductServiceDao;
import cn.edu.xmu.oomall.service.dao.bo.Maintainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MaintainerService {
    private MaintainerDao maintainerDao;
    @Autowired
    public MaintainerService(MaintainerDao maintainerDao) {
        this.maintainerDao = maintainerDao;
    }
    /**
     * 暂停服务商
     * @param id
     * @param user
     */
    public void suspendMaintainer(Long id, UserDto user) {
        Maintainer maintainer = this.maintainerDao.findById(id);
        if(maintainer==null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务商", id));
        }
        if(!Objects.equals(maintainer.getStatus(), Maintainer.VALID)){
            throw new BusinessException(ReturnNo.STATENOTALLOW);
        }
        maintainer.suspend(id,user);
    }

    /**
     * 撤销服务商
     * @param id
     * @param user
     */
    public void cancelMaintainer(Long id, UserDto user) {
        Maintainer maintainer = this.maintainerDao.findById(id);
        if(maintainer==null){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务商", id));
        }
        if(!Objects.equals(maintainer.getStatus(), Maintainer.SUSPENDED)){
            throw new BusinessException(ReturnNo.STATENOTALLOW);
        }
        maintainer.cancel(id,user);
    }
}