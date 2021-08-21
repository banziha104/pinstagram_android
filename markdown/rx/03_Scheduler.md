# 스케쥴러

- subscribeOn() : 구독을 생성하는 부분의 스레드를 지정
- observeOn() : 연산자 체인에 스레드를 지정 

<br>
  
```kotlin
binding
    .writeBtnSend
    .clicks() // WriteButtonClick 시 
    .filter { viewModel.currentAsset != null }
    .throttleFirst(1, TimeUnit.SECONDS) 
    .observeOn(AndroidSchedulers.mainThread())  
    .doOnNext { showProgressLayout() } // MainThread에서 실행 
    .observeOn(Schedulers.io()) // 아래부터는 IO 스레드에서실행
    .flatMap {
        viewModel
            .requestUpload(viewModel.currentAsset!!.uri).toObservable() 
    }.flatMap {
        Observable.combineLatest(
            viewModel.getToken().toObservable()
            Observable.just(it)
        ) { a, b -> a to b }
    }
    .observeOn(AndroidSchedulers.mainThread()) // 아래부터 메인에서 실행, googleMap에 접근하기 위해 Main Thread
    .flatMap { (token, url) ->
        if (token.isEmpty() || token[0].token.isEmpty()) { 
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
    .observeOn(Schedulers.io())  // 아래부터 IO 스레드에서 실행, 
    .flatMapSingle { (url, token, lat, lng) ->
        viewModel.requestCreateContents( 
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
    .observeOn(AndroidSchedulers.mainThread()) // 아래부터 메인스레드에서 실행 
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