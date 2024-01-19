package cn.edu.xmu.oomall.service.mapper.jpa;

import cn.edu.xmu.oomall.service.mapper.po.MaintainerPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaintainerPoMapper extends JpaRepository<MaintainerPo, Long>
{
}


