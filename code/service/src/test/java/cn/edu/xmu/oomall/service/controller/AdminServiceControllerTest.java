package cn.edu.xmu.oomall.service.controller;


import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.service.ServiceApplication;
import cn.edu.xmu.oomall.service.mapper.openfeign.AfterSaleMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Express;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Region;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = ServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AdminServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AfterSaleMapper afterSaleMapper;
    @MockBean
    private ExpressMapper expressMapper;
    @MockBean
    private RegionMapper regionMapper;
    private static String adminToken1;
    private final String ADMIN_MAINTAINERS_ID_SUSPEND = "/shops/{shopId}/maintainers/{id}/suspend";
    private final String ADMIN_MAINTAINERS_ID_CANCEL = "/shops/{shopId}/maintainers/{id}/cancel";

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken1 = jwtHelper.createToken(0L, "13088admin", 0L, 1, 3600);
    }
    //一般
    //无权限
    //状态不允许
    //存在进行中的服务单
    //服务商不存在
    /**
     * 暂停服务商
     * /shops/{shopId}/maintainers/{id}/suspend
     * 0 - 1
     */
    @Test
    public void testSuspendMaintainer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_MAINTAINERS_ID_SUSPEND,0L,1L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }
    /**
     * 暂停服务商--无权限
     * /shops/{shopId}/maintainers/{id}/suspend
     * 1 - 1
     */
    @Test
    public void testSuspendMaintainerGivenNoRight() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_MAINTAINERS_ID_SUSPEND,1L,1L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 暂停服务商--状态不允许
     * /shops/{shopId}/maintainers/{id}/suspend
     * 0 - 1
     */
    @Test
    public void testSuspendMaintainerGivenNotAllowedStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_MAINTAINERS_ID_SUSPEND,0L,2L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }

    /**
     * 暂停服务商--服务商不存在
     * /shops/{shopId}/maintainers/{id}/suspend
     * 0 - 3306
     */
    @Test
    public void testSuspendMaintainerGivenNotExistMid()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_MAINTAINERS_ID_SUSPEND,0L,3306L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * ----------------------------------------------以下为撤销服务商-------------------------------
     */
    /**
     * 撤销服务商
     * /shops/{shopId}/maintainers/{id}/cancel
     * 0 - 1
     */
    @Test
    public void testCancelMaintainer() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_MAINTAINERS_ID_CANCEL,0L,2L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(0));
    }
    /**
     * 撤销服务商--无权限
     * /shops/{shopId}/maintainers/{id}/cancel
     * 1 - 1
     */
    @Test
    public void testCancelMaintainerGivenNoRight() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_MAINTAINERS_ID_CANCEL,1L,1L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 撤销服务商--状态不允许
     * /shops/{shopId}/maintainers/{id}/cancel
     * 0 - 1
     */
    @Test
    public void testCancelMaintainerGivenNotAllowedStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_MAINTAINERS_ID_CANCEL,0L,1L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }
    /**
     * 撤销服务商--存在进行中的服务单
     * /shops/{shopId}/maintainers/{id}/cancel
     * 0 - 1
     */
    @Test
    public void testCancelMaintainerGivenOnGoingSOExist() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_MAINTAINERS_ID_CANCEL,0L,18L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.ONGOING_SERVICEORDER_EXIST.getErrNo()));
    }
    /**
     * 撤销服务商--服务商不存在
     * /shops/{shopId}/maintainers/{id}/cancel
     * 0 - 3306
     */
    @Test
    public void testCancelMaintainerGivenNotExistMid()throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_MAINTAINERS_ID_CANCEL,0L,3306L)
                .header("authorization", adminToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
}
