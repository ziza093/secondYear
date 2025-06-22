fun String.toPascalCase(): String = this.split(" ")
    .joinToString(separator = "") { it.capitalize() }

class MutableMapFunctor<K, V>(val map: MutableMap<K, V>) {
    fun map(function: (V) -> V): MutableMapFunctor<K, V> {
        val resultMap = mutableMapOf<K, V>()
        map.forEach { (key, value) ->
            resultMap[key] = function(value)
        }
        return MutableMapFunctor(resultMap)
    }

    override fun toString(): String {
        return map.toString()
    }
}

fun main() {
    val initialMap = mutableMapOf<Int, String>(
        1 to "hello world example",
        2 to "functional programming paradigm",
        3 to "kotlin programming language"
    )

    val mapFunctor = MutableMapFunctor(initialMap)

    println("Initial map: $mapFunctor")

    val afterFirstMap = mapFunctor.map { "Test $it" }
    println("After adding 'Test' prefix: $afterFirstMap")

    val afterSecondMap = afterFirstMap.map { it.toPascalCase() }
    println("After converting to PascalCase: $afterSecondMap")
}