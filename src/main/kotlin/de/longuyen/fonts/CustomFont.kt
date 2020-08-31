package de.longuyen.fonts

import java.awt.Font

abstract class CustomFont(val size: Float) {
    abstract fun font() : Font
    abstract fun height(text: String): Double
    abstract fun width(text: String): Double
    abstract fun resize(size: Float) : CustomFont
}