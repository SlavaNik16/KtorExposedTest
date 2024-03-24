

package com.example.Context.Database.CommonEntity

import com.example.Context.Database.Tables.Models.BaseAuditEntity
import org.jetbrains.exposed.sql.Query

fun Query.notDeletedAt(): Query {
    return where {
        var result = this as BaseAuditEntity
        result.deletedAt.isNull()
    }
}


