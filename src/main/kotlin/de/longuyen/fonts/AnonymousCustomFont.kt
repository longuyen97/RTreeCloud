package de.longuyen.fonts

import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.nio.file.Paths


class AnonymousCustomFont(size: Float) : CustomFont(size) {
    companion object {
        private val resource = AnonymousCustomFont::class.java.getResource("/Anonymous.ttf")
        private val file = Paths.get(resource.toURI()).toFile()
    }
    private val font = Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(size)
    private val transform = AffineTransform()
    private val renderContext = FontRenderContext(transform, true, true)

    override fun font(): Font {
        return font
    }

    override fun height(text: String): Double {
        return font.getStringBounds(text, renderContext).height
    }

    override fun width(text: String): Double {
        return font.getStringBounds(text, renderContext).width
    }

    override fun resize(size: Float): CustomFont {
        return AnonymousCustomFont(size)
    }
}