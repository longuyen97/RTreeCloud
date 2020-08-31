package de.longuyen

import de.longuyen.collision.RtreeCollisionAlgorithm
import de.longuyen.core.WordCloud
import de.longuyen.core.WordLayout
import de.longuyen.nlp.LuceneTokenizer
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

fun main(){
    val text = Files.readAllLines(Paths.get("data/charliechaplin.txt")).joinToString("\n")
    val wordLayout = WordLayout(RtreeCollisionAlgorithm(), LuceneTokenizer())
    val wordCloud = WordCloud(text, wordLayout)
    val image = wordCloud.generate(500, 500)
    ImageIO.write(image, "PNG", File("target/output.png"))
}