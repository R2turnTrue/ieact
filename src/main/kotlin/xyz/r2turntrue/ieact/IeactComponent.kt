package xyz.r2turntrue.ieact

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import xyz.r2turntrue.ieact.internal.registeredListeners

abstract class IeactComponent<P, S>(val size: Int, val title: Component, protected val props: P?, val player: Player) {

    val closed
        get() = registeredListeners.values().find { e -> e.first == player.uniqueId } == null
    protected var state: S? = null

    abstract fun render(): IeactRendered

    open fun onClose() {}

}