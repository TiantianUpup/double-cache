package com.cqupt.study;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 *
 * @Author: hetiantian
 * @Date:2018/12/22 19:58 
 * @Version: 1.0
 */
@RestController("/test")
public class TestController {
    @GetMapping
    public Object test() {
        return "test";
    }
}
