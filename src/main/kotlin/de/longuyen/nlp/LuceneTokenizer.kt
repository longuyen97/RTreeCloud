package de.longuyen.nlp

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.custom.CustomAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import java.util.*


class LuceneTokenizer : Tokenizer {
    private val analyzer: Analyzer = CustomAnalyzer.builder()
        .withTokenizer("standard")
        .addTokenFilter("lowercase")
        .addTokenFilter("stop")
        .build()

    override fun tokenize(text: String): List<String> {
        val tokenStream = analyzer.tokenStream(UUID.randomUUID().toString(), text)
        val attr: CharTermAttribute = tokenStream.addAttribute(CharTermAttribute::class.java)
        tokenStream.reset()
        val result = mutableListOf<String>()
        while (tokenStream.incrementToken()) {
            result.add(attr.toString())
        }
        return result
    }
}