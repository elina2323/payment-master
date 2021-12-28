package com.hattori.payment.dao;

import com.hattori.payment.model.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepo extends JpaRepository<Pay,Long> {

    Pay findPayById(Long id);

}
