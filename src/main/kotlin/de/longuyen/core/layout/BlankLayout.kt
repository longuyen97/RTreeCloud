package de.longuyen.core.layout

import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.core.Word
import de.longuyen.fonts.AnonymousCustomFont
import de.longuyen.nlp.Tokenizer
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.geom.Point2D
import java.awt.image.BufferedImage

class BlankLayout(private val collisionAlgorithm: CollisionAlgorithm, private val tokenizer: Tokenizer) : Layout(){
    private val log = LogManager.getLogger(BlankLayout::class.java)

    override fun produce(image: BufferedImage, text: String, preferedColor: Color, minSize: Float, maxSize: Float): List<Word> {
        log.info("Generating words' layout for image with width ${image.width} and height ${image.height}")

        val count = tokenDistribution(text)

        val allTokens = count.keys.toMutableList()
        allTokens.sortByDescending {count[it]}
        val minAppearance = count[allTokens.last()]!!
        val maxAppearance = count[allTokens.first()]!!
        log.info("Most frequent token appears $maxAppearance times. Least frequent token appears $minAppearance times.")

        val tokens = allTokens.subList(0, Integer.min(count.keys.size, getMaxWords(image.width, image.height)))
        log.info("${tokens.size} tokens will be used for generating image.")

        val ret = mutableListOf<Word>()
        while (tokens.isNotEmpty()) {
            val token = tokens.random()
            val coefficient = count[token]!!.toFloat() / maxAppearance.toFloat()
            val fontSize = java.lang.Float.max(coefficient * maxSize, minSize)
            log.info("Token $token appears ${count[token]} times with a coefficient $coefficient. Has a font size of $fontSize")

            val font = AnonymousCustomFont(fontSize)
            var found = false
            var y = 0
            var word = Word(font, preferedColor, token, false, Point2D.Double(0.0, y.toDouble()))
            while (!found && y < image.height - word.height()) {
                var x = 0
                while (!found && x < image.width - word.width()) {
                    word = Word(font, preferedColor, token, false, Point2D.Double(x.toDouble(), y.toDouble()))
                    if (!collisionAlgorithm.has(word.rectangle())) {
                        collisionAlgorithm.add(word.rectangle())
                        ret.add(word)
                        found = true
                    }
                    x += 5
                }
                y += 5
            }
            tokens.remove(token)
        }

        log.info("${ret.size} tokens found their place in the image.")
        return ret
    }

    private fun tokenDistribution(text: String) : Map<String, Int>{
        val tokens = tokenizer.tokenize(text)
        log.info("Tokenizer returns ${tokens.size} tokens from the text input.")
        val count = HashMap<String, Int>()

        for (token in tokens) {
            if (count.containsKey(token)) {
                count[token] = count[token]!! + 1
            } else {
                count[token] = 1
            }
        }
        return count
    }

    private fun getMaxWords(width: Int, height: Int): Int {
        return width * height / 100
    }
}