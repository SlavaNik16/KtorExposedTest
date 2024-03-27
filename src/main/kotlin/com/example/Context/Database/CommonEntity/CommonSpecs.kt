package com.example.Context.Database.CommonEntity

import com.example.Context.Database.CommonEntity.EntityInterface.IEntityAuditDeleted
import org.jetbrains.exposed.sql.IsNullOp
import org.jetbrains.exposed.sql.SqlExpressionBuilder

fun <TEntity> SqlExpressionBuilder.notDeletedAt(entity: TEntity): IsNullOp where TEntity : IEntityAuditDeleted {
    var result = entity as IEntityAuditDeleted
    return result.deletedAt.isNull()

}


