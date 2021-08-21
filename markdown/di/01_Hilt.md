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

<br>

- ### Hilt Application 생성

```kotlin
@HiltAndroidApp // Hilt 명시 
class PinstagramApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
```

<br>

- ### Hilt Module 생성

```kotlin
@Module
@InstallIn(ViewModelComponent::class)
class JwtManagerModule {

    @Provides 
    @ViewModelScoped // ViewModelScoped는 ViewModel 생성과 소멸을 같이함
    fun providerJwtModule(): JwtManager = JwtManager()
}
```

<br>

- ### Endpoint 설정 

```kotlin
@AndroidEntryPoint // EndPoint 설정 
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }),
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation {
        /*...*/
    }
```

<br>

- ### ViewModel 에서의 의존성 주입

```kotlin
@HiltViewModel // HiltViewModel 명시
class MainActivityViewModel @Inject constructor( // Inject 명시
    application: Application,
    geoCodeManager: GeoCodeManager,
    val locationEventManager: LocationEventManager,
    val contentsService: ContentsService,
    val storageUploader: StorageUploader,
    private val geometrySerivce: GeometrySerivce,
    private val database: LocalDatabase,
    private val jwtManager: JwtManager,
) : AndroidViewModel(application)
```