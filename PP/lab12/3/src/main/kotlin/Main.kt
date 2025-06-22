import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

fun main() {
    println("Enter the number of points:")
    val n = readLine()?.toIntOrNull() ?: return

    val points = mutableListOf<Point>()
    println("Enter coordinates for each point (x y):")
    repeat(n) {
        val coordinates = readLine()?.split(" ")?.map { it.toDoubleOrNull() ?: 0.0 } ?: listOf(0.0, 0.0)
        if (coordinates.size >= 2) {
            points.add(Point(coordinates[0], coordinates[1]))
        }
    }

    val perimeter = calculatePerimeter(points)
    println("The perimeter of the polygon is: $perimeter")
}

fun calculatePerimeter(points: List<Point>): Double {
    if (points.size < 3) return 0.0

    val pointPairs = points.zipWithNext() + Pair(points.last(), points.first())

    return pointPairs.map { (p1, p2) ->
        distance(p1, p2)
    }.sum()
}

fun distance(p1: Point, p2: Point): Double {
    return sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y))
}