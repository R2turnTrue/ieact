<div align="center">
![ieact](https://user-images.githubusercontent.com/46389044/175945398-7005d41a-0ab9-440d-979a-fb6a61579e8c.png?s=200&v=4)
<h1>Ieact</h1>

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