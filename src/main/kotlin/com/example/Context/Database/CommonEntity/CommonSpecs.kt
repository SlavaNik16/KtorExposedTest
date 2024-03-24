

package com.example.Context.Database.CommonEntity

import com.example.Context.Database.CommonEntity.EntityInterface.IEntityWithId
import com.example.Context.Database.Tables.Models.BaseAuditEntity
import com.example.Context.Database.Tables.Models.UserTable
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import java.util.*

//infix fun <T> Query.ById(id: UUID ): Query where T : IEntityWithId {
//    return where (
//        this.where()
//            entities[entities.indexOf(iterator().next() as T)].id.eq(id)
//        )
//}


