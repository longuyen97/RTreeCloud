package de.longuyen.core

import com.github.davidmoten.rtree.geometry.Geometries
import com.github.davidmoten.rtree.geometry.Rectangle
import de.longuyen.fonts.CustomFont
import java.awt.Color
import java.awt.geom.Point2D

class Word(val font: CustomFont, val color: Color, val word: String, val horizontal: Boolean, val position: Point2D) {
    fun rectangle() : Rectangle {
        return if(horizontal) {
            Geometries.rectangle(position.x, position.y, position.x + font.width(word), position.y + font.height(word))
        }else{
            Geometries.rectangle(position.x, position.y, position.x + font.height(word), position.y + font.width(word))
        }
    }

    fun height() : Double {
        return if(this.horizontal){
            font.height(word)
        }else{
            font.width(word)
        }
    }

    fun width() : Double{
        return if(this.horizontal){
            font.width(word)
        }else{
            font.height(word)
        }
    }

    fun translate(newPosition: Point2D) : Word{
        return Word(font, color, word, horizontal, newPosition)
    }

    fun colorize(newColor: Color) : Word{
        return Word(font, newColor, word, horizontal, position)
    }

    fun resize(size: Float) : Word{
        return Word(font.resize(size), color, word, horizontal, position)
    }

    fun fontSize() : Float {
        return font.size
    }
}