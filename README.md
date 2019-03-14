# EasyUtils
- [更新日志](UPDATE_LOG.md)


- Retrofit2封装
  - 自定义方法
    - setTimeOut(time:Long)
    -  setBaseURL(url: String)
    - fun setIsUseLoggingInterceptor(isUse: Boolean)
  - 调用实例
    ```
    ServiceCreator
                .setIsUseLoggingInterceptor(true)
                .setTimeOut(20L)
                .setBaseURL(WeatherService.BASE_URL)
                .create(WeatherService::class.java)
                .getWeatherData(101210101)
                .execute()
                .body()
    ```
    [参考测试类](./app/src/test/java/com/mml/easyutils/ExampleUnitTest.kt)