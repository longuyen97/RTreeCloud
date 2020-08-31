package de.longuyen.core

import de.longuyen.collision.RtreeCollisionAlgorithm
import de.longuyen.core.layout.BlankLayout
import de.longuyen.nlp.LuceneTokenizer
import org.junit.Test
import java.awt.Color
import java.awt.image.BufferedImage

class TestBlankLayout {
    @Test
    fun testBlankLayout(){
        val layout = BlankLayout(RtreeCollisionAlgorithm(), LuceneTokenizer())
        val image = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
        val words = layout.produce(image, "We Must Go To War", Color.WHITE, 30f, 30f, object: FoundCallback{
            override fun callback(word: Word) {
            }
        })
        println(words)
    }
}