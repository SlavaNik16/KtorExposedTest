package com.example.Context.Database.CommonEntity

import com.example.Context.Database.CommonEntity.EntityInterface.IEntityAuditDeleted
import com.example.Context.Database.Tables.Models.BaseAuditEntity
import org.jetbrains.exposed.sql.IsNullOp
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull

fun <TEntity> SqlExpressionBuilder.notDeletedAt(entity: TEntity): IsNullOp where TEntity : IEntityAuditDeleted {
    var result = entity as IEntityAuditDeleted
    return result.deletedAt.isNull()

}


