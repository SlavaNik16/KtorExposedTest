package com.example.Repositories.Interfaces

import org.jetbrains.annotations.NotNull

interface IRepositoryWriter<TEntity> {

    /**
     * Добавить новую запись
     */
    fun add(@NotNull entity:TEntity, createdBy:String = "")

    /**
     * Изменить запись
     */
    fun update(@NotNull entity:TEntity, updatedBy:String = "")

    /**
     * Удалить запись по истории
     */
    fun delete(@NotNull entity:TEntity)

}