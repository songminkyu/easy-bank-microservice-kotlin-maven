package io.github.songminkyu.account.util

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.Assert
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import kotlin.reflect.KClass

object FieldScannerUtil {

    @JvmStatic
    fun findFieldsWithAnnotation(
        basePackage: String,
        classAnnotation: Class<out Annotation>,
        fieldAnnotation: Class<out Annotation>
    ): Set<Field> {
        Assert.notNull(basePackage, "Base package must not be null")
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        
        return try {
            scanner.addIncludeFilter(AnnotationTypeFilter(classAnnotation))

            scanner.findCandidateComponents(basePackage)
                .asSequence()
                .map { bd ->
                    try {
                        Class.forName(bd.beanClassName)
                    } catch (e: ClassNotFoundException) {
                        throw IllegalStateException("Error occurred while scanning classes", e)
                    }
                }
                .flatMap { clazz ->
                    val annotatedFields = mutableSetOf<Field>()
                    ReflectionUtils.doWithFields(clazz) { field ->
                        if (field.isAnnotationPresent(fieldAnnotation)) {
                            annotatedFields.add(field)
                        }
                    }
                    annotatedFields.asSequence()
                }
                .toSet()
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid base package provided", ex)
        } finally {
            scanner.clearCache()
        }
    }
}
