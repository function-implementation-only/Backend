package com.example.chatservice.tests;

import com.example.chatservice.config.security.user.UserDetailsImpl;
import com.example.chatservice.feignclient.MainServiceClient;
import com.example.chatservice.feignclient.UserResponseDto;
import com.example.chatservice.kafka.Field;
import com.example.chatservice.tests.init.Testing;
import com.example.chatservice.tests.kafkatest.TestDto;
import com.example.chatservice.tests.kafkatest.TestProducer;
import com.example.chatservice.tests.init.TestingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;

@Slf4j
@RestController
public class TestController {

    private final MainServiceClient mainServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final TestProducer testProducer;
    private final TestingRepository testingRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Autowired
    public TestController(MainServiceClient mainServiceClient, CircuitBreakerFactory circuitBreakerFactory, TestProducer testProducer, TestingRepository testingRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.mainServiceClient = mainServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.testProducer = testProducer;
        this.testingRepository = testingRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /*chat-service connection chek*/
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    /*Access_token distingui check*/
    @GetMapping("/test2")
    public String test2(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("유저 계정{}", userDetails.getAccount());
        return userDetails.getAccount();
    }

    /*feignClient token dilivery check*/
    @GetMapping("/test3")
    public Object test3(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        Object myInfo = mainServiceClient.getInfo(auth, accountValue);
        return myInfo;
    }

    /*feignClient with circuitbreaker token dilivery check*/
    @GetMapping("/test4")
    public Object test4(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Object run = circuitBreaker.run(() -> mainServiceClient.getInfo(auth, accountValue));
        return run;
    }

    /*feignClient with circuitbreaker and zipkin token dilivery check*/
    @GetMapping("/test5")
    public Object test5(@RequestHeader("auth") String auth, @RequestHeader("ACCOUNT-VALUE") String accountValue) {
        /*Zipkin trace */
        log.info("trace start");
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Object run = circuitBreaker.run(() -> mainServiceClient.getInfo(auth, accountValue), throwable -> new ArrayList<>());
        log.info("trace end");
        return run;
    }

    /*feignClient with UserResponseDTO check*/
    @GetMapping("/test6")
    public Object test6() {
        /*Zipkin trace */
        log.info("trace start");
        /*CircuitBreaker*/
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        UserResponseDto run = circuitBreaker.run(() -> mainServiceClient.getInfo("true", "string"));
        log.info("trace end");
        return run;
    }

    /*kafka to db test*/
    @GetMapping("/test7")
    public String test7() {

        Testing testing = new Testing();
        testing.setTitle("test");
        testing.setContent("kafka test");
        testingRepository.save(testing);
        return "test";
    }

    /*kafka to db test*/
    @GetMapping("/test8")
    public String test8() {

        TestDto testDto = new TestDto();
        testDto.setTestId(17L);
        testDto.setTitle("test");
        testDto.setContent("kafka test");
        return testProducer.send("testing", testDto);

    }

    /*kafkatemplate test*/
    @PostMapping("/test9")
    public String test9() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString("show me the money");
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        kafkaTemplate.send("temple-test", jsonInString);
        return "test";
    }

    /*Timestamped test*/
    @GetMapping("/test10")
    public Timestamp test10() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        Long longvalue = timestamp.getTime();
        System.out.println(longvalue);
        return timestamp;
    }
/*이너클래스 테스트*/
    @GetMapping("/test11")
    public Field test11() {
        return new Field("int64", false, "updated_at"){public String name="org.apache.kafka.connect.data.Timestamp";public int version =1;};
    }
}
