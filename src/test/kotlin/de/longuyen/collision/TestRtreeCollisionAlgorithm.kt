package de.longuyen.collision

import com.github.davidmoten.rtree.geometry.Geometries
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestRtreeCollisionAlgorithm{
    @Test
    fun `Test Point collision`(){
        val rtree = RtreeCollisionAlgorithm()
        assertFalse(rtree.has(Geometries.point(0.0, 0.0)))
        rtree.add(Geometries.point(0.0, 0.0))
        assertTrue(rtree.has(Geometries.point(0.0, 0.0)))
        assertTrue(rtree.has(Geometries.point(0.0001, 0.0001), 1.0))
        assertFalse(rtree.has(Geometries.point(0.0001, 0.0001)))
    }

    @Test
    fun `Test Rectangle collision`(){
        val rtree = RtreeCollisionAlgorithm()
        assertFalse(rtree.has(Geometries.rectangle(0.0, 0.0, 1.0, 1.0)))
        rtree.add(Geometries.rectangle(0.25, 0.25, 0.75, 0.75))
        assertTrue(rtree.has(Geometries.rectangle(0.0, 0.0, 1.0, 1.0)))
        assertFalse(rtree.has(Geometries.rectangle(0.85, 0.85, 1.0, 1.0)))
        assertTrue(rtree.has(Geometries.rectangle(0.85, 0.85, 1.0, 1.0), 1.0))
    }
}