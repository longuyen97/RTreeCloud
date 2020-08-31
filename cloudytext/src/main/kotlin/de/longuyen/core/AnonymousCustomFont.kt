package de.longuyen.core

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

    override fun font(): Font {
        return font
    }

    override fun height(text: String): Int {
        val transform = AffineTransform()
        val frc = FontRenderContext(transform, true, true)
        return font.getStringBounds(text, frc).height.toInt()
    }

    override fun width(text: String): Int {
        val transform = AffineTransform()
        val frc = FontRenderContext(transform, true, true)
        return font.getStringBounds(text, frc).width.toInt()
    }

    override fun resize(size: Float): CustomFont {
        return AnonymousCustomFont(size)
    }
}