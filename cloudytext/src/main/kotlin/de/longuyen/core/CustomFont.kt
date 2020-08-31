package de.longuyen.core

import java.awt.Font

abstract class CustomFont(val size: Float) {
    abstract fun font() : Font
    abstract fun height(text: String): Int
    abstract fun width(text: String): Int
    abstract fun resize(size: Float) : CustomFont
}