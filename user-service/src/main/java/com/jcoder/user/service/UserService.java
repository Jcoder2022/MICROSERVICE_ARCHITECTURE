package com.jcoder.user.service;

import com.jcoder.user.entity.User;
import com.jcoder.user.repository.UserRepository;
import com.jcoder.user.vo.Department;
import com.jcoder.user.vo.ResponseTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public User saveUser(User user) {
        log.info("Inside saveUser method of UserService ");
       return userRepository.save(user);
    }

    public User findByUserId(Long userId) {
        log.info("Inside findByUserId method of UserService ");
        return userRepository.findByUserId(userId);
    }

    public ResponseTemplateVO getUserWithDepartment(Long userId) {
        log.info("Inside getUserWithDepartment method of UserService ");
        User user =userRepository.findByUserId(userId);

        String departmentResourceUrl
                = "http://DEPARTMENT-SERVICE/departments/"+user.getDepartmentId();
        Department department = restTemplate.getForObject(departmentResourceUrl , Department.class);
        ResponseTemplateVO responseTemplateVO = new ResponseTemplateVO(user,department);
        return responseTemplateVO;
    }

    //To get user along with department which it belongs to

}
