# Pinstagram Android Mobile Client

> Kotlin, AndroidX, JetPack, Rx, Hilt(Dagger), Coroutine 을 활용한 Android 프로젝트

<br>

## 목차

### [1.Pinstagram 프로젝트](#Pinstagram-프로젝트)
### [2.개요](#개요)
### [3.화면 설명](#화면-설명)
### [4.주요 구현 사항](#주요-구현-사항)
### [5.기술부채](#기술-부채)
### [6.후기](#후기)

<br>

## Pinstagram 프로젝트

- ### 기획 배경
    - 새로운 기술을 도입해야 될 때마다 새로운 토이프로젝트를 만들었는데(그러다보니 레파지토리가 250개를 넘었습니다..), 이렇게 계속 진행하다보니 다른 기술들과 유기적으로 연결되지 못하는 느낌을 받았습니다.
    - 이를 개선하고자 하나의 토이 프로젝트를 만들고, 이를 지속적으로 개선해나가는 것이 좋겠다고 판단하여 시작한 프로젝트입니다.
- ### 서비스 내용
    - 기획 내용은 단순히 특정한 위치에 사진을 올리고, 이를 볼 수 있는 서비스입니다.
    - 개발자가 처음으로 하는 프로젝트인 헬로우월드, 그리고 그 다음으로 많이하는 계산기, 그리고 그와 쌍두마차를 이루는 기본적인 게시판 어플의 확장 버전입니다.
- ### 개인적인 규약
    - Git Submodule 미활용 : WAS와 Android의 경우 각 모듈별로 git submodule에 등록하는게 효율적이지만, 레파지토리가 너무 많아지면 관리가 안될 것 같아서 통합된 Git으로 관리합니다.
    - 기획 디자인 최소화 : 개발적인 프로젝트이기 때문에 사업성, 당위성 등은 최대한 배제하고 개발하기에 깔끔한 기획과 디자인만 가져갑니다. 그렇기 때문에 기획 디자인적으로 뜬금없는 페이지(예: Talk)가 들어갈 수 있습니다
    - 기술부채 : 한시적으로 끝나는 프로젝트가 아닌, 지속적인 프로젝트임으로, 현재 가능한 기술을 사용하고, 볼륨 및 러닝커브로 못 가져간 기술은 천천히 도입할 예정입니다.
- ### 서비스 주요 기능 
    - Home : 특정 위치에서 1KM 반경내에 있는 데이터들을 그리드뷰 형식으로 보여줍니다
    - Map : 특정 위치에서 1KM 반경내에 있는 데이터들을 지도 형식으로 보여줍니다.
    - Talk : Socket 통신을 이용한 메세지입니다. 위치기반으로 하는 지역톡 개념이아닌 서비스 이용자 전체와 소통합니다.
    - Write : 특정 위치에 게시물을 명시할 수 있습니다.
    - Auth : 로그인, 로그아웃, 회원가입 등을 진행할 수 있습니다.

- ### 프로젝트 목록  
    - ### [📱 Pinstagram Android (Kotlin & AndroidX)](https://github.com/banziha104/pinstagram_android)
    - ### [🍃 Pinstagram WAS (Spring Boot)](https://github.com/banziha104/pinstagram-was)
    - ### [🚚 Pinstagram DevOps (GKE & K8s & Helm)](https://github.com/banziha104/pinstagram_charts)
    - ### [🕳 Pinstagram Socket (Node.js & Socket.io)](https://github.com/banziha104/pinstagram_socket)


<br>

## 개요 

- ### 모듈 설명

| 이름       | 개요                                           |
|----------|----------------------------------------------|
| app      | 실행되는 Application 을 담은 모듈                     |
| customui | Custom View를 모아둔 모듈                          |
| core     | 다양한 프로젝트에서 공통적으로 사용되는 것들을 모아둔 모듈             |
| api      | Http, Socket, Database, Storage 등 통신을 정의한 모듈 |
| domain   | api 등에서 사용할 Domain 객체를 모아둔 모듈                |

<br>

- ### 모듈 구조 

![모듈 구조](https://github.com/banziha104/pinstagram_android/blob/master/markdown/images/module_relation.png)

<br>

- ### 주요 사용 라이브러리 

| 라이브러리명         | 개요                                                | 범주                           |
|----------------------|-----------------------------------------------------|--------------------------------|
| Rxjava, RxKotlin     | FRP 프로그래밍 라이브러리                           | Functional Reactive Programing |
| KTX                  | core, activity, fragment, viewmodel, lifecycle 사용 | Android                        |
| Dagger, Hilt         | DI Container                                        | Dependency Injection           |
| ViewBinding          | View Dependency Injection                           | Dependency Injection           |
| Retrofit, okHttp     | Networking                                          | Networking                     |
| Gson, Rx Conveter    | Network Convert                                     | Networking                     |
| Socket.io            | Socket Networking                                   | Networking                     |
| JWT                  | Javascript Web Token Authenfitication               | Networking                     |
| JUnit4               | Unit Testing                                        | Testing                        |
| Espresso             | UI Testing                                          | Testing                        |
| Robolectric          | Instrumented unit Testing                           | Testing                        |
| Room                 | Local Database                                      | Database                       |
| Lottie               | 60fps Animation                                     | Animation                      |
| Firebase Storage     | Image Storage                                       | Storage                        |
| Firebase Crashlytics | Error Tracker                                       | Tracker                        |

<br>

## 화면 설명

| 이름             | 개요                     | 기능                                | 상위 클래스         |
|----------------|------------------------|-----------------------------------|----------------|
| SplashActivity | Splash 화면              | 권한 체크 및 요청                        | Activity       |
| MainActivty    | Main 화면 Container      | 각 Dialog, Fragment 생성 및 이벤트 처리    | Activity       |
| HomeFragment   | Main 화면의 메인뷰 (Default) | 데이터를 그리드뷰 형식으로 표현                 | Fragment       |
| MapFragment    | Main 화면의 맵을 활용한 뷰      | 데이터를 맵뷰 형식으로 표현                   | Fragment       |
| TalkFragment   | Main 화면의 메시지 뷰         | 소켓 통신으로 이용중인 유저간의 대화가 가능          | Fragment       |
| SignDialog     | Sign 화면의 Container     | SignInFragment, SignUpFragment 전환 | DialogFragment |
| SignInFragment | Sign 화면의 로그인 뷰         | 로그인                               | Fragment       |
| SignUpFramgent | Sign 화면의 회원가입 뷰        | 회원가입                              | Fragment       |
| LogoutDialog   | Logout 요청 뷰            | 로그아웃                              | AlertDialog    |
| AddressDialog  | 주소 선택 뷰                | 주소 변경                             | DialogFragment |
| WriteDialog    | 게시글 생성 뷰               | 게시글 생성                            | DialogFragment |
| DetailActivity | 게시물 자세히 보기 뷰           | 게시물 자세히보기                         | DetailActivity |

<br>

- ### 화면 관계 

![View](https://github.com/banziha104/pinstagram_android/blob/master/markdown/images/view_relation.png)

<br>

## 주요 구현 사항 

> 클릭하시면 해당 마크다운 파일로 리다이렉팅 됩니다.

<br>

- ### Gradle
  - #### [Gradle 관리](https://github.com/banziha104/pinstagram_android/blob/master/markdown/gradle/01_Gradle.md)
- ### Animation & View
  - #### [Lottie Animation](https://github.com/banziha104/pinstagram_android/blob/master/markdown/animation/01_Lottie.md)
  - #### [Custom View](https://github.com/banziha104/pinstagram_android/blob/master/markdown/animation/02_Custom_View.md)
  - #### [View Binding](https://github.com/banziha104/pinstagram_android/blob/master/markdown/animation/03_View_Binding.md)
- ### Language
  - #### [Generic](https://github.com/banziha104/pinstagram_android/blob/master/markdown/language/01_Generic.md)
  - #### [Enum](https://github.com/banziha104/pinstagram_android/blob/master/markdown/language/02_Enum.md)
  - #### [Delegate](https://github.com/banziha104/pinstagram_android/blob/master/markdown/language/03_Deletagate.md)
  - #### [Sealed Class](https://github.com/banziha104/pinstagram_android/blob/master/markdown/language/04_Sealed_Class.md)
- ### JetPack : Hilt, Room 등은 각자 범주에 맞는 곳에 명시
  - #### [LifeCycle](https://github.com/banziha104/pinstagram_android/blob/master/markdown/jetpack/01_Lifecycle.md)
  - #### [ViewModel](https://github.com/banziha104/pinstagram_android/blob/master/markdown/jetpack/02_ViewModel.md)
  - #### [KTX](https://github.com/banziha104/pinstagram_android/blob/master/markdown/jetpack/03_KTX.md)
- ### Dependency Inject
  - #### [Hilt](https://github.com/banziha104/pinstagram_android/blob/master/markdown/di/01_Hilt.md)
- ### Api
  - #### [Room](https://github.com/banziha104/pinstagram_android/blob/master/markdown/network/01_Room.md)
  - #### [Retrofit & OkHttp & JWT](https://github.com/banziha104/pinstagram_android/blob/master/markdown/network/02_Retrofit.md)
  - #### [Socket](https://github.com/banziha104/pinstagram_android/blob/master/markdown/network/03_Socket.md)
  - #### [Firebase Storage](https://github.com/banziha104/pinstagram_android/blob/master/markdown/network/04_Storage.md)
- ### ReactiveX
  - #### [ReactiveX 와 Coroutine](https://github.com/banziha104/pinstagram_android/blob/master/markdown/rx/01_Rxjava_Coroutine.md)
  - #### [연산자](https://github.com/banziha104/pinstagram_android/blob/master/markdown/rx/02_Operator.md)
  - #### [스케쥴러](https://github.com/banziha104/pinstagram_android/blob/master/markdown/rx/03_Scheduler.md)
  - #### [doOn 시리즈](https://github.com/banziha104/pinstagram_android/blob/master/markdown/rx/04_DoOn.md)
  - #### [Dispose Lifecycle 객체 처리](https://github.com/banziha104/pinstagram_android/blob/master/markdown/rx/05_Disposable.md)
- ### ErrorTracker
  - #### [Firebase Crashlytics](https://github.com/banziha104/pinstagram_android/blob/master/markdown/error/01_Crashlytics.md)
- ### Test
  - #### [계측 테스트](https://github.com/banziha104/pinstagram_android/blob/master/markdown/error/01_Instruments.md)
  - #### [UI 테스트](https://github.com/banziha104/pinstagram_android/blob/master/markdown/error/02_UI.md)

## 기술 부채

- ### **CI/CD**

## 후기