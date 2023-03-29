package dev.igalaxy.comet.modules.pronouns

import com.google.common.cache.CacheBuilder
import com.google.common.collect.Sets
import dev.igalaxy.comet.util.runAsync
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.UUID

object PronounsManager {
    private val cache = CacheBuilder.newBuilder().apply {
        expireAfterAccess(Duration.ofMinutes(10))
        maximumSize(500)
    }.build<UUID, Pronouns>()

    private val inProgressFetching = Sets.newConcurrentHashSet<UUID>()

    private val pronounEvents = mutableMapOf<UUID, MutableList<(Pronouns) -> Unit>>()

    fun isCurrentlyFetching(uuid: UUID) = uuid in inProgressFetching

    fun isPronounsCached(uuid: UUID) = cache.getIfPresent(uuid) != null

    fun getPronouns(uuid: UUID) = cache.getIfPresent(uuid)!!

    @JvmOverloads
    fun cachePronouns(uuid: UUID, completion: ((Pronouns) -> Unit)? = null) {
        if (isPronounsCached(uuid) || isCurrentlyFetching(uuid))
            return

        completion?.let { listenToPronounsGet(uuid, it) }

        inProgressFetching += uuid

        runAsync {
            try {
                val pronouns = fetchPronouns(uuid)
                cache.put(uuid, pronouns)

                inProgressFetching += uuid
                pronounEvents[uuid]?.forEach {
                    it(pronouns)
                }
                pronounEvents.remove(uuid)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun bulkCachePronouns(uuids: MutableCollection<UUID>) {
        val filtered = uuids.filterNot { isCurrentlyFetching(it) || isPronounsCached(it) }
        val chunked = filtered.chunked(50)

        inProgressFetching.addAll(filtered)

        runAsync {
            runBlocking {
                val httpClient = HttpClient.newHttpClient()
                coroutineScope {
                    chunked.map {
                        async {
                            try {
                                val url = URI.create("https://pronoundb.org/api/v1/lookup-bulk?platform=minecraft&ids=${it.joinToString(",")}")
                                val request = HttpRequest.newBuilder(url).build()
                                val response = withContext(Dispatchers.IO) {
                                    httpClient.send(request, HttpResponse.BodyHandlers.ofString())
                                }

                                val pronouns = Json.decodeFromString<Map<String, String>>(response.body())
                                    .mapKeys { (k, _) -> UUID.fromString(k) }
                                    .mapValues { (_, v) -> Pronouns.fromId(v) }
                                cache.putAll(pronouns)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                inProgressFetching.removeAll(it.toSet())
                            }
                        }
                    }.awaitAll()
                }
            }
        }
    }

    private fun listenToPronounsGet(uuid: UUID, listener: (Pronouns) -> Unit) {
        pronounEvents.getOrPut(uuid) { mutableListOf() } += listener
    }

    private fun fetchPronouns(uuid: UUID): Pronouns {
        val httpClient = HttpClient.newHttpClient()
        val url = URI.create("https://pronoundb.org/api/v1/lookup?platform=minecraft&id=$uuid")
        val request = HttpRequest.newBuilder(url).build()
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        return Json.decodeFromString<SingleLookupResponse>(response.body()).toEnum()!!
    }

    @Serializable
    private data class SingleLookupResponse(val pronouns: String) {
        fun toEnum() = Pronouns.fromId(pronouns)
    }
}
