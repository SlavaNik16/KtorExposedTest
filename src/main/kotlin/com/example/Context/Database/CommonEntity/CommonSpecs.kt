

package com.example.Context.Database.CommonEntity

import com.example.Context.Database.CommonEntity.EntityInterface.IEntityAuditDeleted
import com.example.Context.Database.Tables.Models.BaseAuditEntity
import org.jetbrains.exposed.sql.Query

inline fun <TEntity> Query.notDeletedAt(entity:TEntity): Query where TEntity : IEntityAuditDeleted {
    return where {
        var result = entity as IEntityAuditDeleted
        result.deletedAt.isNull()
    }
}


