# KTX

- ### Fragment KTX

```kotlin
// MainActivity
private fun observeBottomTabSelected(): DisposableFunction = {
        binding
            .mainBottomNavigation
            .selectedObserver(this, default = MainTabType.HOME)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                /*...*/
            }
            .subscribe({ type ->
                supportFragmentManager.commit { // Fragment KTX
                    tabType = type
                    replace(
                        R.id.mainFragmentContainer,
                        type.getFragment()
                    )
                }
                /*...*/
            }, {
                it.printStackTrace()
            })
    }
```

<br>

- ### Lifecycle KTX

```kotlin

private fun bindData(id: Long?) {
    viewModel.requestContents(id ?: return makeWarningToast()) { response ->
        lifecycleScope.launch(Dispatchers.Main) { // Lifecycle KTX

            hideProgressLayout()

            if (!response.isOk || response.data == null) {
                makeWarningToast()
                return@launch
            }

            setUpRecyclerView(response.data!!)
        }
    }
}

```

<br>

- ### ViewModel KTX

```kotlin
@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewModel, ActivityMainBinding>(R.layout.activity_main,
        { ActivityMainBinding.inflate(it) }),
    TalkSendContact,
    ProgressController,
    RequestChangeCurrentLocation {

    @Inject
    internal lateinit var permissionManager: PermissionManager

    override val viewModel: MainActivityViewModel by viewModels() // ViewModel KTX
    
    /*...*/
}
```

