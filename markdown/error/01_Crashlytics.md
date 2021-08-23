# Crashlytics

- ### 그래들 설정

```groovy
// Project Level Gradle 
buildscript {
    /*...*.
    dependencies {
        /*...*/
        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.3.0'
    }
}

```


```groovy

// app/build.gradle

plugins {
    /*...*/
    id 'com.google.firebase.crashlytics'
}

buildTypes {
    release {
        /*...*/
        firebaseCrashlytics {
            nativeSymbolUploadEnabled true
            strippedNativeLibsDir "$buildDir/ndklibs/obj"
            unstrippedNativeLibsDir "$buildDir/ndklibs/libs"
        }
    }
}

dependencies {
    /*...*/
    implementation "com.google.firebase:firebase-crashlytics:$firebaseCrashlyticsVersion"
    /*...*/
}
```

<br>

- ### 어플리케이션 설정 

```kotlin
@HiltAndroidApp
class PinstagramApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
```

<br>

- ### 적용된 이미지 

![이미지](https://github.com/banziha104/pinstagram_android/blob/master/markdown/images/crashlytics.png)