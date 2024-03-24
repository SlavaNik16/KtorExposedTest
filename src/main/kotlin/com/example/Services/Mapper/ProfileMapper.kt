package com.example.Services.Mapper

import com.example.Context.Database.Tables.Models.UserTable
import com.example.Context.Database.Tables.Results.CardTableResult
import com.example.Context.Database.Tables.Results.UserTableResult
import com.example.Models.CardModel
import com.example.Models.UserModel
import com.example.Services.Mapper.Constants.MapperConstants
import org.jetbrains.exposed.sql.ResultRow
import org.mapstruct.Mapper
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MapperConstants.COMPONENT_MODEL,
    unmappedTargetPolicy = ReportingPolicy.WARN
)
abstract class ProfileMapper {
    abstract fun mapToUserModel(row: UserTableResult?): UserModel?
    abstract fun mapToCardsModel(rows: List<CardTableResult>): List<CardModel>
    abstract fun mapToCardModel(row: CardTableResult?): CardModel?
}