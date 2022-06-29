<div align="center">
<br/>
<img height=256 src="https://user-images.githubusercontent.com/46389044/175945398-7005d41a-0ab9-440d-979a-fb6a61579e8c.png?s=200&v=4" />

# Ieact

A react-like kotlin library to create an inventory ui easily
</div>

## Example Component

```kotlin
// A component without state & props
class Test1Component(props: Any?, player: Player): IeactComponent<Any?, Any?>(props, player) {
    val theStack = ItemStack(Material.APPLE)
    val theTitle = Component.text("Hello, World!")

    override fun render(): IeactRendered =
        ieact(theTitle, 9) {
            item(x = 0, y = 0, stack = theStack) {
                player.sendMessage("TEST")
            }
        }
    }
```

```kotlin
// A component with state
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
```