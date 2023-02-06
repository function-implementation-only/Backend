package com.example.speedsideproject.server;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/server")
public class ServerController {

    private final Environment env;

    @Timed(value="users.status", longTask = true)
    @GetMapping("/health_check")
//    @Timed(value="users.status", longTask = true)
    public String status() {
        return String.format("Main-service is working\n"
                +"my name is =" + env.getProperty("get.my.name")
                +"my name is =" + env.getProperty("test.my.profile")

//                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
//                + ", gateway ip=" + env.getProperty("gateway.ip")
//                + ", message=" + env.getProperty("greeting.message")
//                + ", token secret=" + env.getProperty("token.secret")
//                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    );
    }






}
