package de.longuyen.collision

import com.github.davidmoten.rtree.RTree
import com.github.davidmoten.rtree.geometry.Geometry
import de.longuyen.core.Word

class RtreeCollisionAlgorithm(width: Int, height: Int) : CollisionAlgorithm(width, height){
    private val tree : RTree<Word, Geometry> = RTree.create()

    override fun add(word: Word, geometry: Geometry): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(geometry: Geometry): Boolean {
        TODO("Not yet implemented")
    }

    override fun has(geometry: Geometry): Boolean {
        TODO("Not yet implemented")
    }
}