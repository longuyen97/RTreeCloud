package de.longuyen.core

import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.cosmetics.AnonymousCustomFont
import de.longuyen.nlp.Tokenizer
import java.awt.Color
import java.awt.geom.Point2D
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*
import kotlin.collections.LinkedHashMap

class WordLayout(private val collisionAlgorithm: CollisionAlgorithm, private val tokenizer: Tokenizer) {
    fun generateLayout(width: Int, height: Int, text: String, color: Color): List<Word> {
        val ret = mutableListOf<Word>()
        val tokens = tokenizer.tokenize(text)
        val count = LinkedHashMap<String, Int>()
        for (token in tokens) {
            if (count.containsKey(token)) {
                count[token] = count[token]!! + 1
            } else {
                count[token] = 1
            }
        }
        val font = AnonymousCustomFont(13f)
        val keys = count.keys.toMutableList().subList(0, min(count.keys.size, getMaxWords(width, height)))
        while (keys.isNotEmpty()) {
            val randomWord = keys.random()
            var found = false
            var y = 0
            var x = 0
            while (!found && y < height) {
                while (!found && x < width) {
                    val word = Word(font, color, randomWord, false, Point2D.Double(x.toDouble(), y.toDouble()))
                    if (!collisionAlgorithm.has(word.rectangle())) {
                        collisionAlgorithm.add(word, word.rectangle())
                        ret.add(word)
                        found = true
                    }
                    ++x
                }
                ++y
            }
            keys.remove(randomWord)
        }
        return ret
    }

    fun getMaxWords(width: Int, height: Int): Int {
        return 100
    }
}