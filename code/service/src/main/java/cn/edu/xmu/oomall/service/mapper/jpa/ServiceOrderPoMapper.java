package cn.edu.xmu.oomall.service.mapper.jpa;

import cn.edu.xmu.oomall.service.mapper.po.ServiceOrderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceOrderPoMapper extends JpaRepository<ServiceOrderPo, Long>
{


    ServiceOrderPo findByAftersalesId(Long aftersalesId);

    List<ServiceOrderPo> findByMaintainerIdAndStatus(Long maintainerId, Byte status);
}

