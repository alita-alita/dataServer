//package cn.idicc.taotie.api.controller;
//
//import cn.idicc.taotie.api.controller.spider.SpiderAcceptorController;
//import cn.idicc.taotie.api.request.SpiderEntity;
//import cn.idicc.taotie.infrastructment.enums.BusinessTypeEnum;
//import cn.idicc.taotie.service.kafka.spider.KafkaProducer;
//import com.alibaba.fastjson2.JSONObject;
//import org.hamcrest.Matchers;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//public class SpiderAcceptorControllerTest {
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private SpiderAcceptorController spiderAcceptorController;
//
//    @Mock
//    KafkaProducer kafkaProducer;
//
//    @Before
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(spiderAcceptorController)
//                .build();
//    }
//
//
//    @Test
//    public void TestRequestBodyEmpty() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/spider/submit"))
//                .andExpect(MockMvcResultMatchers.status().is(400));
//    }
//
//    @Test
//    public void TestRequestBodyFilledAll() throws Exception {
//        SpiderEntity spiderEntity = new SpiderEntity();
//        spiderEntity.setSource("idicc");
//        spiderEntity.setData(new JSONObject());
//        spiderEntity.setUrl("http://www.idicc.cn");
//        spiderEntity.setBusiness(BusinessTypeEnum.NEWS.name());
//        mockMvc.perform(MockMvcRequestBuilders.post("/spider/submit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JSONObject.toJSONString(spiderEntity))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(
//                        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(200)
//                        ))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.msg", Matchers.is("success")));
//    }
//
//    @Test
//    public void TestRequestBodyErrorBusinessType() throws Exception {
//        SpiderEntity spiderEntity = new SpiderEntity();
//        spiderEntity.setSource("idicc");
//        spiderEntity.setData(new JSONObject());
//        spiderEntity.setUrl("http://www.idicc.cn");
//        spiderEntity.setBusiness("error business");
//        mockMvc.perform(MockMvcRequestBuilders.post("/spider/submit")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JSONObject.toJSONString(spiderEntity))
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(
//                        MockMvcResultMatchers.jsonPath("$.code", Matchers.is(400)
//                        ))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.msg", Matchers.is("business type not acceptable")));
//    }
//
//
//}
