package de.longuyen.core.cloud

import java.awt.image.BufferedImage

abstract class Cloud{
    abstract fun generate() : BufferedImage
}