package com.example.vocabwidget

data class Word(
    val term: String,
    val definition: String,
    val example: String,
    val level: String = "B2",
    val origin: String = "Latin",
    val pronunciation: String = "/.../",
    val synonyms: List<String> = listOf("Synonym"),
    val antonyms: List<String> = listOf("Antonym")
)

object VocabRepository {
    private val words = listOf(
        Word(
            "Ephemeral", 
            "Lasting for a very short time. This word is often used to describe natural phenomena like sunsets or the blooming of rare flowers.", 
            "The beauty of a sunset is ephemeral, fading into the night within minutes.",
            level = "C1",
            origin = "Greek",
            pronunciation = "/əˈfem.ər.əl/",
            synonyms = listOf("Fleeting", "Transient", "Momentary"),
            antonyms = listOf("Permanent", "Eternal")
        ),
        Word(
            "Serendipity", 
            "The occurrence of events by chance in a happy or beneficial way.", 
            "Winning the lottery was pure serendipity, as I rarely buy tickets.",
            level = "B2",
            origin = "English",
            pronunciation = "/ˌser.ənˈdɪp.ə.ti/",
            synonyms = listOf("Luck", "Fortuity"),
            antonyms = listOf("Misfortune")
        ),
        Word(
            "Resilient", 
            "Able to withstand or recover quickly from difficult conditions.", 
            "Despite the harsh winter, the trees remained resilient and bloomed in spring.",
            level = "B2",
            origin = "Latin",
            pronunciation = "/rɪˈzɪl.jənt/",
            synonyms = listOf("Tough", "Hardy"),
            antonyms = listOf("Fragile", "Weak")
        )
    )

    fun getRandomWord(): Word = words.random()
    fun getAllWords(): List<Word> = words
}