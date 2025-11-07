package com.devpush.animeapp.features.recommendation

import com.devpush.animeapp.data.local.entities.AnimeDataEntity

class RecommendationEngine {

    private val tfidf = Tfidf()

    fun getRecommendations(
        allAnime: List<AnimeDataEntity>,
        favoriteAnime: List<AnimeDataEntity>
    ): List<AnimeDataEntity> {
        if (favoriteAnime.isEmpty()) {
            return emptyList()
        }

        val allAnimeDocuments = allAnime.map { it.attributes.synopsis.orEmpty().split(" ") }

        val favoriteAnimeDocuments = favoriteAnime.map { it.attributes.synopsis.orEmpty().split(" ") }
        val favoriteAnimeVectors = favoriteAnimeDocuments.map { doc ->
            doc.distinct().associateWith { term ->
                tfidf.tfIdf(term, doc, allAnimeDocuments)
            }
        }

        val allAnimeVectors = allAnimeDocuments.map { doc ->
            doc.distinct().associateWith { term ->
                tfidf.tfIdf(term, doc, allAnimeDocuments)
            }
        }

        val recommendations = mutableListOf<AnimeDataEntity>()
        allAnimeVectors.forEachIndexed { index, vector ->
            val anime = allAnime[index]
            if (favoriteAnime.contains(anime)) {
                return@forEachIndexed
            }

            val similarity = favoriteAnimeVectors.maxOfOrNull { favoriteVector ->
                tfidf.cosineSimilarity(vector, favoriteVector)
            } ?: 0.0

            if (similarity > 0.1) {
                recommendations.add(anime)
            }
        }

        return recommendations.sortedByDescending { anime ->
            val vector = allAnimeVectors[allAnime.indexOf(anime)]
            favoriteAnimeVectors.maxOfOrNull { favoriteVector ->
                tfidf.cosineSimilarity(vector, favoriteVector)
            } ?: 0.0
        }
    }
}
