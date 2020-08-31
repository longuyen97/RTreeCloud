package de.longuyen

import de.longuyen.collision.RtreeCollisionAlgorithm
import de.longuyen.core.cloud.BlankCloud
import de.longuyen.nlp.LuceneTokenizer
import java.awt.Color
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

fun main(){
    val text = Files.readAllLines(Paths.get("data/obama.txt")).joinToString("\n")
    val wordCloud = BlankCloud(ImageIO.read(File("data/obama.png")),1000, 1000, RtreeCollisionAlgorithm(), LuceneTokenizer())
    val image = wordCloud.generate(text, Color.WHITE, 5f, 50f)
    ImageIO.write(image, "PNG", File("target/output.png"))
}