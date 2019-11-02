# EasyUtils
- [README](README.md)
- [更新日志](UPDATE_LOG.md)
- [功能介绍](FUNCTION.md) 

- 19-10-21 上午9:52
    + 将BalloonRelativeLayout移出android库,占用太大了
<!-- ./android/src/main/java/-->
- 2019年5月7日23:53:37
  - [SharedPreferencesUtil](./android/src/main/java/com/mml/android/utils/SharedPreferencesUtils.kt)
    增加对象存储 使用方式 
    ```kotlin
    //定义
    var user by SharedPreferenceDelegates.Any()
    //使用
    sp.user=User("nihao")
           val ss=sp.user as User
            log.text.append(ss.name+"\n")
    ```

- 2019年4月14日00:28:08
  - 新增 widget
    - [MYAutoCompleteTextView](./android/src/main/java/com/mml/android/widget/MYAutoCompleteTextView.kt):带有删除和下拉的自动填充布局
    - 自定义view属性参考[MYAutoCompleteTextView1](./android/src/main/java/com/mml/android/widget/MYAutoCompleteTextView1.kt)
- 2019年4月12日18:58:33
  - 新增 widget
    - [BalloonRelativeLayout](./android/src/main/java/com/mml/android/widget/BalloonRelativeLayout.kt)，气泡布局
- 2019年4月11日15:38:20
  - 新增 java->data:AESUtils,Base64
    - [AESUtils](./java/src/main/java/com/mml/java/data/AESUtils.kt)
    - [Base64](./java/src/main/java/com/mml/java/data/Base64.kt)
- 2019年4月8日17:05:00
- 新增 widget
   + ClearEditText:带清除按钮的EditText
   + CountdownView:验证码倒计时
   + SimpleLayout:简单的Layout
    

- 2019年4月8日12:22:46 
    - 更改文件结构
  -   新增[WIFIUtils](./android/src/main/java/com/mml/android/utils/WIFIUtils.kt)
    - 新增[DeviceUtils](./android/src/main/java/com/mml/android/utils/DeviceUtils.kt)
    - 新增[ShellUtils](./android/src/main/java/com/mml/android/utils/ShellUtils.kt)
- 2019-3-19 
  - 修复logutil的bug
- 2019-3-18 
  - 新增[FileUtil](./android/src/main/java/com/mml/android/utils/FileUtils.kt)
  - [LogUtil](./android/src/main/java/com/mml/android/utils/LogUtils.kt)
  - [SharedPreferencesUtil](./android/src/main/java/com/mml/android/utils/SharedPreferencesUtils.kt)
  - [ResourcesUtil](./android/src/main/java/com/mml/android/utils/ResourcesUtils.kt)
- 2019-3-14 初始化