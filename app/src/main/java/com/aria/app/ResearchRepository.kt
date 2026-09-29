package com.aria.app

class ResearchRepository(private val api: ApiClient) {
    suspend fun arxiv(query: String) = api.researchArxiv(query)
    suspend fun semantic(query: String) = api.researchSemantic(query)
    suspend fun rss(url: String) = api.researchRss(url)
    suspend fun save(source: String, externalId: String, title: String, url: String) =
        api.saveResearch(source, externalId, title, url)
    suspend fun summarize(text: String) = api.summarizeResearch(text)
}
