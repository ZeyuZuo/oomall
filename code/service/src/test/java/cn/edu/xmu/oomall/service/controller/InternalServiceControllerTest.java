package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.IdNameTypeDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.service.ServiceApplication;
import cn.edu.xmu.oomall.service.controller.vo.ServiceOrderCreateVo;
import cn.edu.xmu.oomall.service.mapper.openfeign.AfterSaleMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.ExpressMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.service.mapper.openfeign.po.AfterSale;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ServiceApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class InternalServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AfterSaleMapper afterSaleMapper;
    @MockBean
    private ExpressMapper expressMapper;
    @MockBean
    private RegionMapper regionMapper;
    private static final String ADMIN_CREATE_SERVICE_ORDER = "/internal/shops/{shopId}/aftersales/{id}/createService";
    private static String adminToken;
    private static String shop1Token;
    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(0L, "13088admin", 0L, 1, 3600);
        shop1Token = jwtHelper.createToken(1L, "shops1", 1L, 2, 3600);
    }

    //---------------------------sendServiceOrder-----------------------------------------------------------------
    /** 创建服务单(sendServiceOrder)
     */
    @Test
    void testCreateServiceOrder1() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);


        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1L, 1L)
                .header("authorization", shop1Token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":0,\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }
    /**
     * 创建服务单--售后单不存在(sendServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASNOTEXIST1() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn( null);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1L, 3L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":0,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    /**
     * 创建服务单--售后单不属于该店铺(sendServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASNOTBELONG1() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(100L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1L, 3L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":0,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 创建服务单--售后单已有服务单(sendServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASHAVE1() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1, 800)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":0,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AFTERSALE_EXIST_SERVICEORDER.getErrNo()));

    }

    /**
     * ---------------------------opSpotServiceOrder-----------------------------------------------------------------
     */

    /**
     * 创建服务单(onSpotServiceOrder)
     */
    @Test
    void testCreateServiceOrder2() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":1,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    /**
     * 创建服务单--售后单不存在(onSpotServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASNOTEXIST2() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":1,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
    /**
     * 创建服务单--售后单不属于该店铺(onSpotServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASNOTBELONG2() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(100L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":1,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }
    /**
     * 创建服务单--售后单已有服务单(onSpotServiceOrder)
     */
    @Test
    void testCreateServiceOrderGivenASHAVE2() throws Exception {
        InternalReturnObject<AfterSale> afterSaleInternalReturnObject = new InternalReturnObject<>(new AfterSale());
        IdNameTypeDto shop = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto product = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type
        IdNameTypeDto customer = new IdNameTypeDto(1L,"test",(byte)0);//id,name,type

        afterSaleInternalReturnObject.getData().setId(1L);
        afterSaleInternalReturnObject.getData().setShop(shop);
        afterSaleInternalReturnObject.getData().setProduct(product);
        afterSaleInternalReturnObject.getData().setCustomer(customer);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(afterSaleMapper.findById(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(afterSaleInternalReturnObject);

        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CREATE_SERVICE_ORDER, 1, 800)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":1,\"consignee\":{"+consignee+"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AFTERSALE_EXIST_SERVICEORDER.getErrNo()));
    }

}
