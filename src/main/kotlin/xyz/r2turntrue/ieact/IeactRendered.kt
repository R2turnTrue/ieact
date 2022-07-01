package xyz.r2turntrue.ieact

import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

data class IeactRendered(val items: HashMap<Int, Triple<ItemStack, Boolean, () -> Unit>> = HashMap())

class IeactRenderedBuilder() {
    private val items = HashMap<Int, Triple<ItemStack, Boolean, () -> Unit>>()

    fun item(x: Int = 0, y: Int = 0, stack: ItemStack? = null, clickCancelled: Boolean = true, onClick: () -> Unit = {}) {
        items[x * y] = Triple(stack!!, clickCancelled, onClick)
    }

    internal fun build(): IeactRendered =
        IeactRendered(items)
}

fun ieact(func: IeactRenderedBuilder.() -> Unit) =
    IeactRenderedBuilder().apply(func).build()