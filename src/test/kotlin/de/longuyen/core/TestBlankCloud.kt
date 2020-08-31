package de.longuyen.core

import de.longuyen.core.cloud.BlankCloud
import de.longuyen.core.layout.Layout
import de.longuyen.fonts.AnonymousCustomFont
import java.awt.Color
import java.awt.geom.Point2D
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.test.Test

class TestBlankCloud {
    @Test
    fun `Test cloud generated image`(){
        val font10 = AnonymousCustomFont(10f)
        val font20 = AnonymousCustomFont(20f)

        val dummyLayout = object: Layout() {
            override fun produce(image: BufferedImage, text: String, preferedColor: Color, minSize: Float, maxSize: Float, callback: FoundCallback): List<Word> {
                val ret = mutableListOf<Word>()
                ret.add(Word(font10, preferedColor, "1. Horizontal 10", true, Point2D.Double(10.0, 10.0)))
                ret.add(Word(font10, preferedColor, "2. Horizontal 10", true, Point2D.Double(20.0, 100.0)))
                ret.add(Word(font20, preferedColor, "3. Vertical 20", false, Point2D.Double(30.0, 200.0)))
                return ret
            }
        }

        val blankCloud = BlankCloud(500, 500, UUID.randomUUID().toString(), dummyLayout)
        val image = blankCloud.generate()
        ImageIO.write(image, "PNG", File("target/TestBlankCloud - Test cloud generated image.png"))
    }
}