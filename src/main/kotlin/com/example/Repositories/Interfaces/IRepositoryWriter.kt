package com.example.Repositories.Interfaces

import org.jetbrains.annotations.NotNull

interface IRepositoryWriter<TEntity> {

    /**
     * Добавить новую запись
     */
    suspend fun add(@NotNull entity: TEntity, createdBy: String = "")

    /**
     * Изменить запись
     */
    suspend fun update(@NotNull entity: TEntity, updatedBy: String = "")

    /**
     * Удалить запись по истории
     */
    suspend fun delete(@NotNull entity: TEntity)

}