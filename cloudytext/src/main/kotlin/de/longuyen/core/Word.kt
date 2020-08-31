package de.longuyen.core

import com.github.davidmoten.rtree.geometry.Geometries
import com.github.davidmoten.rtree.geometry.Geometry
import java.awt.Color
import java.awt.geom.Point2D

class Word(val font: CustomFont, val color: Color, val word: String, val isVertical: Boolean, val position: Point2D) {
    fun rectangle() : Geometry {
        if(isVertical) {
            return Geometries.rectangle(position.x, position.y, position.x + font.height(word), position.y + font.width(word))
        }else{
            return Geometries.rectangle(position.x, position.y, position.x + font.width(word), position.y + font.height(word))
        }
    }
}