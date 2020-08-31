package de.longuyen.core.cloud

import de.longuyen.core.layout.BlankLayout
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class BlankCloud(private val width: Int, private val height: Int, private val text: String, private val layout: BlankLayout) {
    private val log = LogManager.getLogger(BlankLayout::class.java)

    fun generate() : BufferedImage{
        log.info("Generating image with width $width and height $height")

        val ret = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val words = layout.produce(ret, text, Color.WHITE)
        val graphics = ret.graphics as Graphics2D

        for(word in words){
            graphics.color = word.color
            if(word.horizontal) {
                drawRotate(graphics, word.position.x, word.position.y, 90, word.word)
            }else{
                drawRotate(graphics, word.position.x, word.position.y, 0, word.word)
            }
        }
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