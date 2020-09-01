package de.longuyen.core

import com.github.davidmoten.rtree.geometry.Geometries
import com.github.davidmoten.rtree.geometry.Point
import de.longuyen.collision.CollisionAlgorithm
import de.longuyen.fonts.CustomFont
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
import java.lang.Integer.max
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.math.pow

class BlankCloud(
    private val groundTruth: BufferedImage,
    collisionAlgorithm: CollisionAlgorithm,
    private val tokenizer: Tokenizer
) {
    private val log = LogManager.getLogger(BlankCloud::class.java)
    private val random = Random()
    private val frame = JFrame()
    private val image = BufferedImage(groundTruth.width, groundTruth.height, BufferedImage.TYPE_INT_RGB)
    private val graphics = image.graphics as Graphics2D
    private val panel = object : JPanel() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(image, 0, 0, width, height, this)
        }
    }
    private val shapeCollisionAlgorithm = collisionAlgorithm.copy()
    private val pixelCollisionAlgorithm = collisionAlgorithm.copy()

    init {
        frame.setSize(groundTruth.width, groundTruth.height)
        frame.add(panel)
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)
    }


    fun generate(
        text: String,
        minSize: Float,
        maxSize: Float,
        graphicalDebug: Boolean = true,
        horizontalProbability: Int = 75,
        defaultFont: CustomFont = HelveticaCustomFont(10f)
    ): BufferedImage {
        graphics.color = Color.BLACK
        graphics.fillRect(0, 0, image.width, image.height)

        if (graphicalDebug) {
            frame.isResizable = false
            frame.isVisible = true
            frame.defaultCloseOperation = EXIT_ON_CLOSE
        }

        log.info("Generating words' layout for image with width ${image.width} and height ${image.height}. Min font $minSize and max font $maxSize")
        val count = tokenDistribution(text)

        val allTokens = count.keys.toMutableList()
        allTokens.sortByDescending { count[it] }

        val subTokens = allTokens.subList(0, (allTokens.size))
        val minAppearance = count[subTokens.last()]!!.toDouble().pow(2.0)
        val maxAppearance = count[subTokens.first()]!!.toDouble().pow(2.0)
        log.info("Most frequent token appears $maxAppearance times. Least frequent token appears $minAppearance times.")
        log.info("${subTokens.size} tokens will be used for generating image.")

        var founds = 0
        while (subTokens.isNotEmpty()) {
            val token = subTokens.random()
            val coefficient = count[token]!!.toDouble().pow(2.0) / minAppearance
            val fontSize = java.lang.Float.min((coefficient * minSize).toFloat(), maxSize)
            log.info("Token $token appears ${count[token]} times with a coefficient $coefficient. Has a font size of $fontSize")

            val tokenFont = defaultFont.resize(fontSize)
            var found = false
            val horizontal = random.nextInt(100) < horizontalProbability
            var word = Word(tokenFont, Color.WHITE, token, horizontal, Point2D.Double(0.0, 0.0))

            log.info("Staring looking for a normal place for token ${word.word}")
            // Find fitting shape first
            while (!found && word.fontSize() >= minSize / 2) {
                log.info("Token ${word.word} has the size ${word.fontSize()}")
                var y = 0
                while (!found && y < image.height - word.height()) {
                    var x = 0
                    while (!found && x < image.width - word.width()) {
                        word = Word(word.font, Color.WHITE, token, horizontal, Point2D.Double(x.toDouble(), y.toDouble()))
                        // Found place for word. Render it
                        if (!shapeCollisionAlgorithm.has(word.rectangle(), 1.0)) {
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


            log.info("Token ${word.word} is currently at (${word.position.x}, ${word.position.y})")

            // Fitting pixel
            if (!found) {
                log.info("Can not find a free space for word ${word.word} in a normal place. Try inside other words.")
                word = Word(tokenFont, Color.WHITE, token, horizontal, Point2D.Double(0.0, 0.0))
                while (!found && word.fontSize() > minSize / 2) {
                    word = word.translate(Point2D.Double(0.0, 0.0))

                    var y = 0
                    while (!found && y < image.height - word.height()) {
                        var x = 0
                        while (!found && x < image.width - word.width()) {
                            word = word.translate(Point2D.Double(x.toDouble(), y.toDouble()))
                            val temporalImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
                            val temporalGraphics = temporalImage.createGraphics()

                            temporalGraphics.setRenderingHint(
                                RenderingHints.KEY_FRACTIONALMETRICS,
                                RenderingHints.VALUE_FRACTIONALMETRICS_ON
                            )
                            temporalGraphics.setRenderingHint(
                                RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
                            )
                            temporalGraphics.color = Color.BLACK
                            temporalGraphics.fillRect(0, 0, temporalImage.width, temporalImage.height)

                            temporalGraphics.color = Color.WHITE
                            if (word.horizontal) {
                                drawRotate(temporalGraphics, word.position.x, word.position.y, 0, word.word)
                            } else {
                                drawRotate(temporalGraphics, word.position.x, word.position.y, 90, word.word)
                            }

                            val wordPixels = mutableListOf<Point>()
                            for (yi in word.position.y.toInt() until word.position.y.toInt() + word.height().toInt()) {
                                for (xi  in word.position.x.toInt() until word.position.x.toInt() + word.width().toInt()) {
                                    try {
                                        val color = Color(temporalImage.getRGB(xi, yi))
                                        if (color == word.color) {
                                            wordPixels.add(Geometries.point(xi.toFloat(), yi.toFloat()))
                                        }
                                    } catch (e: java.lang.Exception) {
                                        log.error("Error at index ($xi, $yi)")
                                        throw e
                                    }
                                }
                            }

                            var matchedPixel = false
                            for (wordPixel in wordPixels) {
                                if (pixelCollisionAlgorithm.has(wordPixel)) {
                                    matchedPixel = true
                                    break
                                }
                            }
                            found = matchedPixel.not()
                            if(found){
                                log.info("Token ${word.word} found a non-normal place at ${x} ${y}")
                            }
                            x +=4
                        }
                        y += 4
                    }
                    if (!found) {
                        word = word.resize(word.fontSize() - minSize)
                        log.info("Token $token can not find a place. Try a smaller font with ${word.fontSize()}")
                    }
                }
            }


            if (found) {
                log.info("Token ${word.word} found a place at (${word.position.x}, ${word.position.y})")
                word = word.colorize(meanRgbAt(word))

                shapeCollisionAlgorithm.add(word.rectangle())
                graphics.color = word.color
                graphics.font = word.font.font()
                if (word.horizontal) {
                    drawRotate(graphics, word.position.x, word.position.y, 0, word.word)
                } else {
                    drawRotate(graphics, word.position.x, word.position.y, 90, word.word)
                }
                if (graphicalDebug) {
                    panel.repaint()
                    panel.revalidate()
                    ImageIO.write(
                        image, "PNG", File("target/${++founds}.png")
                    )
                }

                for (yi in word.position.y.toInt() until word.position.y.toInt() + word.height().toInt()) {
                    for (xi  in word.position.x.toInt() until word.position.x.toInt() + word.width().toInt()) {
                        val color = Color(image.getRGB(xi, yi))
                        if (color == word.color) {
                            pixelCollisionAlgorithm.add(Geometries.point(xi.toFloat(), yi.toFloat()))
                        }
                    }
                }
                shapeCollisionAlgorithm.add(word.rectangle())
                pixelCollisionAlgorithm.visualize("target/pixel.png", image.width, image.height)
                shapeCollisionAlgorithm.visualize("target/shape.png", image.width, image.height)
            }

            subTokens.remove(token)
        }

        return image
    }

    private fun meanRgbAt(word: Word): Color {
        var red = 0.0
        var green = 0.0
        var blue = 0.0
        val steps = 2
        for (yi in word.position.y.toInt() until word.position.y.toInt() + word.height().toInt() step steps) {
            for (xi in word.position.x.toInt() until word.position.x.toInt() + word.width().toInt() step steps) {
                try {
                    val pixColor = Color(groundTruth.getRGB(xi, yi))
                    red += pixColor.red
                    green += pixColor.green
                    blue += pixColor.blue
                } catch (e: java.lang.Exception) {
                    log.error("Error at index ($xi, $yi)")
                    throw e
                }
            }
        }
        try {
            red /= ((word.height() * word.width()) / (steps * steps))
            green /= ((word.height() * word.width()) / (steps * steps))
            blue /= ((word.height() * word.width()) / (steps * steps))
            return Color(
                max(50, red.toInt()),
                max(50, green.toInt()),
                max(50, blue.toInt())
            )
        } catch (e: Exception) {
            return Color.WHITE
        }
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