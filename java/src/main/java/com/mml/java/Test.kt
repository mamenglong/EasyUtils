package com.mml.java

import com.mml.java.data.AESUtils
import com.mml.java.data.Base64
import java.nio.charset.Charset

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/4/11.
 * 修改时间：2019/4/11 13:11
 */
class Test {
    companion object {
        /** 我是main入口函数 **/
        @JvmStatic
        fun main(args: Array<String>) {
            val code = "马梦龙"
            println(code)
            val aesResult = AESUtils.encrypt("1111111111111111", "0000000000000000", code.toByteArray())

            println(AESUtils.bytesToString(aesResult))

            val aesDecrypt = AESUtils.decrypt("1111111111111111", "0000000000000000", aesResult)
            println(AESUtils.bytesToString(aesDecrypt))

            val encrypt = AESUtils.encrypt(code)
            println(encrypt)
            println(AESUtils.decrypt(encrypt))

            val e = Base64.encode(code.toByteArray(), "utf-8")
            println(e)
            println(Base64.decode(e.toByteArray()).toString(Charset.defaultCharset()))
        }
    }
}
