package com.example.Repositories.Implementation

import com.example.Context.Database.CommonEntity.EntityInterface.*
import com.example.Context.Database.CommonEntity.Providers.DateTimeProvider
import com.example.Context.Database.DatabaseFactory.dbQuery
import com.example.Context.Database.Tables.Models.BaseAuditEntity

import com.example.Repositories.Interfaces.IRepositoryWriter

import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.update
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Базовая реализация записи
 */
open class BaseWriteRepository<TEntity> : IRepositoryWriter<TEntity> where TEntity : IEntity {
    private val userName = "KtorSampleApi"
    private var dateTimeProvider = DateTimeProvider()

    override suspend fun add(entity: TEntity, createdBy: String) {
        if(entity !is IEntityWithId) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        if(entity.id == null) {
            var entityWithId = entity as IEntityWithId
            dbQuery {
                entity.update {
                    it[entityWithId.id] = UUID.randomUUID()
                }
            }
        }
        if(createdBy.isBlank()) {
            createdBy.plus(userName)
        }
        AuditForCreated(entity, createdBy)
        AuditForUpdated(entity, createdBy)
    }

    override suspend fun update(entity: TEntity, updatedBy: String) {
        if(updatedBy.isBlank()) {
            updatedBy.plus(userName)
        }
        AuditForUpdated(entity, updatedBy)
    }

    override suspend fun delete(entity: TEntity) {
        AuditForUpdated(entity, userName)
        AuditForDeleted(entity)
    }

    private suspend fun AuditForCreated(@NotNull entity: TEntity, createdBy: String){
        if(entity !is IEntityAuditCreated) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditCreated = entity as IEntityAuditCreated
        dbQuery {
            entity.update {
                it[entityAuditCreated.createdAt] = dateTimeProvider.UtcNow()
                it[entityAuditCreated.createdBy] = createdBy
            }
        }
    }

    private suspend fun AuditForUpdated(@NotNull entity: TEntity, updatedBy: String){
        if(entity !is IEntityAuditUpdated) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditUpdated = entity as IEntityAuditUpdated
        dbQuery {
            entity.update {
                it[entityAuditUpdated.updatedAt] = dateTimeProvider.UtcNow()
                it[entityAuditUpdated.updatedBy] = updatedBy
            }
        }
    }

    private suspend fun AuditForDeleted(@NotNull entity: TEntity){
        if(entity !is IEntityAuditDeleted) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditDeleted = entity as IEntityAuditDeleted
        dbQuery {
            entity.update {
                it[entityAuditDeleted.deletedAt] = dateTimeProvider.UtcNow()
            }
        }
    }
}