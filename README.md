# EasyUtils
[![](https://jitpack.io/v/mamenglong/EasyUtils.svg)](https://jitpack.io/#mamenglong/EasyUtils)
- [README](README.md)
- [更新日志](UPDATE_LOG.md)
- [功能介绍](FUNCTION.md)
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
                 EasyUtils.initialize(this);
                 //设置允许打印日志
                 EasyUtils.debug(true);
                 //or
                 LogUtils.debug(true);
             }
         }
 ```
  Make sure to call this method as early as you can. In the
 onCreate() method of Application will be fine. And always remember to
 use the application context as parameter. Do not use any instance of
 activity or service as parameter, or memory leaks might happen.

- 介绍 

项目包括两个主要模块：android，java 

android包含java中的方法 java中为通用的方法

自行添加依赖： 
``` 

    implementation 'androidx.appcompat:appcompat:1.1.0-alpha04'
    implementation 'androidx.core:core-ktx:1.1.0-alpha05'
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.5.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.8.0'
```