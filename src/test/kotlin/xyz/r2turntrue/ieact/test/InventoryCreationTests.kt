package xyz.r2turntrue.ieact.test

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.r2turntrue.ieact.MyPlugin
import kotlin.test.*

class InventoryCreationTests {

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
        player.openInventory.close()
        //assertEquals(expectedTitle, player.openInventory.title) // because mockbukkit doesn't implemented title of chest inventory
    }

    @Test
    fun testInventoryUpdate() {
        val player = server.addPlayer()
        server.execute("test2", player)
        assert(player.openInventory.getItem(0)!!.type == Material.APPLE)
        server.scheduler.performOneTick()
        assert(player.openInventory.getItem(0)!!.type == Material.GOLDEN_APPLE)
        player.openInventory.close()
    }

    @AfterTest
    fun shutdown() {
        MockBukkit.unmock()
    }

}