# Gradle 관리

> 여러 모듈에서 동일한 버전의 라이브러리를 불러오거나, 쉽게 수정할 수 있도록 관리가 필요

<br>

- ### dependencies.gradle 생성 및 라이브러리별 버전 명시

```groovy
ext {
    // Android
    appCompatVersion = '1.3.1'
    materialVersion = '1.4.0'
    constraintLayoutVersion = '2.0.4'
    legacySerportVersion = '1.0.0'

    // Language
    kotlinVersion = "1.5.10"
    kotlinXVersion = '0.9.1'

    // KTX
    coroutineVersion = '1.5.1'
    ktxCoreVersion = '1.6.0'
    ktxActivityVersion = '1.2.3'
    ktxFragmentVersion = '1.3.5'
    ktxViewModelVersion = '2.4.0-alpha02'
    ktxLifeCycleVersion = '2.4.0-alpha02'


    // Gradle plugin
    androidPluginVersion = '3.0.1'

    // Library
    supportLibVersion = '28.0.0'
    archComponentVersion = '1.1.1'
    multidexVersion = '1.0.1'

    // DI
    daggerVersion = '2.38.1'
    hiltVersion = '2.38.1'
    hiltViewModelVersion = '1.0.0-alpha01'

    //Image
    glideVersion = '4.12.0'

    //Converter
    gsonVersion = '2.7'
    kotlinXConverterVersion = '0.4.0'

    //Network
    okHttpVersion = '4.9.1'
    retrofitVersion = '2.9.0'

    // ReactiveX
    rxAndroidVersion = '3.0.0'
    rxBindingVersion = '4.0.0'
    rxJavaVersion = '3.0.13'
    rxKotlinVersion = '3.0.1'

    // Firebase
    firebaseStorageVersion = '28.2.1'
    firebaseCrashlyticsVersion = '17.1.0'

    // JetPack
    roomVersion = '2.3.0'
    lifeCycleVersion = '2.4.0-alpha02'

    //Animation
    lottieVersion = '3.0.1'

    // ImagePicker
    imagePickerVersion = '2.0.1'

    //Test
    mockitoVersion ='3.11.2'
    mockitoInlineVersion ='2.21.0'
    junitVersion = '4.12'
    androidxJunitVersion = '1.1.3'
    espressVersion = '3.4.0'
    androidTestRuleVersion='1.1.0'
    archTextVersion = '2.1.0'
    robolectricVersion = '4.6.1'

    //Kakao
    kakaoSdkVersion = '2.5.2'

    //Socket
    socketIoVersion = '2.0.1'

    // Jwt
    jwtVersion = '0.9.1'

    // Others
    chartVersion = 'v3.0.3'
    constraintLayout = '1.1.3'
    easyValidationVersion = '1.0.1'
    googleMapVersion='17.0.1'

}

```

<br>

- ### dependencies.gradle 을 전체 프로젝트에서 사용할 수 있도록 등록 

```groovy
// Project Level Gradle 
buildscript {
    apply from: file('dependencies.gradle')
    /*...*/
}
```

- ### 하위 모듈은 변수화된 버전을 가져와 라이브러리를 임포트함

```groovy

// app/build.gradle

dependencies{
    /*...*/
    implementation "com.jakewharton.rxbinding4:rxbinding-core:$rxBindingVersion"
    implementation "com.jakewharton.rxbinding4:rxbinding-appcompat:$rxBindingVersion"
    implementation "com.jakewharton.rxbinding4:rxbinding-recyclerview:$rxBindingVersion"
    implementation "com.jakewharton.rxbinding4:rxbinding-viewpager2:$rxBindingVersion"
    implementation "io.reactivex.rxjava3:rxandroid:$rxAndroidVersion"
    implementation "io.reactivex.rxjava3:rxjava:$rxJavaVersion"
    implementation "io.reactivex.rxjava3:rxkotlin:$rxKotlinVersion"

    //Network
    implementation "com.squareup.okhttp3:logging-interceptor:$okHttpVersion"
    implementation "com.squareup.okhttp3:okhttp:$okHttpVersion"
    implementation "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-simplexml:$retrofitVersion"
    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    /*...*/
}
```