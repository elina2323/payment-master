package com.hattori.payment.dao;

import com.hattori.payment.model.enums.CodeStatus;
import com.hattori.payment.model.entity.Code;
import com.hattori.payment.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepo extends JpaRepository<Code, Long > {

    Code findByUserAndCodeStatus(User user, CodeStatus status);
}
