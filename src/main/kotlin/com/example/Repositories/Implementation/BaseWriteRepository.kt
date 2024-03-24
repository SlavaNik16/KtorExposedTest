package com.example.Repositories.Implementation

import com.example.Context.Database.CommonEntity.EntityInterface.*
import com.example.Context.Database.Tables.Models.BaseAuditEntity

import com.example.Repositories.Interfaces.IRepositoryWriter

import org.jetbrains.annotations.NotNull
import org.jetbrains.exposed.sql.insert
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Базовая реализация записи
 */
open class BaseWriteRepository<TEntity> : IRepositoryWriter<TEntity> where TEntity : IEntity {
    private val userName = "KtorSampleApi"
    override fun add(entity: TEntity, createdBy: String) {
        if(entity !is IEntityWithId) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityWithId = entity as IEntityWithId
        entity.insert {
            it[entityWithId.id] = UUID.randomUUID()
        }
        if(createdBy.isBlank()) {
            createdBy.plus(userName)
        }
        AuditForCreated(entity, createdBy)
        AuditForUpdated(entity, createdBy)
    }

    override fun update(entity: TEntity, updatedBy: String) {
        if(updatedBy.isBlank()) {
            updatedBy.plus(userName)
        }
        AuditForUpdated(entity, updatedBy)
    }

    override fun delete(entity: TEntity) {
        AuditForUpdated(entity, userName)
        AuditForDeleted(entity)
    }

    private fun AuditForCreated(@NotNull entity: TEntity, createdBy: String){
        if(entity !is IEntityAuditCreated) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditCreated = entity as IEntityAuditCreated
        entity.insert {
            it[entityAuditCreated.createdAt] = OffsetDateTime.now(ZoneOffset.UTC)
            it[entityAuditCreated.createdBy] = createdBy
        }
    }

    private fun AuditForUpdated(@NotNull entity: TEntity, updatedBy: String){
        if(entity !is IEntityAuditUpdated) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditUpdated = entity as IEntityAuditUpdated
        entity.insert {
            it[entityAuditUpdated.updatedAt] = OffsetDateTime.now(ZoneOffset.UTC)
            it[entityAuditUpdated.updatedBy] = updatedBy
        }
    }

    private fun AuditForDeleted(@NotNull entity: TEntity){
        if(entity !is IEntityAuditDeleted) {
            return
        }
        if(entity !is BaseAuditEntity) {
            return
        }
        var entityAuditDeleted = entity as IEntityAuditDeleted
        entity.insert {
            it[entityAuditDeleted.deletedAt] = OffsetDateTime.now(ZoneOffset.UTC)
        }
    }
}