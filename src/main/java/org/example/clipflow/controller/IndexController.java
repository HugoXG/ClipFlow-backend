package org.example.clipflow.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.clipflow.service.user.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/luckyjourney/index")
public class IndexController {
    //构造依赖注入
    private final UserService userService;
    public IndexController(UserService userService) {
        this.userService = userService;
    }


}
