package cedict

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class CedictTest {
    private val cedict = Cedict("jdbc:sqlite:cedict.db")

    @TestFactory
    fun testSearchChinese() = listOf(
            "你好"
    ).map { input ->
        dynamicTest("Searching $input as Chinese") {
            println(cedict.searchChinese(input))
        }
    }

    @TestFactory
    fun testSearchPinyin() = listOf(
            "zhong1 wen2"
    ).map { input ->
        dynamicTest("Searching $input as Pinyin") {
            println(cedict.searchPinyin(input))
        }
    }

    @TestFactory
    fun testSearchEnglish() = listOf(
            "English"
    ).map { input ->
        dynamicTest("Searching $input as English") {
            println(cedict.searchEnglish(input).filterIndexed { index, _ -> index < 5 })
        }
    }

    @TestFactory
    fun testSearch() = listOf(
            "你好",
            "zhong1 wen2",
            "English"
    ).map { input ->
        dynamicTest("Searching $input as any") {
            println(cedict[input].filterIndexed { index, _ -> index < 5 })
        }
    }
}
