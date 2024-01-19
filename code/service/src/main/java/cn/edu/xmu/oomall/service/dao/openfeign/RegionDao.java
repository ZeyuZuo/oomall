package cn.edu.xmu.oomall.service.dao.openfeign;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.service.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RegionDao {
    private RegionMapper regionMapper;
    @Autowired
    public RegionDao(RegionMapper regionMapper){
        this.regionMapper = regionMapper;
    }
    public Region findById(Long id){
        InternalReturnObject<Region> ret = this.regionMapper.getRegionById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
    public List<Region> findParentsById(Long id){
        InternalReturnObject<List<Region>> ret = this.regionMapper.getParentsRegions(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
