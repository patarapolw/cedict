package cedict

data class CedictEntry internal constructor (
        val traditional: String?,
        val simplified: String,
        val pinyin: String,
        val english: String
)