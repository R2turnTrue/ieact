package xyz.r2turntrue.ieact.internal

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.PluginClassLoader

// from monun/heartbeat-coroutines
internal object Downstream {
    private val classLoaderFields
        get() = PluginClassLoader::class.java.declaredFields.filter {
            ClassLoader::class.java.isAssignableFrom(it.type)
        }.onEach { field ->
            field.isAccessible = true
        }

    private val PluginClassLoader.internalLoaders: List<ClassLoader>
        get() = classLoaderFields.map { it.get(this) }.filterIsInstance<ClassLoader>()

    fun pullPlugin(): Plugin {
        val classLoader = Downstream::class.java.classLoader

        return Bukkit.getPluginManager().plugins.find { plugin ->
            val pluginClassLoader = plugin.javaClass.classLoader as PluginClassLoader

            pluginClassLoader === classLoader || pluginClassLoader.internalLoaders.any { it === classLoader }
        } ?: error("ieact must be loaded from PluginClassLoader")
    }
}