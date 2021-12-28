package com.hattori.payment.mapper;

import com.hattori.payment.model.dto.CodeDto;
import com.hattori.payment.model.entity.Code;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeMapper {

    CodeMapper INSTANCE = Mappers.getMapper(CodeMapper.class);

    @Mapping(source = "user.id", target = "user.id")
    Code mapToCode(CodeDto codeDto);

    @Mapping( source = "user.id", target = "user.id")
    CodeDto mapToCodeDto(Code code);
}