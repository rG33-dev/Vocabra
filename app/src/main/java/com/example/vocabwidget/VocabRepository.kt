package com.example.vocabwidget

data class Word(
    val term: String,
    val definition: String,
    val example: String,
    val level: String = "B2",
    val origin: String = "Latin",
    val pronunciation: String = "/.../",
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val category: String = "Modern"
)

object VocabRepository {
    private val words = listOf(
        Word(
            "Ephemeral", 
            "Lasting for a very short time. Often used to describe natural phenomena like sunsets.", 
            "The beauty of a sunset is ephemeral, fading into the night within minutes.",
            level = "C1", origin = "Greek", pronunciation = "/əˈfem.ər.əl/",
            synonyms = listOf("Fleeting", "Transient", "Momentary"),
            antonyms = listOf("Permanent", "Eternal"),
            category = "Academic"
        ),
        Word(
            "Serendipity", 
            "The occurrence of events by chance in a happy or beneficial way.", 
            "Winning the lottery was pure serendipity, as I rarely buy tickets.",
            level = "B2", origin = "English", pronunciation = "/ˌser.ənˈdɪp.ə.ti/",
            synonyms = listOf("Luck", "Fortuity", "Fluke"),
            antonyms = listOf("Misfortune"),
            category = "Modern"
        ),
        Word(
            "Resilient", 
            "Able to withstand or recover quickly from difficult conditions.", 
            "Despite the harsh winter, the trees remained resilient and bloomed in spring.",
            level = "B2", origin = "Latin", pronunciation = "/rɪˈzɪl.jənt/",
            synonyms = listOf("Tough", "Hardy"),
            category = "Modern"
        ),
        Word(
            "Petrichor", 
            "A pleasant smell that accompanies the first rain after a long period of warm, dry weather.", 
            "The smell of petrichor filled the air after the summer storm.",
            level = "C2", origin = "Greek", pronunciation = "/ˈpet.rɪ.kɔːr/",
            category = "Nature"
        ),
        Word(
            "Eloquence", 
            "Fluent or persuasive speaking or writing.", 
            "His eloquence moved the entire audience during the graduation speech.",
            level = "C1", origin = "Latin", pronunciation = "/ˈel.ə.kwəns/",
            category = "Advanced"
        ),
        Word(
            "Luminous", 
            "Full of or shedding light; bright or shining, especially in the dark.", 
            "The moon was a luminous disk in the clear night sky.",
            level = "B1", origin = "Latin", pronunciation = "/ˈluː.mɪ.nəs/",
            category = "Nature"
        ),
        Word(
            "Pensive", 
            "Engaged in, involving, or reflecting deep or serious thought.", 
            "She was in a pensive mood after reading the old letter.",
            level = "B2", origin = "French", pronunciation = "/ˈpen.sɪv/",
            category = "Academic"
        ),
        Word(
            "Ineffable", 
            "Too great or extreme to be expressed or described in words.", 
            "The joy I felt at that moment was completely ineffable.",
            level = "C1", origin = "Latin", pronunciation = "/ɪnˈef.ə.bəl/",
            category = "Advanced"
        ),
        Word(
            "Quixotic", 
            "Exceedingly idealistic; unrealistic and impractical.", 
            "He had a quixotic vision of a world without any conflict.",
            level = "C1", origin = "Spanish", pronunciation = "/kwɪkˈsɒt.ɪk/",
            category = "Academic"
        ),
        Word(
            "Mellifluous", 
            "A sound that is sweet and smooth, pleasing to hear.", 
            "The singer's mellifluous voice captivated everyone in the hall.",
            level = "C1", origin = "Latin", pronunciation = "/məˈlɪf.lu.əs/",
            category = "Advanced"
        )
    )

    fun getRandomWord(): Word = words.random()
    fun getAllWords(): List<Word> = words
    fun getCategories(): List<String> = listOf("All") + words.map { it.category }.distinct().sorted()
}