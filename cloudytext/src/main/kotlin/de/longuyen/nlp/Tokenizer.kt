package de.longuyen.nlp

interface Tokenizer {
    fun tokenize(text: String) : List<String>
}