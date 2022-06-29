package xyz.r2turntrue.ieact.test

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import xyz.r2turntrue.ieact.MyPlugin
import kotlin.test.*

class InventoryClosingTests {

    lateinit var server: ServerMock
    lateinit var plugin: MyPlugin

    @BeforeTest
    fun setup() {
        System.setProperty("UNIT_TEST", "1")
        server = MockBukkit.mock()
        plugin = MockBukkit.loadWith(MyPlugin::class.java, javaClass.classLoader.getResourceAsStream("plugin.yml"))
    }

    @Test
    fun testInventoryClose() {
        val player = server.addPlayer()
        server.execute("test1", player)
        server.scheduler.performOneTick()
        server.scheduler.performOneTick()
        player.openInventory.close()
        server.scheduler.performOneTick()
        server.scheduler.performOneTick()
        player.openInventory
        assertNull(player.openInventory.topInventory)
    }

    @AfterTest
    fun shutdown() {
        MockBukkit.unmock()
    }
}