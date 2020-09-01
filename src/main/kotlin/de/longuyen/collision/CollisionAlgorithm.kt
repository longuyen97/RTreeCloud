package de.longuyen.collision

import com.github.davidmoten.rtree.geometry.Point
import com.github.davidmoten.rtree.geometry.Rectangle

abstract class CollisionAlgorithm {
    abstract fun add(rectangle: Rectangle)
    abstract fun add(point: Point)
    abstract fun has(rectangle: Rectangle): Boolean
    abstract fun has(rectangle: Rectangle, maxDistance: Double): Boolean
    abstract fun has(point: Point, maxDistance: Double) : Boolean
    abstract fun has(point: Point) : Boolean
    abstract fun copy() : CollisionAlgorithm
    abstract fun visualize(path: String, width: Int, height: Int)
}