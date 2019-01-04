package cedict

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Cedict (connectionUrl: String) {
    private val conn: Connection = DriverManager.getConnection(connectionUrl)
    private val tableName = "cedict"
    data class CedictEntry internal constructor (
            val traditional: String?,
            val simplified: String,
            val pinyin: String,
            val english: String
    )

    private fun searchOne(s: String, col1: String): Map<Int, Pair<CedictEntry, Float>> {
        val results = mutableMapOf<Int, Pair<CedictEntry, Float>>()

        try {
            val preparedStatement = conn.prepareStatement("""
                SELECT * FROM $tableName
                WHERE $col1 LIKE ?
            """.trimIndent())
            preparedStatement.setString(1, "%$s%")

            val resultSet = preparedStatement.executeQuery()

            while (resultSet.next()) {
                var trad: String? = null
                try {
                    trad = resultSet.getString("traditional")
                } catch (e: SQLException) {
                }

                results[resultSet.getInt("id")] = Pair(CedictEntry(
                        traditional = trad,
                        simplified = resultSet.getString("simplified"),
                        pinyin = resultSet.getString("pinyin"),
                        english = resultSet.getString("english")
                ), resultSet.getFloat("frequency"))
            }
        } catch (e: SQLException) {
            println(e.message)
        }

        return results.toMap()
    }

    fun searchChinese(s: String): List<CedictEntry> {
        val results = mutableListOf<CedictEntry>()

        try {
            val preparedStatement = conn.prepareStatement("""
                SELECT * FROM $tableName
                WHERE simplified LIKE ? OR traditional LIKE ?
                ORDER BY frequency DESC
            """.trimIndent())
            preparedStatement.setString(1, "%$s%")
            preparedStatement.setString(2, "%$s%")

            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                var trad: String? = null
                try {
                    trad = resultSet.getString("traditional")
                } catch (e: SQLException) {
                }

                results.add(CedictEntry(
                        traditional = trad,
                        simplified = resultSet.getString("simplified"),
                        pinyin = resultSet.getString("pinyin"),
                        english = resultSet.getString("english")
                ))
            }
        } catch (e: SQLException) {
            println(e.message)
        }

        return results
    }

    fun searchChineseMatch(s: String): List<CedictEntry> {
        val results = mutableListOf<CedictEntry>()

        try {
            val preparedStatement = conn.prepareStatement("""
                SELECT * FROM $tableName
                WHERE simplified = ? OR traditional = ?
                ORDER BY frequency DESC
            """.trimIndent())
            preparedStatement.setString(1, s)
            preparedStatement.setString(2, s)

            val resultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                var trad: String? = null
                try {
                    trad = resultSet.getString("traditional")
                } catch (e: SQLException) {
                }

                results.add(CedictEntry(
                        traditional = trad,
                        simplified = resultSet.getString("simplified"),
                        pinyin = resultSet.getString("pinyin"),
                        english = resultSet.getString("english")
                ))
            }
        } catch (e: SQLException) {
            println(e.message)
        }

        return results
    }

    fun searchEnglish(s: String): List<CedictEntry> {
        return searchOne(s, "english").values.sortedBy { -it.second }.map { it.first }
    }

    fun searchPinyin(s: String): List<CedictEntry> {
        return searchOne(s, "pinyin").values.sortedBy { -it.second }.map { it.first }
    }

    private fun searchPinyinEnglish(s: String): List<CedictEntry> {
        val results = searchOne(s, "english").toMutableMap()
        searchOne(s, "pinyin").forEach { k, v -> run {
            results[k] = v
        } }

        return results.values.sortedBy { it.second }.map { it.first }
    }

    operator fun get(s: String): List<CedictEntry> = when(Regex("\\p{IsHan}").find(s)) {
        null -> searchPinyinEnglish(s)
        else -> searchChinese(s)
    }
}
