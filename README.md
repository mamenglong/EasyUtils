# EasyUtils
[![](https://jitpack.io/v/mamenglong/EasyUtils.svg)](https://jitpack.io/#mamenglong/EasyUtils)
- [更新日志](UPDATE_LOG.md)
- 使用
  - To get a Git project into your build:
    - Gradle
      -   Step 1. Add the JitPack repository to your build file Add it
          in your root build.gradle at the end of repositories:
           ```
             allprojects {
                 repositories {
                     ...
                     maven { url 'https://jitpack.io' }
                 }
             }
           ``` 
        - Step 2. Add the dependency
            ```
                dependencies {
                        implementation 'com.github.mamenglong.EasyUtils:android:Tag'
                        or
                        implementation 'com.github.mamenglong.EasyUtils:java:Tag'
                }
            ```    
    - maven
      - Step 1. Add the JitPack repository to your build file 
          ```
          <repositories>
               <repository>
                   <id>jitpack.io</id>
                   <url>https://jitpack.io</url>
               </repository>
           </repositories>
          ```
      -  Step 2. Add the dependency 
          ``` 
          <dependency>
              <groupId>com.github.mamenglong.EasyUtils</groupId>
              <artifactId>android/java</artifactId>
              <version>Tag</version>
          </dependency>
          ```

- 初始化 
  - Configure UtilsApplication 
    - You don't want to pass the Context param all the time. To makes
      the APIs simple, just configure the EasyUtilsApplication in
      AndroidManifest.xml as below:
        ```
        <manifest>
            <application
                android:name="com.mml.android.EasyUtilsApplication"
                ...
            >
                ...
            </application>
        </manifest>
        ```
    - Of course you may have your own Application and has already
      configured here, like:
        ``` 
            <manifest>
             <application
            android:name="com.example.MyOwnApplication" ... > 
            ... 
            </application>
            </manifest> 
        ``` 
That's OK. Utils can still live with that. Just call
EasyUtils.initialize(context) in your own Application:

    ```java
        public class MyOwnApplication extends Application {
        
            @Override
            public void onCreate() {
                super.onCreate();
                EasyUtilsApplication.initialize(this);
            }
        }
    ```

Make sure to call this method as early as you can. In the onCreate() method of Application will be fine. And always remember to use the application context as parameter. Do not use any instance of activity or service as parameter, or memory leaks might happen.

- 功能
  - Retrofit2封装
    - 自定义方法
      - setTimeOut(time:Long)
      -  setBaseURL(url: String)
      - fun setIsUseLoggingInterceptor(isUse: Boolean)
    - 调用实例
      ```
            HttpService
                  .setIsUseLoggingInterceptor(true)
                  .setTimeOut(20L)
                  .setBaseURL(WeatherService.BASE_URL)
                  .create(WeatherService::class.java)
                  .getWeatherData(101210101)
                  .execute()
                  .body()
                  
         //异步
            HttpService
                 .setIsUseLoggingInterceptor(true)
                 .setTimeOut(20L)
                 .setBaseURL(BingInterface.BingService.BASE_URL)
                 .create(BingInterface.BingService::class.java)
                 .getImageInfo(-1,1).enqueue(object :Callback<Images>(){
                      override fun onFailure(call: Call<Images>,
                      t: Throwable) {
                         TODO("not implemented")  
                     }
                     override fun onResponse(call: Call<Images>, response: Response<Images>) {
                         TODO("not implemented")  
                     }
              })
      ```
      [参考测试类](./app/src/test/java/com/mml/easyutils/ExampleUnitTest.kt)
  - LogUtil封装
    - 使用 
        - LogUtil.debug(true).i(tag,msg)
  - SharedPreferencesUtil
    - 使用
      - 新建类继承SharedPreferencesUtil ,并在里面声明变量
          ```kotlin
          class MySP(context: Context):SharedPreferencesUtil(context){
              var age by SharedPreferenceDelegates.int()
              var flag by SharedPreferenceDelegates.boolean()
              //更多声明方式参照SharedPreferencesUtil
          }
          ```
      - 调用 
          ```kotlin
           class Activity:AppCompatActivity(){
                  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
                      super.onCreate(savedInstanceState, persistentState)
                      preferences.age=99
                  }
                  private val preferences by lazy { MySP(this) }
                  
              }
          ```
  - ResourcesUtil，FileUtil
    - 使用参考类
  - DeviceUtils ：设备相关方法
  - ShellUtils：执行命令
  - WIFIUtils:检测网络