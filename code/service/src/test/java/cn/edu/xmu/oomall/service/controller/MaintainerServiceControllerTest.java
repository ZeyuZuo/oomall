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
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Consignee;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Express;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.Region;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(classes = ServiceApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class MaintainerServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AfterSaleMapper afterSaleMapper;
    @MockBean
    private ExpressMapper expressMapper;
    @MockBean
    private RegionMapper regionMapper;
    @MockBean
    private RedisUtil redisUtil;
    private static String adminToken;
    private static String maintainer1Token;
    private final String ADMIN_ACCEPT_SERVICE_ORDER = "/maintainers/{mid}/serviceOrder/{id}/accept";
    private final String ADMIN_CANCEL_SERVICE_ORDER = "/maintainers/{mid}/serviceOrder/{id}/cancel";
    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        maintainer1Token = jwtHelper.createToken(1L, "maintainer1", 0L, 2, 3600);
    }
    /**--------------------------sendServiceOrder---------------------------------*/
    /**
     * 服务商接收服务单(sendServiceOrder
     */
    @Test
    public void testAcceptServiceOrder1() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);
        InternalReturnObject<Express> expressInternalReturnObject = new InternalReturnObject<>(new Express());
        expressInternalReturnObject.getData().setId(1L);
        expressInternalReturnObject.getData().setBillCode("test");
        expressInternalReturnObject.getData().setStatus((byte) 1);
        expressInternalReturnObject.getData().setSender(new Consignee());
        expressInternalReturnObject.getData().getSender().setAddress("test");
        expressInternalReturnObject.getData().getSender().setMobile("test");
        expressInternalReturnObject.getData().getSender().setName("test");
        expressInternalReturnObject.getData().getSender().setRegionId(1L);
        expressInternalReturnObject.getData().setReceiver(new Consignee());
        expressInternalReturnObject.getData().getReceiver().setAddress("test");
        expressInternalReturnObject.getData().getReceiver().setMobile("test");
        expressInternalReturnObject.getData().getReceiver().setName("test");
        expressInternalReturnObject.getData().getReceiver().setRegionId(1L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);
        Mockito.when(expressMapper.createExpress(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(expressInternalReturnObject);


        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 1, 59)
                        .header("authorization", maintainer1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }
    /**
     * 服务商不可接收服务单(sendServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenCannotAccept1() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 45)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 接收服务单--服务单不存在(sendServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenSONotExist1() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 999)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    /**
     * 接收服务单--服务单不是待接收状态(sendServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenSOStateNotWaiting1() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }


    //-----------------------------------onSpotServiceOrder--------------------------------------------

    /**
     * 服务商接收服务单(onSpotServiceOrder
     */
    //         mid/serviceOrderId
    @Test
    public void testAcceptServiceOrder2() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);


        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 1, 45)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }
    /**
     * 服务商不可接收服务单(onSpotServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenCannotAccept2() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 45)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 接收服务单--服务单不存在(onSpotServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenSONotExist2() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 404)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * 接收服务单--服务单不是待接收状态(onSpotServiceOrder
     */
    @Test
    public void testAcceptServiceOrderGivenSOStateNotWaiting2() throws Exception {
        InternalReturnObject<Region> regionInternalReturnObject = new InternalReturnObject<>(new Region());
        regionInternalReturnObject.getData().setId(1L);
        InternalReturnObject<List<Region>> fatherRegionInternalReturnObject = new InternalReturnObject<>(List.of(new Region()));
        fatherRegionInternalReturnObject.getData().get(0).setId(2L);

        Mockito.when(regionMapper.getRegionById(Mockito.any())).thenReturn(regionInternalReturnObject);
        Mockito.when(regionMapper.getParentsRegions(Mockito.any())).thenReturn(fatherRegionInternalReturnObject);

        mockMvc.perform(put(ADMIN_ACCEPT_SERVICE_ORDER, 0, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"maintainerName\":\"test\",\"maintainerMobile\":\"test\",\"regionId\":2,\"address\":\"test\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }



    //----------------------------------服务商撤销服务商

    /**
     * 服务商撤销服务单(sendServiceOrder ---已签收
     */
    //"/maintainers/{mid}/serviceOrder/{id}/cancel"
    @Test
    public void testCancelServiceOrder1() throws Exception {
        InternalReturnObject<Express> expressInternalReturnObject = new InternalReturnObject<>(new Express());
        expressInternalReturnObject.getData().setId(1L);
        expressInternalReturnObject.getData().setBillCode("test");
        expressInternalReturnObject.getData().setStatus((byte) 2);
        expressInternalReturnObject.getData().setSender(new Consignee());
        expressInternalReturnObject.getData().getSender().setAddress("test");
        expressInternalReturnObject.getData().getSender().setMobile("test");
        expressInternalReturnObject.getData().getSender().setName("test");
        expressInternalReturnObject.getData().getSender().setRegionId(1L);
        expressInternalReturnObject.getData().setReceiver(new Consignee());
        expressInternalReturnObject.getData().getReceiver().setAddress("test");
        expressInternalReturnObject.getData().getReceiver().setMobile("test");
        expressInternalReturnObject.getData().getReceiver().setName("test");
        expressInternalReturnObject.getData().getReceiver().setRegionId(1L);

        Mockito.when(expressMapper.createExpress(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(expressInternalReturnObject);
        Mockito.when(expressMapper.getExpressById(Mockito.any())).thenReturn(expressInternalReturnObject);

        mockMvc.perform(put(ADMIN_CANCEL_SERVICE_ORDER, 1, 77)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    /**
     * 服务商撤销服务单(onSpotServiceOrder
     */
    @Test
    public void testCancelServiceOrder2() throws Exception {

        mockMvc.perform(put(ADMIN_CANCEL_SERVICE_ORDER, 201, 2)
                        .header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }
    /**
     * 服务商撤销服务单--服务单不存在(onSpotServiceOrder
     */
    @Test
    public void testCancelServiceOrderGivenNonExistentServiceOrder2() throws Exception {

        mockMvc.perform(put(ADMIN_CANCEL_SERVICE_ORDER, 0, 999)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    /**
     * 服务商撤销服务单--不是服务商的服务单(onSpotServiceOrder
     */
    @Test
    public void testCancelServiceOrderGivenWrongMaintainer2() throws Exception {

        mockMvc.perform(put(ADMIN_CANCEL_SERVICE_ORDER, 201, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }


}
