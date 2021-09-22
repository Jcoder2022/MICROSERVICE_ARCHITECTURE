package com.jcoder.user.repository;

import com.jcoder.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserId(Long userId);

    User findByUserIdAndDepartmentId(Long userId, Long departmentId);
}
