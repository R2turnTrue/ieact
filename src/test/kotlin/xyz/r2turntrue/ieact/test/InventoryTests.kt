package xyz.r2turntrue.ieact.test

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.r2turntrue.ieact.MyPlugin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class InventoryTests {

    lateinit var server: ServerMock
    lateinit var plugin: MyPlugin

    @BeforeTest
    fun setup() {
        System.setProperty("UNIT_TEST", "1")
        server = MockBukkit.mock()
        plugin = MockBukkit.loadWith(MyPlugin::class.java, javaClass.classLoader.getResourceAsStream("plugin.yml"))
    }

    @Test
    fun testInventoryCreate() {
        val player = server.addPlayer()
        server.execute("test1", player)

        val expectedStack = ItemStack(Material.APPLE)
        val expectedTitle = "Hello, World!"
        assert(player.openInventory.getItem(0)!!.type == expectedStack.type)
        //assertEquals(expectedTitle, player.openInventory.title) // because mockbukkit doesn't implemented title of chest inventory
    }

    @AfterTest
    fun shutdown() {
        MockBukkit.unmock()
    }

}