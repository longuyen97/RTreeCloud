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
    val text = Files.readAllLines(Paths.get("data/vietnam.txt")).joinToString("\n")
    val groundTruth = ImageIO.read(File("data/vietnam.png"))
    val wordCloud = BlankCloud(groundTruth,groundTruth.width, groundTruth.height, RtreeCollisionAlgorithm(), LuceneTokenizer())
    val image = wordCloud.generate(text, Color.WHITE, 5f, 35f)
    ImageIO.write(image, "PNG", File("target/output.png"))
}