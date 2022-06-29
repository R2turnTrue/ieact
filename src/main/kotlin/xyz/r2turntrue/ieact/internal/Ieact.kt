package xyz.r2turntrue.ieact.internal

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimaps
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.scheduler.BukkitTask
import xyz.r2turntrue.ieact.IeactComponent
import xyz.r2turntrue.ieact.testPlugin
import java.util.*

internal val registeredListeners = ArrayListMultimap.create<Class<out IeactComponent<*, *>>, Triple<UUID, Class<out Event>, Listener>>()
internal val plugin = if(System.getProperty("UNIT_TEST") == "1") testPlugin else Downstream.pullPlugin()

fun <P> Player.renderComponent(clazz: Class<out IeactComponent<*, *>>, props: P? = null) {
    val component = clazz.constructors[0].newInstance(props, this) as IeactComponent<*, *>
    lateinit var renderTask: BukkitTask
    fun renderInventory() {
        val rendered = component.render()
        val renderedInventory =
            if(System.getProperty("UNIT_TEST") == "1")
                Bukkit.createInventory(null, rendered.size, PlainTextComponentSerializer.plainText().serialize(rendered.title))
            else
                Bukkit.createInventory(null, rendered.size, rendered.title)
        rendered.items.forEach { slot, stack ->
            renderedInventory.setItem(slot, stack.first)
        }
        this.openInventory(renderedInventory)
    }
    renderInventory()
    renderTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
        if(component.closed) {
            Bukkit.getScheduler().cancelTask(renderTask.taskId)
            return@Runnable
        }
        renderInventory()
    }, 0, 1)
    registeredListeners.put(clazz, Triple(this.uniqueId, InventoryClickEvent::class.java, listener(InventoryClickEvent::class.java) { event ->
        if(event.view.player.uniqueId == this.uniqueId) {
            val rendered = component.render()
            event.isCancelled = true
            rendered.items[event.slot]?.also { renderedItemPair ->
                if(!renderedItemPair.second)
                    event.isCancelled = false
                renderedItemPair.third()
            }
        }
    }))
    registeredListeners.put(clazz, Triple(this.uniqueId, InventoryCloseEvent::class.java, listener(InventoryCloseEvent::class.java) { event ->
        if(component.player.uniqueId == event.view.player.uniqueId) {
            component.onClose()
            registeredListeners.values().removeIf { listenerTriple -> listenerTriple.first == this.uniqueId }
        }
    }))
}