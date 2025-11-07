package com.devpush.animeapp.features.recommendation

import kotlin.math.ln
import kotlin.math.sqrt

class Tfidf {

    private fun tf(term: String, document: List<String>): Double {
        val termCount = document.count { it == term }
        return termCount.toDouble() / document.size
    }

    private fun idf(term: String, documents: List<List<String>>): Double {
        val docCount = documents.count { it.contains(term) }
        return ln(documents.size.toDouble() / (docCount + 1))
    }

    fun tfIdf(term: String, document: List<String>, documents: List<List<String>>): Double {
        return tf(term, document) * idf(term, documents)
    }

    fun cosineSimilarity(vec1: Map<String, Double>, vec2: Map<String, Double>): Double {
        val intersection = vec1.keys.intersect(vec2.keys)
        val dotProduct = intersection.sumOf { vec1.getOrDefault(it, 0.0) * vec2.getOrDefault(it, 0.0) }
        val norm1 = sqrt(vec1.values.sumOf { it * it })
        val norm2 = sqrt(vec2.values.sumOf { it * it })
        return if (norm1 == 0.0 || norm2 == 0.0) 0.0 else dotProduct / (norm1 * norm2)
    }
}
