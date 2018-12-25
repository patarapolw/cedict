package cedict

import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class CedictTest {
    private val cedict = Cedict()

    @TestFactory
    fun testSearchChinese() = listOf(
            "你好" to listOf(CedictEntry(traditional=null, simplified="你好", pinyin="ni3 hao3", english="hello/hi"))
    ).map { (input, output)->
        dynamicTest("Searching $input as Chinese, expecting $output") {
            println(cedict.searchChinese(input))
        }
    }

    @TestFactory
    fun testSearchPinyin() = listOf(
            "zhong1 wen2" to listOf(
                    CedictEntry(traditional=null, simplified="中文", pinyin="Zhong1 wen2", english="Chinese language"),
                    CedictEntry(traditional=null, simplified="香港中文大学", pinyin="Xiang1 gang3 Zhong1 wen2 Da4 xue2", english="Chinese University of Hong Kong"),
                    CedictEntry(traditional=null, simplified="中文标准交换码", pinyin="Zhong1 wen2 biao1 zhun3 jiao1 huan4 ma3",
                            english="CSIC, Chinese standard interchange code used from 1992"),
                    CedictEntry(traditional=null, simplified="独立中文笔会", pinyin="du2 li4 Zhong1 wen2 bi3 hui4",
                            english="Independent Chinese PEN center"),
                    CedictEntry(traditional=null, simplified="国家标准中文交换码",
                            pinyin="guo2 jia1 biao1 zhun3 Zhong1 wen2 jiao1 huan4 ma3",
                            english="CNS 11643, Chinese character coding adopted in Taiwan, 1986-1992"))
    ).map { (input, output)->
        dynamicTest("Searching $input as Pinyin, expecting $output") {
            println(cedict.searchPinyin(input))
        }
    }

    @TestFactory
    fun testSearchEnglish() = listOf(
            "English" to listOf(
                    CedictEntry(traditional=null, simplified="听", pinyin="ting1",
                            english="to listen/to hear/to obey/a can (loanword from English \"tin\")/classifier for canned beverages"),
                    CedictEntry(traditional=null, simplified="家", pinyin="jia1",
                            english="home/family/(polite) my (sister, uncle etc)/classifier for families or businesses/refers to the philosophical schools of pre-Han China/noun suffix for a specialist in some activity, such as a musician or revolutionary, corresponding to English -ist, -er, -ary or -ian/CL:個|个[ge4]"),
                    CedictEntry(traditional=null, simplified="作用", pinyin="zuo4 yong4", english="to act on/to affect/action/function/activity/impact/result/effect/purpose/intent/to play a role/corresponds to English -ity, -ism, -ization/CL:個|个[ge4]"),
                    CedictEntry(traditional=null, simplified="英语", pinyin="Ying1 yu3", english="English (language)"),
                    CedictEntry(traditional=null, simplified="英文", pinyin="Ying1 wen2", english="English (language)")
            )
    ).map { (input, output)->
        dynamicTest("Searching $input as English, expecting $output") {
            println(cedict.searchEnglish(input).filterIndexed { index, _ -> index < 5 })
        }
    }

    @TestFactory
    fun testSearch() = listOf(
            "你好" to listOf<CedictEntry>(),
            "zhong1 wen2" to listOf<CedictEntry>(),
            "English" to listOf<CedictEntry>()
    ).map { (input, output)->
        dynamicTest("Searching $input as any, expecting $output") {
            println(cedict[input].filterIndexed { index, _ -> index < 5 })
        }
    }

    private fun listEquals(ls1: Any?, ls2: Any?): Boolean {
        if(ls1 is List<*> && ls2 is List<*>) {
            if(ls1.size == ls2.size) {
                return ls1.mapIndexed { i, a -> listEquals(a, ls2[i]) }.all { it }
            }
        } else if (ls1 is CedictEntry && ls2 is CedictEntry) {
            return listOf(
                    ls1.english == ls2.english,
                    ls1.pinyin == ls2.pinyin,
                    ls1.simplified == ls2.simplified,
                    ls1.traditional == ls2.traditional
            ).all { it }
        }

        return ls1 == ls2
    }
}
