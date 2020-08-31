package de.longuyen.core

import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.cosmetics.AnonymousCustomFont
import de.longuyen.nlp.Tokenizer
import java.awt.Color
import java.awt.geom.Point2D

class WordLayout(private val collisionAlgorithm: CollisionAlgorithm, private val tokenizer: Tokenizer) {
    fun generateLayout(width: Int, height: Int, text: String, color: Color) : List<Word>{
        val ret = mutableListOf<Word>()
        ret.add(Word(AnonymousCustomFont(13f), color, "vertical", true, Point2D.Double(50.0, 50.0)))
        ret.add(Word(AnonymousCustomFont(13f), color, "horizontal", false, Point2D.Double(100.0, 100.0)))
        return ret
    }
}