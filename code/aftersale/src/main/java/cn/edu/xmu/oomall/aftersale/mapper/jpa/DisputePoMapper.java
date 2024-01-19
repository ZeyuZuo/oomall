package cn.edu.xmu.oomall.aftersale.mapper.jpa;

import cn.edu.xmu.oomall.aftersale.mapper.po.DisputePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisputePoMapper extends JpaRepository<DisputePo, Long>
{
    List<DisputePo> findByAfterSaleId(Long afterSaleId);
}
