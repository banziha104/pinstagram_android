# Hilt

- ### Hilt 설정 

```groovy

// 프로젝트 레벨 build.gradle

buildscript {
    /*...*/
    dependencies {
        /*...*/
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
        /*...*/
    }
    /*...*/
}


// 앱 레벨 build.gradle

plugins {
    /*...*/
    id 'dagger.hilt.android.plugin'
    /*...*/
}

/*...*/
hilt {
    enableTransformForLocalTests = true
}

/*...*/
dependencies {
    /*...*/
    implementation "com.google.dagger:hilt-android:$hiltVersion"
    implementation "androidx.legacy:legacy-support-v4:$legacySerportVersion"
    kapt "com.google.dagger:hilt-android-compiler:$hiltVersion"
    kapt "androidx.hilt:hilt-compiler:$hiltViewModelVersion"
    kaptAndroidTest "com.google.dagger:hilt-android-compiler:$hiltVersion"
    androidTestImplementation "com.google.dagger:hilt-android-testing:$hiltVersion"
    /*...*/
}

/*...*/
```