package cn.edu.xmu.oomall.aftersale.mapper.jpa;

import cn.edu.xmu.oomall.aftersale.mapper.po.AfterSalePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AfterSalePoMapper extends JpaRepository<AfterSalePo, Long> {
}
