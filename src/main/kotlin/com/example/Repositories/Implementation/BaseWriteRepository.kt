package com.example.Repositories.Implementation

import com.example.Context.Database.CommonEntity.EntityInterface.IEntity
import com.example.Repositories.Interfaces.IRepositoryWriter

/**
 * Базовая реализация записи
 */
open class BaseWriteRepository<TEntity> : IRepositoryWriter<TEntity> where TEntity : IEntity {
    override fun add(entity: TEntity, createdBy: String) {
        TODO("Not yet implemented")
    }

    override fun update(entity: TEntity, updatedBy: String) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: TEntity) {
        TODO("Not yet implemented")
    }
}