# Firebase Stroage

- ### StorageUploader 인터페이스 정의 

```kotlin
interface StorageUploader {
    fun upload(uri : Uri) : Single<String>
}
```

- ### StorageUploader 인터페이스를 구현한 객체 정의 

```kotlin
class FirebaseStorageUploader : StorageUploader {
    private val storage : FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    override fun upload(uri: Uri): Single<String> = Single.create{ emitter ->
        val date = Calendar.getInstance()

        // 스토리지 저장 규칙 /년/월/일/id.jpg
        val ref = storage.reference.child("${date[Calendar.YEAR]}/${date[Calendar.MONTH]}/${date[Calendar.DAY_OF_MONTH]}/${UUID.randomUUID()}.jpeg") 
        val uploadTask = ref.putFile(uri)
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                emitter.onSuccess(downloadUri.toString())
            } else {
                emitter.onError(task.exception)
            }
        }
    }
}
```

<br>

- ### 테스트 

```kotlin
@RunWith(AndroidJUnit4::class)
class StorageTests {
    private lateinit var uploader: StorageUploader

    private val path = "file:///storage/emulated/0/DCIM/Screenshots/Screenshot_20210624-232521_YouTube.jpg"
    @Before
    fun setUp(){
        uploader = FirebaseStorageUploader()
    }

    @Test
    fun `업로드_테스트`(){
        val uri = Uri.parse(path)
        uploader
            .upload(uri)
            .test()
            .awaitDone(10,TimeUnit.SECONDS)
            .assertNoErrors()
            .assertValue {
                it.isNotBlank()
            }
    }
}
```