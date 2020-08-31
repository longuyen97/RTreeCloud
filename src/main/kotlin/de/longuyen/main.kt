package de.longuyen

import de.longuyen.collision.RtreeCollisionAlgorithm
import de.longuyen.core.cloud.BlankCloud
import de.longuyen.core.layout.BlankLayout
import de.longuyen.nlp.LuceneTokenizer
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

fun main(){
    val text = Files.readAllLines(Paths.get("data/charliechaplin.txt")).joinToString("\n")
    val layout = BlankLayout(RtreeCollisionAlgorithm(), LuceneTokenizer())
    val wordCloud = BlankCloud(500, 500, text, layout)
    val image = wordCloud.generate()
    ImageIO.write(image, "PNG", File("target/output.png"))
}