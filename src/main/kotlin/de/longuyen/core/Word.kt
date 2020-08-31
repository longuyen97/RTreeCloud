package de.longuyen.core

import com.github.davidmoten.rtree.geometry.Geometries
import com.github.davidmoten.rtree.geometry.Rectangle
import de.longuyen.cosmetics.CustomFont
import java.awt.Color
import java.awt.geom.Point2D

class Word(val font: CustomFont, val color: Color, val word: String, val isVertical: Boolean, val position: Point2D) {
    fun rectangle() : Rectangle {
        if(isVertical) {
            return Geometries.rectangle(position.x, position.y, position.x + font.height(word), position.y + font.width(word))
        }else{
            return Geometries.rectangle(position.x, position.y, position.x + font.width(word), position.y + font.height(word))
        }
    }

    fun translate(newPosition: Point2D) : Word{
        return Word(font, color, word, isVertical, newPosition)
    }

    fun rotate() : Word{
        return Word(font, color, word, isVertical.not(), position)
    }

    fun resize(size: Float) : Word{
        return Word(font.resize(size), color, word, isVertical, position)
    }
}