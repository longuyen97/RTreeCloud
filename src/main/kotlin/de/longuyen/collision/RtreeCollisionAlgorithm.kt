package de.longuyen.collision

import com.github.davidmoten.rtree.RTree
import com.github.davidmoten.rtree.geometry.Geometry
import com.github.davidmoten.rtree.geometry.Point
import com.github.davidmoten.rtree.geometry.Rectangle
import de.longuyen.cosmetics.AnonymousCustomFont
import de.longuyen.core.Word
import java.awt.Color
import java.awt.geom.Point2D
import java.util.*

class RtreeCollisionAlgorithm : CollisionAlgorithm() {
    private var tree : RTree<Word, Geometry> = RTree.create()

    override fun add(word: Word, rectangle: Rectangle) {
        tree = tree.add(word, rectangle)
    }

    override fun add(point: Point) {
        tree = tree.add(Word(AnonymousCustomFont(12f), Color.BLACK, UUID.randomUUID().toString(), true, Point2D.Double(-1.0, -1.0)), point)
    }

    override fun has(point: Point) : Boolean{
        val result =  tree.search(point)
        val matches: Int = result.count().toBlocking().single()
        return matches > 0
    }

    override fun has(point: Point, maxDistance: Double) : Boolean{
        val result =  tree.search(point, maxDistance)
        val matches: Int = result.count().toBlocking().single()
        return matches > 0
    }

    override fun has(rectangle: Rectangle): Boolean {
        val result =  tree.search(rectangle)
        val matches: Int = result.count().toBlocking().single()
        return matches > 0
    }
}