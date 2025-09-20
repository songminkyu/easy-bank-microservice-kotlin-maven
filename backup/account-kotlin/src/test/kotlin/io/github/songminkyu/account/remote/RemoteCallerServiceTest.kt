package io.github.songminkyu.account.remote

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Field

class RemoteCallerServiceTest {

    @Test
    fun testReflection() {
        // Spring uygulamasını başlat
        val packageName = "io.github.songminkyu.account.remote"
        val remoteEndpointFields = findFieldsWithAnnotation(packageName, RemoteEndpoint::class.java)

        // Sonuçları yazdır
        println("@RemoteEndpoint annotation'ına sahip alanlar:")
        for (field in remoteEndpointFields) {
            println("${field.declaringClass.name} - ${field.name}")
        }
    }

    companion object {
        fun findFieldsWithAnnotation(packageName: String, annotation: Class<out Annotation>): Set<Field> {
            val fields = mutableSetOf<Field>()
            val scanner = ClassPathScanningCandidateComponentProvider(false)
            scanner.addIncludeFilter(AnnotationTypeFilter(Component::class.java))
            val beanDefinitions = scanner.findCandidateComponents(packageName)
            for (bd in beanDefinitions) {
                try {
                    val cls = Class.forName(bd.beanClassName)
                    ReflectionUtils.doWithFields(cls) { field ->
                        if (AnnotationUtils.findAnnotation(field, annotation) != null) {
                            fields.add(field)
                        }
                    }
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                }
            }
            return fields
        }
    }
}