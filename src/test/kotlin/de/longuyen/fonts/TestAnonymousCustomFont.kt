package de.longuyen.fonts

import kotlin.test.Test
import kotlin.test.assertEquals

class TestAnonymousCustomFont{

    @Test
    fun `Font can be read normally`(){
        val font1 = AnonymousCustomFont(10f)
        assertEquals(font1.font().size, 10)


        val font2 = AnonymousCustomFont(20f)
        assertEquals(font2.font().size, 20)
    }

    @Test
    fun `Test fonts sizes`(){
        val font1 = AnonymousCustomFont(10f)
        assertEquals(font1.width("W"), font1.width("i"))
        assertEquals(font1.height("l"), font1.height("o"))

        val font2 = AnonymousCustomFont(20f)
        assertEquals(font2.width("W"), font1.width("i") * 2)
    }
}