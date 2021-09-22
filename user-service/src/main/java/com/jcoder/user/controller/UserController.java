package com.jcoder.user.controller;

import com.jcoder.user.entity.User;
import com.jcoder.user.service.UserService;
import com.jcoder.user.vo.Department;
import com.jcoder.user.vo.ResponseTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public User saveUser(@RequestBody User user){
        log.info("Inside saveUser method of UserController");
       return userService.saveUser(user);
    }

//    @GetMapping("/{id}")
//    public User findUserByid(@PathVariable("id") Long userId){
//        log.info("Inside findByUserid method of UserController");
//        return userService.findByUserId(userId);
//    }

    // To get user along with department which it belongs to,
    // for this we will create a wrapper object that will contain both user and department
    // as we dont department object in value object (vo) package. Here, we have department class as duplication but
    // micro-services come with caveat we are having loose coupling
    // This department class will not be annotated with @Entity/@Id, It will be only contain POJO
    // It will be annotated with Data, @AllArgsConstructor
    //@NoArgsConstructor only, Next we will create a wrapper object i.e ReponseTemplateVO that will contain both
    // user and department, will be annotated with following:
    //@Data
    //@NoArgsConstructor
    //@AllArgsConstructor
    // For this work we need to create resttemplate as bean in main class


    @GetMapping("/{userId}")
    public ResponseTemplateVO getUserWithDepartment(@PathVariable("userId") Long userId){
        log.info("Inside getUserWithDepartment of UserController");
        ResponseTemplateVO responseTemplateVO = userService.getUserWithDepartment(userId);

        return responseTemplateVO;
    }




}
