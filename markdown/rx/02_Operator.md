# 생성 연산자

> LiveData의 fromPublisher를 사용했었지만, 현 버전에서 삭제. 생성은 just와 create위주로 진행

- ### Just

```kotlin
 Single.zip(
    Single.just(LatLng(it.latitude, it.longitude)), // just연산자로 생성
    requestContentsData(it.latitude, it.longitude)
) { a, b -> a to b }
```

<br>

- ### Create

```kotlin
  @TargetApi(Build.VERSION_CODES.M)
override fun checkAndRequestPermision(
    activity: Activity,
    permissions: Array<String>
): Single<IsAllGranted> = Single.create { emitter ->
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        emitter.onSuccess(true)
    } else {
        val permissionMap = permissions
            .map { it to activity.checkSelfPermission(it) }
            .groupBy { it.second }

        if (permissionMap[PackageManager.PERMISSION_DENIED] != null) {
            activity.requestPermissions(permissionMap[PackageManager.PERMISSION_DENIED]!!.map { it.first }
                .toTypedArray(), REQUEST_CODE)
            emitter.onSuccess(false)
        } else {
            emitter.onSuccess(true)
        }
    }
}
```

<br> 

# 변환 연산자

> concatMap과 switchMap은 사용할만한 상황이 안나와서 map과 flatMap만 사용

- ### map

```kotlin

binding
    .mainBottomNavigation
    .selectedObserver(this, default = MainTabType.HOME)
    .observeOn(AndroidSchedulers.mainThread())
    .map {
        when (it.title) {
            resString(R.string.main_tap_home_title) -> MainTabType.HOME
            resString(R.string.main_tap_map_title) -> MainTabType.MAP
            resString(R.string.main_tap_talk_title) -> MainTabType.TALK
            else -> MainTabType.HOME
        }
    }
/*...*/
```

<br>

- ### flatMap 과 flatMapSingle

```kotlin
  binding
    .writeBtnSend
    .clicks() // WriteButtonClick 시 
    .filter { viewModel.currentAsset != null }
    .throttleFirst(1, TimeUnit.SECONDS) //  1초 동안 발행된 이벤트중 첫번째만
    .observeOn(AndroidSchedulers.mainThread())
    .doOnNext { showProgressLayout() }
    .observeOn(Schedulers.io())
    .flatMap {
        viewModel
            .requestUpload(viewModel.currentAsset!!.uri).toObservable() // 이미지를 올리고
    }.flatMap {
        Observable.combineLatest(
            viewModel.getToken().toObservable(), // 데이터베이스에서 토큰을 조회하고
            Observable.just(it)
        ) { a, b -> a to b }
    }
    .observeOn(AndroidSchedulers.mainThread()) // googleMap에 접근하기 위해 Main Thread
    .flatMap { (token, url) ->
        if (token.isEmpty() || token[0].token.isEmpty()) { // 토큰 검사 
            throw TokenIsNotValidated()
        }
        Observable.just(
            CreateRequestData(
                url, token[0].token.bearerToken(),
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude
            )
        )
    }
    .observeOn(Schedulers.io())
    .flatMapSingle { (url, token, lat, lng) ->
        viewModel.requestCreateContents( // 서버에 컨텐츠 생성 요청 
            token,
            ContentsCreateRequest(
                binding.writeTxtTitle.getText(),
                binding.writeTxtDescription.getText(),
                url,
                resString(viewModel.spinnerItems[binding.writeSpinner.selectedItemPosition].origin),
                lat,
                lng
            )
        )
    }
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({
        hideProgressLayout()
        Toast.makeText(
            requireContext(),
            if (it.isOk) resString(R.string.write_toast_create_success) else it.message,
            Toast.LENGTH_LONG
        ).show()
        dismiss()
    }, {
        hideProgressLayout()
        when (it) {
            is ValidationFailedException, is TokenIsNotValidated ->
                Toast.makeText(
                    viewModel.context,
                    it.message,
                    Toast.LENGTH_LONG
                ).show()
            else -> Toast.makeText(
                viewModel.context,
                R.string.write_toast_create_fail,
                Toast.LENGTH_LONG
            ).show()
        }
        dismiss()
        it.printStackTrace()
    })
```

<br>

# 결합 연산자

```kotlin
private fun bindObservable(): DisposableFunction = {
    Observable.combineLatest(
        // 아래 연산자들 결합 
        bindTitleText(), // 제목 텍스트 변경 Observable
        bindDesciptionText(), // 설명 텍스트 변경 Observable
        bindSpinner(), // Spinner Click Observable
        bindImageButton(), // 현재 이미지 관찰 Observable
    ) { titlePair, desciptionPair, selectedItem, image ->
        WriteRequestAsset(titlePair, desciptionPair, selectedItem, image) // 반환값 정의 
    }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            val isValidated = it.title.first && it.description.first
            Log.d(testTag, "${it.toString()} ${isValidated}")
            binding.writeBtnSend.isEnabled = isValidated
            viewModel.currentAsset = if (isValidated) it else null
        }, {
            it.printStackTrace()
        })
}

```

<br>

# 필터링 연산자

- ### Skip

```kotlin

fun bindRule(rules: List<ValidateRule>): Observable<Pair<IsValidated, ErrorMessage?>> =
    editText
        .textChanges()
        .skipInitialValue() // RxBinding의 메소드로, 초기 구독시 현재 텍스트를 발행하는 데 이를 skip하는 메소드 
        .map {
            val validation = rules.firstOrNull { rule -> !rule.predicate(it.toString()) }
            val isValidated: IsValidated = validation == null
            val errorMessage = validation?.errorMessage
            val color = (ContextCompat.getColor(
                context,
                if (isValidated) R.color.borderSuccess else R.color.borderFail
            ))
            hint = if (isValidated) originHint else errorMessage
            boxStrokeColor = color
            defaultHintTextColor = ColorStateList.valueOf(color)

            (validation == null) to errorMessage
        }
```

<br>


- ### filter

```kotlin

binding
    .mainTabLayoutTop
    .selectedObserver()
    .filter { it == TabLayoutEventType.SELECTED } // 셀렉트 연산만 필터링
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({
        if (it.text != null) {
            if (it.text == resString(R.string.main_top_tab_all)) {
                viewModel.currentContentsList.postValue(viewModel.originContentsList.value)
            } else {
                val type = ContentsTagType.findByKoreanTitle(this, it.text!!)
                if (type != null) viewModel.currentContentsList.postValue(viewModel.originContentsList.value?.filter { it.tag == type })
            }
        }
    }, {
        it.printStackTrace()
    })

enum class TabLayoutEventType(
    var position : Int? = null,
    var text : String? = null
){
    SELECTED, UNSELECTED, RESELECTED
}


```