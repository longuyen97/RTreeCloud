package de.longuyen.collision

import com.github.davidmoten.rtree.RTree
import com.github.davidmoten.rtree.geometry.Geometry
import com.github.davidmoten.rtree.geometry.Point
import com.github.davidmoten.rtree.geometry.Rectangle
import java.util.*

class RtreeCollisionAlgorithm : CollisionAlgorithm() {
    private var tree : RTree<String, Geometry> = RTree.create()

    override fun add(rectangle: Rectangle) {
        tree = tree.add(UUID.randomUUID().toString(), rectangle)
    }

    override fun add(point: Point) {
        tree = tree.add(UUID.randomUUID().toString(), point)
    }

    override fun has(point: Point) : Boolean{
        val result =  tree.search(point)
        val matches: Int = result.count().toBlocking().single()
        return matches > 0
    }

    override fun copy(): CollisionAlgorithm {
        return RtreeCollisionAlgorithm()
    }

    override fun visualize(path: String, width: Int, height: Int) {
        tree.visualize(width,height).save(path)
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

    override fun has(rectangle: Rectangle, maxDistance: Double): Boolean {
        val result =  tree.search(rectangle, maxDistance)
        val matches: Int = result.count().toBlocking().single()
        return matches > 0
    }
}