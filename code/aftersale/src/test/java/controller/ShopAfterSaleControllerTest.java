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
public class ShopAfterSaleControllerTest {
    @Autowired
    private MockMvc mvc;

    private static String shop1Token;
    private static String shop2Token;

    private final String SHOP_FIND_AFTERSALE_BY_ID = "/shops/{shopId}/aftersales/{id}";
    private final String SHOP_CONFIRM_AFTERSALE = "/shops/{shopId}/aftersales/{id}/confirm";
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
        shop1Token = jwtHelper.createToken(1L, "shop1", 0L, 2, 3600);
        shop2Token = jwtHelper.createToken(2L, "shop2", 0L, 2, 3600);
    }

    @Test
    public void testShopFindAfterSaleById() throws Exception {

        InternalReturnObject<OrderItem> orderItemInternalReturnObject = new InternalReturnObject<>(new OrderItem());
        orderItemInternalReturnObject.getData().setId(1L);
        orderItemInternalReturnObject.getData().setOrderId(1L);
        orderItemInternalReturnObject.getData().setName("test");
        orderItemInternalReturnObject.getData().setQuantity(1);
        orderItemInternalReturnObject.getData().setPrice(100L);
        orderItemInternalReturnObject.getData().setDiscountPrice(0L);
        orderItemInternalReturnObject.getData().setPaymentId(1L);
        orderItemInternalReturnObject.getData().setAmount(100L);
        orderItemInternalReturnObject.getData().setDivAmount(0L);

        InternalReturnObject<Customer> customerInternalReturnObject = new InternalReturnObject<>(new Customer());
        customerInternalReturnObject.getData().setId(1L);
        customerInternalReturnObject.getData().setName("test");

        InternalReturnObject<Shop> shopInternalReturnObject = new InternalReturnObject<>(new Shop());
        shopInternalReturnObject.getData().setId(1L);
        shopInternalReturnObject.getData().setName("test");
        shopInternalReturnObject.getData().setStatus((byte)0);
        shopInternalReturnObject.getData().setConsignee(new Consignee("test","12345678910",1L,"test"));

        InternalReturnObject<Product> productInternalReturnObject = new InternalReturnObject<>(new Product());
        productInternalReturnObject.getData().setId(1L);
        productInternalReturnObject.getData().setName("test");

        InternalReturnObject<Express> expressInternalReturnObject = new InternalReturnObject<>(new Express());
        expressInternalReturnObject.getData().setId(1L);
        expressInternalReturnObject.getData().setBillCode("test");
        expressInternalReturnObject.getData().setStatus((byte)0);
        expressInternalReturnObject.getData().setSender(new Consignee("test","12345678910",1L,"test"));
        expressInternalReturnObject.getData().setReceiver(new Consignee("test","12345678910",1L,"test"));

        Mockito.when(orderItemMapper.getOrderItemById(Mockito.anyLong())).thenReturn(orderItemInternalReturnObject);
        Mockito.when(customerMapper.getCustomerById(Mockito.anyLong())).thenReturn(customerInternalReturnObject);
        Mockito.when(shopMapper.getShopById(Mockito.anyLong())).thenReturn(shopInternalReturnObject);
        Mockito.when(productMapper.getProductById(Mockito.anyLong())).thenReturn(productInternalReturnObject);
        Mockito.when(expressMapper.getExpressById(Mockito.any())).thenReturn(expressInternalReturnObject);
        Mockito.when(expressMapper.getExpressById(Mockito.any())).thenReturn(expressInternalReturnObject);

        mvc.perform(MockMvcRequestBuilders.get(SHOP_FIND_AFTERSALE_BY_ID, 1L, 1L)
                        .header("authorization", shop1Token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(0))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    public void testShopFindAfterSaleByIdGivenWrongShopId() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(SHOP_FIND_AFTERSALE_BY_ID, 2L, 1L)
                        .header("authorization", shop2Token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));
    }

    @Test
    public void testShopConfirmAfterSaleGivenNotExistASId() throws Exception {

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 2L, 256498L)
                        .header("authorization", shop2Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":false,\"conclusion,\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testShopConfirmAfterSaleGivenType0AndRefundOnly() throws Exception {

        InternalReturnObject<Refund> returnObject = new InternalReturnObject<>(new Refund());
        returnObject.getData().setId(1L);
        returnObject.getData().setAmount(100L);
        returnObject.getData().setDivAmount(0L);
        returnObject.getData().setStatus((byte)0);

        InternalReturnObject<OrderItem> orderItemInternalReturnObject = new InternalReturnObject<>(new OrderItem());
        orderItemInternalReturnObject.getData().setId(1L);
        orderItemInternalReturnObject.getData().setOrderId(1L);
        orderItemInternalReturnObject.getData().setName("test");
        orderItemInternalReturnObject.getData().setQuantity(1);
        orderItemInternalReturnObject.getData().setPrice(100L);
        orderItemInternalReturnObject.getData().setDiscountPrice(0L);
        orderItemInternalReturnObject.getData().setPaymentId(1L);
        orderItemInternalReturnObject.getData().setAmount(100L);
        orderItemInternalReturnObject.getData().setDivAmount(0L);

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(refundMapper.createRefund(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnObject);
        Mockito.when(orderItemMapper.getOrderItemById(Mockito.any())).thenReturn(orderItemInternalReturnObject);

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 3L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":true,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    @Test
    public void testShopConfirmAfterSaleGivenType0AndReturn() throws Exception {

        InternalReturnObject<Express> returnObject = new InternalReturnObject<>(new Express());
        returnObject.getData().setId(1L);
        returnObject.getData().setBillCode("test");
        returnObject.getData().setStatus((byte)0);
        returnObject.getData().setSender(new Consignee("test","12345678910",1L,"test"));
        returnObject.getData().setReceiver(new Consignee("test","12345678910",1L,"test"));


        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(expressMapper.createExpress(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnObject);

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 4L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":false,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    @Test
    public void testShopConfirmAfterSaleGivenType1() throws Exception {

        InternalReturnObject<Express> returnObject = new InternalReturnObject<>(new Express());
        returnObject.getData().setId(1L);
        returnObject.getData().setBillCode("test");
        returnObject.getData().setStatus((byte)0);
        returnObject.getData().setSender(new Consignee("test","12345678910",1L,"test"));
        returnObject.getData().setReceiver(new Consignee("test","12345678910",1L,"test"));

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(expressMapper.createExpress(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnObject);

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 5L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":false,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    @Test
    public void testShopConfirmAfterSaleGivenType2() throws Exception {

        InternalReturnObject<ServiceOrder> returnObject = new InternalReturnObject<>(new ServiceOrder());
        returnObject.getData().setId(1L);
        returnObject.getData().setType((byte)1);
        returnObject.getData().setDesc("test");

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        Mockito.when(serviceOrderMapper.createServiceOrder(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(returnObject);

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 6L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":false,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }


    @Test
    public void testShopRefuseAfterSale() throws Exception {

        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 7L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":false,\"refundOnly\":false,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));
    }

    @Test
    public void testShopConfirmAfterSaleGivenNotAllowedStatus() throws Exception{
        String consignee = "\"name\":\"test\",\"mobile\":\"12345678910\",\"regionId\":1,\"address\":\"test\"";

        mvc.perform(MockMvcRequestBuilders.post(SHOP_CONFIRM_AFTERSALE, 1L, 8L)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirm\":true,\"refundOnly\":false,\"conclusion\":\"test\",\"consignee\":{"+consignee+"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errno").value(ReturnNo.STATENOTALLOW.getErrNo()));
    }

}
