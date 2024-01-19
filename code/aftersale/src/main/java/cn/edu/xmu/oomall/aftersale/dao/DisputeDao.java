package cn.edu.xmu.oomall.aftersale.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.aftersale.mapper.jpa.DisputePoMapper;
import cn.edu.xmu.oomall.aftersale.mapper.po.DisputePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.oomall.aftersale.dao.bo.Dispute;
import cn.edu.xmu.oomall.aftersale.mapper.jpa.AfterSalePoMapper;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
@RefreshScope
public class DisputeDao {
    private final static Logger logger = LoggerFactory.getLogger(DisputeDao.class);
    private DisputePoMapper disputePoMapper;

    @Autowired
    public DisputeDao(DisputePoMapper disputePoMapper){
        this.disputePoMapper = disputePoMapper;
    }

    /**
     * 查找仲裁申请记录(list)
     * @param ASId 售后单id
     */
    public List<Dispute> findByASId(Long ASId) {//待写
        if(ASId == null){
            throw new IllegalArgumentException("id can not be null");
        }
        logger.debug("findByASId: ASId = {}", ASId);
        List<DisputePo> disputes = this.disputePoMapper.findByAfterSaleId(ASId);
        if(disputes == null){
            return null;
        }
        else {
            List<Dispute> ret = new ArrayList<>(disputes.size());
            for (DisputePo disputePo : disputes) {
                Dispute dispute = CloneFactory.copy(new Dispute(), disputePo);
                ret.add(dispute);
            }
            logger.debug("findByASId: retrieve from database List<Dispute> = {}", ret);
            return ret;
        }
    }

    /**
     * 创建仲裁
     * @param bo
     * @param user
     */
    public Dispute insert(Dispute bo, UserDto user) {//待写
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());
        DisputePo po = CloneFactory.copy(new DisputePo(), bo);
        logger.debug("save: po = {}", po);
        po = disputePoMapper.save(po);
        bo.setId(po.getId());
        return bo;
    }
}
