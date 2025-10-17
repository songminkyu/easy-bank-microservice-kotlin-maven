
import org.mapstruct.BeanMapping
import org.mapstruct.MappingTarget
import org.mapstruct.Named
import org.mapstruct.NullValuePropertyMappingStrategy
import java.util.concurrent.CopyOnWriteArrayList

interface ImmutableEntityMapper<D, E> {
    fun toEntity(dto: D): E

    fun toDto(entity: E): D

    /**
     * Convert a DTO list to an Entity list (thread-safe)
     * @param dtoList: A list of DTOs to convert
     * @return a thread-safe Entity list
     */
    fun toEntity(dtoList: Collection<D>?): List<E> {
        if (dtoList == null) {
            return CopyOnWriteArrayList()
        }
        return dtoList
            .map { toEntity(it) }
            .toCollection(CopyOnWriteArrayList())
    }

    /**
     * Convert a list of entities to a list of DTOs (thread-safe)
     * @param entityList A list of entities to convert
     * @return a thread-safe list of DTOs
     */
    fun toDto(entityList: Collection<E>?): List<D> {
        if (entityList == null) {
            return CopyOnWriteArrayList()
        }
        return entityList
            .map { toDto(it) }
            .toCollection(CopyOnWriteArrayList())
    }

    /**
     * Convert to an immutable list (safest method)
     * @param dtoList List of DTOs to convert
     * @return List of immutable entities
     */
    fun toEntityImmutable(dtoList: Collection<D>?): List<E> {
        if (dtoList == null) {
            return emptyList()
        }
        return dtoList.map { toEntity(it) }
    }

    /**
     * Convert to an immutable list (safest method)
     * @param entityList List of entities to convert
     * @return List of immutable DTOs
     */
    fun toDtoImmutable(entityList: Collection<E>?): List<D> {
        if (entityList == null) {
            return emptyList()
        }
        return entityList.map { toDto(it) }
    }

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(@MappingTarget entity: E, dto: D)
}