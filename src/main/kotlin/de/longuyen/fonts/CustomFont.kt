package de.longuyen.fonts

import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform

abstract class CustomFont(val size: Float) {
    private val renderContext = FontRenderContext(AffineTransform(), true, true)

    abstract fun resize(size: Float) : CustomFont
    abstract fun font() : Font

    fun height(text: String): Double {
        return font().getStringBounds(text, renderContext).height
    }

    fun width(text: String): Double {
        return font().getStringBounds(text, renderContext).width
    }
}