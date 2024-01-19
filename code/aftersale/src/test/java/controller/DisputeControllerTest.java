package controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.aftersale.AfterSaleApplication;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.*;
import cn.edu.xmu.oomall.aftersale.mapper.openfeign.po.*;
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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AfterSaleApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class DisputeControllerTest {
    private static String customer1Token;
    private static String customer2Token;
    private static String customer9Token;

    private static final String createDisputeUrl = "/aftersales/{id}/disputes";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerMapper customerMapper;
    @MockBean
    private ExpressMapper expressMapper;
    @MockBean
    private OrderItemMapper orderItemMapper;
    @MockBean
    private ProductMapper productMapper;
    @MockBean
    private RefundMapper refundMapper;
    @MockBean
    private ServiceOrderMapper serviceOrderMapper;
    @MockBean
    private ShopMapper shopMapper;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        customer1Token = jwtHelper.createToken(1L, "customer1", 2L, 2, 3600);
        customer2Token = jwtHelper.createToken(2L, "customer2", 2L, 2, 3600);
        customer9Token = jwtHelper.createToken(9L, "customer9", 2L, 2, 3600);
    }

    @Test
    public void testCreateDisputeGivenWrongUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(createDisputeUrl, 10L)
                        .header("authorization", customer2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"test\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }

    @Test
    public void testCreateDisputeGivenNotExistASId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(createDisputeUrl, 564L)
                        .header("authorization", customer1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"test\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    @Test
    public void testCreateDisputeGivenWrongStatus() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(createDisputeUrl, 1L)
                        .header("authorization", customer1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }


    @Test
    public void testCreateDispute() throws Exception {

        InternalReturnObject<Customer> customerInternalReturnObject = new InternalReturnObject<>(new Customer());
        customerInternalReturnObject.getData().setId(1L);
        customerInternalReturnObject.getData().setName("test");

        InternalReturnObject<Shop> shopInternalReturnObject = new InternalReturnObject<>(new Shop());
        shopInternalReturnObject.getData().setId(1L);
        shopInternalReturnObject.getData().setName("test");
        shopInternalReturnObject.getData().setStatus((byte)0);
        shopInternalReturnObject.getData().setConsignee(new Consignee("test","12345678910",1L,"test"));

        Mockito.when(customerMapper.getCustomerById(any())).thenReturn(customerInternalReturnObject);
        Mockito.when(shopMapper.getShopById(any())).thenReturn(shopInternalReturnObject);

        mvc.perform(MockMvcRequestBuilders.post(createDisputeUrl, 9L)
                        .header("authorization", customer9Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reason\":\"test\"}"))
                .andExpect(status().isCreated());
    }
}
