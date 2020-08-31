package de.longuyen.core

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

fun drawRotate(g2d: Graphics2D, x: Double, y: Double, angle: Int, text: String) {
    g2d.translate(x, y)
    g2d.rotate(Math.toRadians(angle.toDouble()))
    g2d.drawString(text, 0, 0)
    g2d.rotate(-Math.toRadians(angle.toDouble()))
    g2d.translate(-x, -y )
}

class WordCloud(private val text: String, private val wordLayout: WordLayout) {
    fun generate(width: Int, height: Int) : BufferedImage{
        val ret = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val words = wordLayout.generateLayout(width, height, text, Color.WHITE)
        val graphics = ret.graphics as Graphics2D
        for(word in words){
            graphics.color = word.color
            if(word.isVertical) {
                drawRotate(graphics, word.position.x, word.position.y, 90, word.word)
            }else{
                drawRotate(graphics, word.position.x, word.position.y, 0, word.word)
            }
        }
        return ret
    }
}