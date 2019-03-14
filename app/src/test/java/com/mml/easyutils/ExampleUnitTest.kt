package com.mml.easyutils

import android.util.Log
import com.mml.easyutils.api.WeatherService
import com.mml.easyutils.model.WeatherData
import com.mml.lib.network.ServiceCreator
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        println(ServiceCreator
            .setBaseURL(WeatherService.BASE_URL)
            .create(WeatherService::class.java)
            .getWeatherData(101210101)
            .execute()
            .body()
            .toString()
        )
        println(ServiceCreator
            .setIsUseLoggingInterceptor(true)
            .setBaseURL(WeatherService.BASE_URL)
            .create(WeatherService::class.java)
            .getWeatherData(101210102)
            .execute()
            .body()
            .toString()
        )
        println(ServiceCreator
            .setBaseURL(WeatherService.BASE_URL)
            .create(WeatherService::class.java)
            .getWeatherData(101210102)
            .execute()
            .body()
            .toString()
        )
    }
}
