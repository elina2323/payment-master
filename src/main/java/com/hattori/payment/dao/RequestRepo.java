package com.hattori.payment.dao;

import com.hattori.payment.model.entity.Code;
import com.hattori.payment.model.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepo extends JpaRepository<Request, Long> {

    int countByCodeAndSuccess(Code code, boolean success);
}