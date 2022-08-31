package com.linton.web

import io.micronaut.context.annotation.Context
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect
import org.thymeleaf.TemplateEngine

@Singleton
class ThymeleafDialectLoader(val templateEngine: TemplateEngine) {
    @PostConstruct
    fun setDialect() {
        templateEngine.addDialect(LayoutDialect())
    }
}