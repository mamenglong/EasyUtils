package com.mml.java

import java.util.*

/**
 * 随机工具类
 */

/**
 * 项目名称：Library
 * Created by Long on 2018/10/15.
 * 修改时间：2018/10/15 13:14
 * @author Long
 */
class RandomUtils private constructor() {

    init {
        throw AssertionError()
    }

    companion object {
        val NUMBERS_AND_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val NUMBERS = "0123456789"
        val LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"

        /**
         * get a fixed-length random string, its a mixture of uppercase, lowercase letters and numbers
         *
         * @param length
         * @return
         * @see RandomUtil.getRandom
         */
        fun getRandomNumbersAndLetters(length: Int): String? {
            return getRandom(NUMBERS_AND_LETTERS, length)
        }

        /**
         * get a fixed-length random string, its a mixture of numbers
         *
         * @param length
         * @return
         * @see RandomUtil.getRandom
         */
        fun getRandomNumbers(length: Int): String? {
            return getRandom(NUMBERS, length)
        }

        /**
         * get a fixed-length random string, its a mixture of uppercase and lowercase letters
         *
         * @param length
         * @return
         * @see RandomUtil.getRandom
         */
        fun getRandomLetters(length: Int): String? {
            return getRandom(LETTERS, length)
        }

        /**
         * get a fixed-length random string, its a mixture of uppercase letters
         *
         * @param length
         * @return
         * @see RandomUtil.getRandom
         */
        fun getRandomCapitalLetters(length: Int): String? {
            return getRandom(CAPITAL_LETTERS, length)
        }

        /**
         * get a fixed-length random string, its a mixture of lowercase letters
         *
         * @param length
         * @return
         * @see RandomUtil.getRandom
         */
        fun getRandomLowerCaseLetters(length: Int): String? {
            return getRandom(LOWER_CASE_LETTERS, length)
        }

        /**
         * get a fixed-length random string, its a mixture of chars in source
         *
         * @param source
         * @param length
         * @return
         *  * if source is null or empty, return null
         *  * else see [RandomUtil.getRandom]
         *
         */
        fun getRandom(source: String?, length: Int): String? {
            return if (source == null) null else getRandom(source.toCharArray(), length)
        }

        /**
         * get a fixed-length random string, its a mixture of chars in sourceChar
         *
         * @param sourceChar
         * @param length
         * @return
         *  * if sourceChar is null or empty, return null
         *  * if length less than 0, return null
         *
         */
        fun getRandom(sourceChar: CharArray?, length: Int): String? {
            if (sourceChar == null || sourceChar.size == 0 || length < 0) {
                return null
            }

            val str = StringBuilder(length)
            val random = Random()
            for (i in 0 until length) {
                str.append(sourceChar[random.nextInt(sourceChar.size)])
            }
            return str.toString()
        }

        /**
         * get random int between 0 and max
         *
         * @param max
         * @return
         *  * if max <= 0, return 0
         *  * else return random int between 0 and max
         *
         */
        fun getRandom(max: Int): Int {
            return getRandom(0, max)
        }

        /**
         * get random int between min and max
         *
         * @param min
         * @param max
         * @return
         *  * if min > max, return 0
         *  * if min == max, return min
         *  * else return random int between min and max
         *
         */
        fun getRandom(min: Int, max: Int): Int {
            if (min > max) {
                return 0
            }
            return if (min == max) {
                min
            } else min + Random().nextInt(max - min)
        }

        /**
         * Shuffling algorithm, Randomly permutes the specified array using a default source of randomness
         */
        fun shuffle(objArray: Array<Any>?): Boolean {
            return if (objArray == null) {
                false
            } else shuffle(objArray, getRandom(objArray.size))
        }

        /**
         * Shuffling algorithm, Randomly permutes the specified array
         */
        fun shuffle(objArray: Array<Any>?, shuffleCount: Int): Boolean {
            val length: Int = objArray!!.size
            if (shuffleCount < 0 || length < shuffleCount) {
                return false
            }

            for (i in 1..shuffleCount) {
                val random = getRandom(length - i)
                val temp = objArray[length - i]
                objArray[length - i] = objArray[random]
                objArray[random] = temp
            }
            return true
        }

        /**
         * Shuffling algorithm, Randomly permutes the specified int array using a default source of randomness
         */
        fun shuffle(intArray: IntArray?): IntArray? {
            return if (intArray == null) {
                null
            } else shuffle(intArray, getRandom(intArray.size))

        }

        /**
         * Shuffling algorithm, Randomly permutes the specified int array
         */
        fun shuffle(intArray: IntArray?, shuffleCount: Int): IntArray? {
            val length: Int = intArray!!.size
            if (shuffleCount < 0 || length < shuffleCount) {
                return null
            }

            val out = IntArray(shuffleCount)
            for (i in 1..shuffleCount) {
                val random = getRandom(length - i)
                out[i - 1] = intArray[random]
                val temp = intArray[length - i]
                intArray[length - i] = intArray[random]
                intArray[random] = temp
            }
            return out
        }
    }
}
