package com.mml.easyutils

import com.mml.easyutils.api.WeatherService
import com.mml.java.HttpServiceUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println(
            HttpServiceUtils
                .setBaseURL(WeatherService.BASE_URL)
                .create(WeatherService::class.java)
                .getWeatherData(101210101)
                .execute()
                .body()
                .toString()
        )
        println(
            HttpServiceUtils
                .setIsUseLoggingInterceptor(true)
                .setBaseURL(WeatherService.BASE_URL)
                .create(WeatherService::class.java)
                .getWeatherData(101210102)
                .execute()
                .body()
                .toString()
        )
        println(
            HttpServiceUtils
                .setBaseURL(WeatherService.BASE_URL)
                .create(WeatherService::class.java)
                .getWeatherData(101210102).execute()
                .body()
                .toString()
        )
    }

    @Test
    fun test() {

    }
}
