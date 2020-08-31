package de.longuyen.core.cloud

import de.longuyen.core.FoundCallback
import de.longuyen.core.Word
import de.longuyen.core.layout.BlankLayout
import de.longuyen.core.layout.Layout
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants.EXIT_ON_CLOSE

class BlankCloud(
    private val width: Int,
    private val height: Int,
    private val text: String,
    private val layout: Layout
) :
    Cloud() {
    private val log = LogManager.getLogger(BlankLayout::class.java)

    override fun generate(): BufferedImage {
        log.info("Generating image with width $width and height $height")

        val ret = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = ret.graphics as Graphics2D
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, width, height)
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        layout.produce(ret, text, Color.BLACK, foundCallback = object: FoundCallback{
            private val uuid = UUID.randomUUID().toString()
            private val frame = JFrame()
            private val panel = object: JPanel(){
                override fun paintComponent(g: Graphics) {
                    super.paintComponent(g)
                    g.drawImage(ret, 0, 0, width, height, this)
                }
            }

            init {
                frame.setSize(width, height)
                frame.add(panel)
                frame.isResizable = false
                frame.isVisible = true
                frame.defaultCloseOperation = EXIT_ON_CLOSE
            }

            override fun callback(word: Word) {
                graphics.color = word.color
                graphics.font = word.font.font()
                if (word.horizontal) {
                    drawRotate(graphics, word.position.x, word.position.y + word.height(), 0, word.word)
                } else {
                    drawRotate(graphics, word.position.x, word.position.y, 90, word.word)
                }
                panel.repaint()
                panel.revalidate()
                ImageIO.write(ret, "PNG", File("target/${uuid}.png"))
            }
        })
        ImageIO.write(ret, "PNG", File("target/output.png"))
        return ret
    }

    private fun drawRotate(g2d: Graphics2D, x: Double, y: Double, angle: Int, text: String) {
        g2d.translate(x, y)
        g2d.rotate(Math.toRadians(angle.toDouble()))
        g2d.drawString(text, 0, 0)
        g2d.rotate(-Math.toRadians(angle.toDouble()))
        g2d.translate(-x, -y)
    }
}