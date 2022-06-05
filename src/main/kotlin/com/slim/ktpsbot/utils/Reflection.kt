package com.slim.ktpsbot.utils

import com.slim.ktpsbot.command.Command
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

object Reflection {
    fun getCommandsFromPackage(packageName: String): Set<Class<out Command>> {
        val reflections = Reflections(
            ConfigurationBuilder()
                .filterInputsBy(FilterBuilder().includePackage(packageName))
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.SubTypes)
        )
        return reflections.getSubTypesOf(Command::class.java) ?: emptySet()
    }
}
