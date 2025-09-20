package io.github.songminkyu.account.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.MappingTarget
import org.mapstruct.Named
import org.mapstruct.NullValuePropertyMappingStrategy

interface EntityMapper<D, E> {
    fun toEntity(dto: D): E
    fun toDto(entity: E): D
    fun toEntity(dtoList: List<D>): List<E>
    fun toDto(entityList: List<E>): List<D>

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(@MappingTarget entity: E, dto: D)
}