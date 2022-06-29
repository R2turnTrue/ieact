package xyz.r2turntrue.ieact

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.JavaPluginLoader
import xyz.r2turntrue.ieact.internal.plugin
import xyz.r2turntrue.ieact.internal.registeredListeners
import xyz.r2turntrue.ieact.internal.renderComponent
import xyz.r2turntrue.ieact.internal.unregisterListener
import java.io.File

lateinit var testPlugin: MyPlugin

class MyPlugin : JavaPlugin, CommandExecutor {

    override fun onEnable() {
        testPlugin = this
        println("!!! THIS PLUGIN NEED TO BE USED ONLY IEACT TEST!")
        println("!!! IF YOU NOT USE THIS PLUGIN FOR IEACT TEST, DELETE THIS PLUGIN!")
        println("UNITTEST: ${System.getProperty("UNIT_TEST")}")
        getCommand("test1")?.setExecutor(this)
        getCommand("test2")?.setExecutor(this)
    }

    class Test1Component(props: Any?, player: Player): IeactComponent<Any?, Any?>(props, player) {
        val theStack = ItemStack(Material.APPLE)
        val theTitle = Component.text("Hello, World!")

        override fun render(): IeactRendered =
            ieact(theTitle, 9) {
                item(x = 0, y = 0, stack = theStack, onClick = {
                    player.sendMessage("TEST")
                })
            }
    }

    // clean test
    override fun onDisable() {
        //plugin = null
        registeredListeners.values().forEach { triple ->
            unregisterListener(triple.second, triple.third)
        }
        registeredListeners.clear()
    }

    data class Test2ComponentState(var counter: Int)

    class Test2Component(props: Any?, player: Player): IeactComponent<Any?, Test2ComponentState>(props, player) {
        init {
            state = Test2ComponentState(0)
        }

        override fun render(): IeactRendered =
            ieact(Component.text("Hello, World!"), 9) {
                state?.run {
                    item(x = 0, y = 0, stack = ItemStack(if(counter >= 1) Material.GOLDEN_APPLE else Material.APPLE), onClick = {
                        player.sendMessage("TEST")
                    })
                    counter++
                }
            }

    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(label.equals("test1", true)) {
            (sender as Player).renderComponent(Test1Component::class.java, null)
        } else if(label.equals("test2", true)) {
            (sender as Player).renderComponent(Test2Component::class.java, null)
        }

        return true
    }

    constructor() : super() {}
    protected constructor(
        loader: JavaPluginLoader?,
        description: PluginDescriptionFile?,
        dataFolder: File?,
        file: File?
    ) : super(
        loader!!, description!!, dataFolder!!, file!!
    ) {
    }
}