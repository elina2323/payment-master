package com.hattori.payment.model.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pays")
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String serviceName;

    BigDecimal cash;

    @CreationTimestamp
    Date addDate;

    @UpdateTimestamp
    Date editDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
