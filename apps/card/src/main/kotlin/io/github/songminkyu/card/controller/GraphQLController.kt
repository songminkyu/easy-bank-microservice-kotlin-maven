package io.github.songminkyu.card.controller

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Controller

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Controller
annotation class GraphQLController(
    @get:AliasFor(annotation = Controller::class)
    val value: String = ""
)