package com.example.Services.Mapper

import com.example.Models.UserModel
import com.example.Services.Mapper.Constants.MapperConstants
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MapperConstants.COMPONENT_MODEL,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
abstract class ProfileMapper {
    //abstract UserModel map(userTable: UserTable)
}