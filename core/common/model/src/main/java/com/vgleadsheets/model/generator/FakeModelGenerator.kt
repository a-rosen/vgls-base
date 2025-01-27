package com.vgleadsheets.model.generator

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import java.io.IOException
import java.util.EmptyStackException
import java.util.Random
import java.util.Stack
import kotlin.random.asKotlinRandom

@Suppress("TooManyFunctions", "UnusedPrivateMember", "MagicNumber")
class FakeModelGenerator constructor(
    private val random: Random,
    /*@Named("RngSeed")*/
    private val seed: Long,
    private val stringGenerator: StringGenerator,
) {
    lateinit var possibleTags: Map<String, List<String>>

    lateinit var possibleSongs: List<Song>

    lateinit var possibleComposers: List<Composer>

    lateinit var possibleGames: List<Game>

    var generateEmptyState = false

    var maxSongs = DEFAULT_MAX_SONGS

    var maxGames = DEFAULT_MAX_GAMES
    var maxComposers = DEFAULT_MAX_COMPOSERS
    var maxTags = DEFAULT_MAX_TAGS
    var maxTagsValues = DEFAULT_MAX_TAGS_VALUES
    var maxSongsPerGame = DEFAULT_MAX_SONGS_PER_GAME

    private var skipModelGeneration = false
    private var remainingSongs: Stack<Song>? = null

    init {
        generateModels()
    }

    fun randomSong() = possibleSongs.random(random.asKotlinRandom())

    fun randomSongs() = List(random.nextInt(100)) {
        randomSong()
    }.distinctBy { it.id }

    fun randomGame() = possibleGames.random(random.asKotlinRandom())

    fun randomGames() = List(random.nextInt(100)) {
        randomGame()
    }.distinctBy { it.id }

    fun randomComposer() = possibleComposers.random(random.asKotlinRandom())

    fun randomComposers() = List(random.nextInt(100)) {
        randomComposer()
    }.distinctBy { it.id }

    fun randomTagKey() = possibleTags
        .entries
        .toList()
        .random(random.asKotlinRandom())
        .let { entry ->
            TagKey(
                id = random.nextLong(),
                name = entry.value.random(random.asKotlinRandom()),
                values = randomTagValues()
            )
        }

    fun randomTagKeys() = List(random.nextInt(20)) {
        randomTagKey()
    }

    fun randomTagValue() = possibleTags
        .entries
        .toList()
        .random(random.asKotlinRandom())
        .let { entry ->
            TagValue(
                id = random.nextLong(),
                name = entry.value.random(random.asKotlinRandom()),
                tagKeyId = random.nextLong(),
                tagKeyName = entry.key,
                songs = emptyList()
            )
        }

    fun randomTagValues() = List(random.nextInt(20)) {
        randomTagValue()
    }

    private fun generateModels() {
        if (skipModelGeneration) {
            return
        }
        skipModelGeneration = true

        random.setSeed(seed)

        if (generateEmptyState) {
            throw IOException("Arbitrarily failed a network request!")
        }

        generateComposers()
        generateTags()

        val gameCount = random.nextInt(maxGames) + 100
        val games = List(gameCount) {
            generateGame()
        }

        val filteredGames = games
            .distinctBy { it.id }
            .filter { it.songs!!.isNotEmpty() }

        possibleGames = filteredGames
    }

    private fun generateGame(): Game {
        return Game(
            id = random.nextLong(),
            name = stringGenerator.generateTitle(),
            hasVocalSongs = random.nextBoolean(),
            photoUrl = stringGenerator.generateName(),
            songs = getSongs(),
            songCount = random.nextInt(20),
            sheetsPlayed = 0,
            isFavorite = false,
            isAvailableOffline = false,
        )
    }

    @Suppress("SwallowedException")
    private fun getSongs(): List<Song> {
        if (remainingSongs == null) {
            generateSongs()
        }

        val availableSongs = remainingSongs!!
        val songCount = random.nextInt(maxSongsPerGame) + 1

        val songs = ArrayList<Song>(songCount)

        for (songIndex in 0 until songCount) {
            try {
                val song = availableSongs.pop()
                songs.add(song)
            } catch (ex: EmptyStackException) {
                return songs
            }
        }

        return songs
    }

    private fun generateSongs() {
        val songCount = random.nextInt(maxSongs) + 100

        val songs = Stack<Song>()
        val songIds = HashSet<Long>(songCount)

        for (songIndex in 0 until songCount) {
            val song = generateSong()
            val newId = song.id

            if (!songIds.contains(newId)) {
                songs.add(song)
                songIds.add(newId)
            }
        }

        remainingSongs = songs
        possibleSongs = songs.toMutableList()
    }

    private fun generateSong() = Song(
        id = random.nextLong(),
        name = stringGenerator.generateTitle(),
        filename = "${stringGenerator.generateName()} - ${stringGenerator.generateTitle()}",
        pageCount = random.nextInt(MAX_PAGE_COUNT) + 1,
        lyricPageCount = random.nextInt(MAX_PAGE_COUNT) + 1,
        altPageCount = random.nextInt(MAX_PAGE_COUNT) + 1,
        composers = getComposersForSong(),
        isFavorite = false,
        isAltSelected = false,
        isAvailableOffline = false,
        hasVocals = random.nextBoolean(),
        game = null,
        gameId = random.nextLong(),
        gameName = stringGenerator.generateTitle(),
        playCount = 0,
    )

    @Suppress("MagicNumber")
    private fun getParts(): Set<String> {
        val randomNumber = random.nextInt(10)

        return if (randomNumber < 2) {
            PARTS_WITH_VOCALS
        } else {
            PARTS_NO_VOCALS
        }
    }

    @Suppress("MagicNumber")
    private fun getComposersForSong(): List<Composer> {
        val availableComposers = possibleComposers!!
        val randomNumber = random.nextInt(10)
        val composerCount = if (randomNumber < 1) {
            3
        } else if (randomNumber < 3) {
            2
        } else {
            1
        }

        val composers = ArrayList<Composer>(composerCount)

        for (composerIndex in 0 until composerCount) {
            val whichComposer = random.nextInt(availableComposers.size - 1)
            val composer = availableComposers.get(whichComposer)
            composers.add(composer)
        }

        return composers.distinctBy { it.id }
    }

    private fun generateComposers() {
        val composerCount = random.nextInt(maxComposers) + 100
        val composers = ArrayList<Composer>(composerCount)

        for (composerIndex in 0 until composerCount) {
            val composer = generateComposer()
            composers.add(composer)
        }

        possibleComposers = composers.distinctBy { it.id }
    }

    private fun generateComposer() = Composer(
        id = random.nextLong(),
        songs = null,
        songCount = random.nextInt(10),
        hasVocalSongs = random.nextBoolean(),
        name = stringGenerator.generateName(),
        photoUrl = stringGenerator.generateName(),
        isFavorite = false,
        isAvailableOffline = false,
        sheetsPlayed = 0,
    )

    private fun getTags(): Map<String, List<String>> {
        if (possibleTags == null) {
            generateTags()
        }

        val songTags = mutableMapOf<String, List<String>>()

        possibleTags!!.forEach { possibleTag ->
            val valuesSize = possibleTag.value.size
            val selectedValue = possibleTag.value[random.nextInt(valuesSize)]

            songTags.put(possibleTag.key, listOf(selectedValue))
        }

        return songTags
    }

    private fun generateTags() {
        val tagCount = random.nextInt(maxTags) + 1
        val tags = mutableMapOf<String, List<String>>()

        for (tagIndex in 0 until tagCount) {
            val tagName = stringGenerator.generateTitle()
            val isNumericTag = random.nextBoolean()

            val tagValues = if (isNumericTag) {
                TAG_VALUES_NUMERIC
            } else {
                val tagValuesCount = random.nextInt(maxTagsValues - 1) + 1
                val tagValues = ArrayList<String>(tagValuesCount)

                for (tagValueIndex in 0 until tagValuesCount) {
                    val tagValue = stringGenerator.generateTitle()
                    tagValues.add(tagValue)
                }

                tagValues
            }

            tags[tagName] = tagValues
        }

        possibleTags = tags
    }

    companion object {
        const val DEFAULT_MAX_SONGS = 50
        const val DEFAULT_MAX_GAMES = 400
        const val DEFAULT_MAX_COMPOSERS = 200
        const val DEFAULT_MAX_TAGS = 20
        const val DEFAULT_MAX_TAGS_VALUES = 10
        const val DEFAULT_MAX_SONGS_PER_GAME = 10

        const val MAX_WORDS_PER_LOREM = 50
        const val MAX_WORDS_PER_TITLE = 5
        const val MAX_PAGE_COUNT = 2

        val PARTS_NO_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto")
        val PARTS_WITH_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto", "Vocals")

        val TAG_VALUES_NUMERIC = listOf("1", "2", "3", "4", "5")
    }
}
