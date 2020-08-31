package de.longuyen.core.cloud

import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.core.Word
import de.longuyen.fonts.HelveticaCustomFont
import de.longuyen.nlp.Tokenizer
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.EXIT_ON_CLOSE

class BlankCloud(
    private val groundTruth: BufferedImage,
    private val width: Int,
    private val height: Int,
    private val collisionAlgorithm: CollisionAlgorithm,
    private val tokenizer: Tokenizer
) :
    Cloud() {
    private val log = LogManager.getLogger(BlankCloud::class.java)
    private val random = Random()
    private val frame = JFrame()
    private val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    private val graphics = image.graphics as Graphics2D
    private val panel = object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(image, 0, 0, width, height, this)
        }
    }

    init {
        frame.setSize(width, height)
        frame.add(panel)
        frame.isResizable = false
        frame.isVisible = true
        frame.defaultCloseOperation = EXIT_ON_CLOSE
    }


    override fun generate(text: String, preferedColor: Color, minSize: Float, maxSize: Float): BufferedImage {
        log.info("Generating image with width $width and height $height")
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)

        log.info("Generating words' layout for image with width ${image.width} and height ${image.height}. Min font $minSize and max font $maxSize")

        val count = tokenDistribution(text)

        val allTokens = count.keys.toMutableList()
        allTokens.sortByDescending { count[it] }

        val subTokens = allTokens.subList(0, (allTokens.size))
        val minAppearance = count[subTokens.last()]!!
        val maxAppearance = count[subTokens.first()]!!
        log.info("Most frequent token appears $maxAppearance times. Least frequent token appears $minAppearance times.")
        log.info("${subTokens.size} tokens will be used for generating image.")

        var founds = 0
        val ret = mutableListOf<Word>()
        while (subTokens.isNotEmpty()) {
            val token = subTokens.random()
            val coefficient = count[token]!!.toDouble() / minAppearance.toDouble()
            val fontSize = java.lang.Float.min((coefficient * minSize).toFloat(), maxSize)
            log.info("Token $token appears ${count[token]} times with a coefficient $coefficient. Has a font size of $fontSize")

            val font = HelveticaCustomFont(fontSize)
            var found = false
            val horizontal = random.nextInt(100) < 75
            var word = Word(font, preferedColor, token, horizontal, Point2D.Double(0.0, 0.0))

            // Find fitting shape first
            while (!found && word.fontSize() > minSize) {
                var y = 0
                while (!found && y < image.height - word.height()) {
                    var x = 0
                    while (!found && x < image.width - word.width()) {
                        word = Word(word.font, preferedColor, token, horizontal, Point2D.Double(x.toDouble(), y.toDouble()))

                        // Found place for word. Render it
                        if (!collisionAlgorithm.has(word.rectangle(), 2.0)) {
                            collisionAlgorithm.add(word.rectangle())
                            ret.add(word)
                            graphics.color = word.color
                            graphics.font = word.font.font()
                            if (word.horizontal) {
                                drawRotate(graphics, word.position.x, word.position.y + word.height(), 0, word.word)
                            } else {
                                drawRotate(graphics, word.position.x, word.position.y, 90, word.word)
                            }
                            panel.repaint()
                            panel.revalidate()
                            ImageIO.write(image, "PNG", File("target/${++founds}.png"))
                            found = true
                        }
                        x += 4
                    }
                    y += 4
                }
                if (!found) {
                    word = word.resize(word.fontSize() - minSize)
                    log.info("Token $token can not find a place. Try a smaller font with ${word.fontSize()}")
                }
            }
            subTokens.remove(token)
        }

        log.info("${ret.size} tokens found their place in the image.")

        ImageIO.write(image, "PNG", File("target/output.png"))
        return image
    }

    private fun tokenDistribution(text: String): Map<String, Int> {
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

    private fun drawRotate(g2d: Graphics2D, x: Double, y: Double, angle: Int, text: String) {
        g2d.translate(x, y)
        g2d.rotate(Math.toRadians(angle.toDouble()))
        g2d.drawString(text, 0, 0)
        g2d.rotate(-Math.toRadians(angle.toDouble()))
        g2d.translate(-x, -y)
    }
}