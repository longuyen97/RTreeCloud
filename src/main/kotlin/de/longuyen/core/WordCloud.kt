package de.longuyen.core

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage


class WordCloud(private val text: String, private val wordLayout: WordLayout) {
    fun generate(width: Int, height: Int) : BufferedImage{
        val ret = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val words = wordLayout.generateLayout(width, height, text, Color.WHITE)
        val graphics = ret.graphics as Graphics2D
        for(word in words){
            val orig: AffineTransform = graphics.transform
            graphics.rotate(-Math.PI / 2)
            graphics.color = word.color
            graphics.drawString(word.word, word.position.x.toInt(), word.position.y.toInt())
            graphics.transform = orig
        }
        return ret
    }
}