package de.longuyen.core.cloud

import java.awt.Color
import java.awt.image.BufferedImage

abstract class Cloud{
    abstract fun generate(text: String, preferedColor: Color, minSize: Float, maxSize: Float) : BufferedImage
}