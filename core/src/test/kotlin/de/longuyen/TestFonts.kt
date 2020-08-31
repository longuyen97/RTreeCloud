package de.longuyen

import org.junit.Test
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO


class TestFonts{
    @Test
    fun testFontsSuitableForWordClouding(){
        val resource = TestFonts::class.java.getResource("/Anonymous.ttf")
        val file = Paths.get(resource.toURI()).toFile()
        val font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(48f)

        val image = BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.font = font
        graphics.drawString("iii", 0, 30)
        graphics.drawString("lll", 100, 30)
        graphics.drawString("www", 0, 80)
        graphics.drawString("hhh", 100, 80)
        ImageIO.write(image, "PNG", File("target/font.png"))
    }
}