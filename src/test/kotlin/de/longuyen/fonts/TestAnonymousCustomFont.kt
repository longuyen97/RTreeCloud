package de.longuyen.fonts

import java.awt.Font
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestAnonymousCustomFont{

    @Test
    fun `Font can be read normally`(){
        val font = AnonymousCustomFont(10f)
        assertTrue(font.font().family == "Anonymous")
        assertTrue(font.font().name == "AnonymousRegular")
        assertEquals(font.font().style, Font.PLAIN)
        assertEquals(font.font().size, 10)
    }

    @Test
    fun `Test fonts sizes`(){
        val font = AnonymousCustomFont(10f)
        assertEquals(font.width("W"), font.width("i"))
        assertEquals(font.height("l"), font.height("o"))
    }
}