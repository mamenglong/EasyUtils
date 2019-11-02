# EasyUtils
[![](https://jitpack.io/v/mamenglong/EasyUtils.svg)](https://jitpack.io/#mamenglong/EasyUtils)
- [README](README.md)
- [更新日志](UPDATE_LOG.md)
- [功能介绍](FUNCTION.md)
  - [android](#android)
  - [java](#java)
  
  <p id = "android"></p>

## android 功能
- LogUtils封装
  - 使用 
    -   LogUtils.debug(true).i(tag,msg)
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
+ widget
   + ClearEditText:带清除按钮的EditText
   + CountdownView:验证码倒计时
   +  SimpleLayout:简单的Layout 
   +  BalloonRelativeLayout:产生气泡
  +  MYAutoCompleteTextView:带有删除和下拉的自动填充布局
- Base64： Base64 
- FlashLightUtils： 闪光灯， 开启、关闭闪光灯。 
+ SilentInstaller： 安装器，静默安装、卸载（仅在root过的手机上）。
+ BitmapUtil： 位图操作， 拍照，裁剪，圆角，byte、string互转，压缩，放缩，保存等
+ ByteUtil： byte工具类
+ ClassUtil： 类工具， 新建实例，判断类的类型等
   
 <p id = "java"></p>

## java功能
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
- TimeUtils,常用操作 
+ RandomUtils： 随机工具类，产生随机string或数字，随机洗牌等
- 加密：
    - [AESUtils](./java/src/main/java/com/mml/java/data/AESUtils.kt)
    - [Base64](./java/src/main/java/com/mml/java/data/Base64.kt)
    

