
package io.github.songminkyu.account.util

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.Assert
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field
import java.util.stream.Collectors
import java.util.stream.Stream

object FieldScannerUtil {
    @JvmStatic
    fun findFieldsWithAnnotation(
        basePackage: String?,
        classAnnotation: Class<out Annotation?>,
        fieldAnnotation: Class<out Annotation?>
    ): MutableSet<Field> {
        Assert.notNull(basePackage, "Base package must not be null")
        val scanner = ClassPathScanningCandidateComponentProvider(false)

        try {
            scanner.addIncludeFilter(AnnotationTypeFilter(classAnnotation))

            return scanner.findCandidateComponents(basePackage)
                .stream()
                .map { bd: BeanDefinition ->
                    try {
                        Class.forName(bd.beanClassName)
                    } catch (e: ClassNotFoundException) {
                        throw IllegalStateException("Error occurred while scanning classes", e)
                    }
                }
                .flatMap { clazz: Class<*> ->
                    val annotatedFields = mutableSetOf<Field>()
                    ReflectionUtils.doWithFields(clazz) { field: Field ->
                        if (field.isAnnotationPresent(fieldAnnotation)) {
                            annotatedFields.add(field)
                        }
                    }
                    annotatedFields.stream()
                }
                .collect(Collectors.toSet())
        } catch (ex: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid base package provided", ex)
        } finally {
            scanner.clearCache()
        }
    }
}