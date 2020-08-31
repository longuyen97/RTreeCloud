package de.longuyen.collision

import com.github.davidmoten.rtree.geometry.Point
import com.github.davidmoten.rtree.geometry.Rectangle
import de.longuyen.core.Word

abstract class CollisionAlgorithm {
    abstract fun add(word: Word, rectangle: Rectangle)
    abstract fun add(point: Point)
    abstract fun has(rectangle: Rectangle): Boolean
    abstract fun has(point: Point, maxDistance: Double) : Boolean
    abstract fun has(point: Point) : Boolean
}