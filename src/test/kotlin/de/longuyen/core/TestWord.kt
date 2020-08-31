package de.longuyen.core

import de.longuyen.fonts.AnonymousCustomFont
import java.awt.Color
import java.awt.geom.Point2D
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestWord {
    @Test
    fun `Test size of word`(){
        val font = AnonymousCustomFont(10f)
        val word1 = Word(font, Color.WHITE, "h", true, Point2D.Double(0.0, 0.0))
        val word2 = Word(font, Color.WHITE, "h", false, Point2D.Double(0.0, 0.0))

        assertEquals(word1.width(), word2.height())
        assertEquals(word1.height(), word2.width())

        assertTrue(word1.width() < word1.height())
        assertTrue(word2.width() > word2.height())
    }

    @Test
    fun testWordBoundingBox(){
        val font = AnonymousCustomFont(10f)
        val word1 = Word(font, Color.WHITE, "h", true, Point2D.Double(0.0, 0.0))

        assertEquals(word1.rectangle().x2(), font.width("h"))
        assertEquals(word1.rectangle().y2(), font.height("h"))

        val word2 = Word(font, Color.WHITE, "h", false, Point2D.Double(0.0, 0.0))
        assertEquals(word2.rectangle().x2(), font.height("h"))
        assertEquals(word2.rectangle().y2(), font.width("h"))
    }
}