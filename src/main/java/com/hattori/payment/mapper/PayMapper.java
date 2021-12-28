package com.hattori.payment.mapper;

import com.hattori.payment.model.dto.PayDto;
import com.hattori.payment.model.entity.Pay;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayMapper {
    PayMapper INSTANCE = Mappers.getMapper(PayMapper.class);

    Pay mapToPay(PayDto payDto);
    PayDto mapToPayDto(Pay pay);
}
