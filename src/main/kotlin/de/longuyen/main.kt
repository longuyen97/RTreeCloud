package de.longuyen

import de.longuyen.collision.RtreeCollisionAlgorithm
import de.longuyen.core.BlankCloud
import de.longuyen.nlp.LuceneTokenizer
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

fun main(){
    val text = Files.readAllLines(Paths.get("data/obama.txt")).joinToString("\n")
    val groundTruth = ImageIO.read(File("data/obama.jpg"))
    val wordCloud = BlankCloud(groundTruth, RtreeCollisionAlgorithm(), LuceneTokenizer())
    val image = wordCloud.generate("HELLO WORLD", 100f, 100f)
    ImageIO.write(image, "PNG", File("target/output.png"))
}