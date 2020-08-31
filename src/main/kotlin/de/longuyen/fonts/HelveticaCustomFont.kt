package de.longuyen.fonts

import java.awt.Font
import java.nio.file.Paths

class HelveticaCustomFont(size: Float) : CustomFont(size) {
    companion object {
        private val resource = AnonymousCustomFont::class.java.getResource("/Helvetica.ttf")
        private val file = Paths.get(resource.toURI()).toFile()
    }

    private val font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(size)

    override fun font(): Font {
        return font
    }

    override fun resize(size: Float): CustomFont {
        return AnonymousCustomFont(size)
    }
}