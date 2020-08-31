package de.longuyen.collision

import com.github.davidmoten.rtree.geometry.Geometry
import de.longuyen.core.Word

abstract class CollisionAlgorithm(width: Int, height: Int) {
    abstract fun add(word: Word, geometry: Geometry) : Boolean
    abstract fun add(geometry: Geometry) : Boolean
    abstract fun has(geometry: Geometry) : Boolean
}