package de.longuyen.core.layout

import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.core.FoundCallback
import de.longuyen.core.Word
import de.longuyen.fonts.HelveticaCustomFont
import de.longuyen.nlp.Tokenizer
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.util.*
import kotlin.collections.HashMap

class BlankLayout(private val collisionAlgorithm: CollisionAlgorithm, private val tokenizer: Tokenizer) : Layout(){
    private val log = LogManager.getLogger(BlankLayout::class.java)
    private val random = Random()

    override fun produce(image: BufferedImage, text: String, preferedColor: Color, minSize: Float, maxSize: Float, foundCallback: FoundCallback): List<Word> {
        log.info("Generating words' layout for image with width ${image.width} and height ${image.height}. Min font $minSize and max font $maxSize")

        val count = tokenDistribution(text)

        val allTokens = count.keys.toMutableList()
        allTokens.sortByDescending {count[it]}
        val minAppearance = count[allTokens.last()]!!
        val maxAppearance = count[allTokens.first()]!!
        log.info("Most frequent token appears $maxAppearance times. Least frequent token appears $minAppearance times.")

        val subTokens = allTokens.subList(0, (allTokens.size))
        log.info("${subTokens.size} tokens will be used for generating image.")

        val ret = mutableListOf<Word>()
        while (subTokens.isNotEmpty()) {
            val token = subTokens.random()
            val coefficient = count[token]!!.toFloat() / minAppearance.toFloat()
            val fontSize = java.lang.Float.min(coefficient * minSize, maxSize)
            log.info("Token $token appears ${count[token]} times with a coefficient $coefficient. Has a font size of $fontSize")

            val font = HelveticaCustomFont(fontSize)
            var found = false
            val horizontal = random.nextInt(100) < 85
            var word = Word(font, preferedColor, token, horizontal, Point2D.Double(0.0, 0.0))
            while (!found && word.fontSize() > minSize / 4) {
                var y = 0
                while (!found && y < image.height - word.height()) {
                    var x = 0
                    while (!found && x < image.width - word.width()) {
                        word = Word(word.font, preferedColor, token, horizontal, Point2D.Double(x.toDouble(), y.toDouble()))
                        if (!collisionAlgorithm.has(word.rectangle(), 1.0)) {
                            collisionAlgorithm.add(word.rectangle())
                            ret.add(word)
                            found = true
                        }
                        x += 4
                    }
                    y += 4
                }
                if(!found) {
                    word = word.resize(word.fontSize() - 2)
                    log.info("Token $token can not find a place. Try a smaller font with ${word.fontSize()}")
                }
            }
            subTokens.remove(token)
            foundCallback.callback(word)
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
}