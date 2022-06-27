package xyz.r2turntrue.ieact

import org.bukkit.entity.Player
import xyz.r2turntrue.ieact.internal.registeredListeners

abstract class IeactComponent<P, S>(protected val props: P?, val player: Player) {

    val closed
        get() = registeredListeners.values().find { e -> e.first == player.uniqueId } == null
    protected var state: S? = null

    abstract fun render(): IeactRendered

    fun onClose() {}

}