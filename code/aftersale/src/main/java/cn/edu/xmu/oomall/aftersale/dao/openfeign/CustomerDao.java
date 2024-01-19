package cn.edu.xmu.oomall.aftersale.dao.openfeign;


import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.CustomerMapper;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.Customer;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao {
    private CustomerMapper customerMapper;

    @Autowired
    public CustomerDao(CustomerMapper customerMapper){
        this.customerMapper = customerMapper;
    }

    public Customer findById(Long id){
        InternalReturnObject<Customer> ret = this.customerMapper.getCustomerById(id);
        ReturnNo returnNo = ReturnNo.getByCode(ret.getErrno());
        if (!returnNo.equals(ReturnNo.OK)) {
            throw new BusinessException(returnNo, ret.getErrmsg());
        }else{
            return ret.getData();
        }
    }
}
