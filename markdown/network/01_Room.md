# Room 

> 현재까지 프로젝트에서 로컬에 저장할만한것이 JWT Token 밖에 없어서, JWT Token의 예입니다.

<br>
 
- ### Entity 정의 (domain 모듈)
  
```kotlin

const val TOKEN_ID = 1L // 한개의 Row만 사용할 예정이기 때문에, ID는 미리 정해둠 

@Entity(tableName = "token")
class TokenEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var token: String,
)

```

<br>

- ### DAO 정의 (api 모듈)

```kotlin

@Dao
interface TokenDao {
    @Query("SELECT * FROM token where token.id == 1") // 한번만 가져오는 쿼리 
    fun findToken() : Single<List<TokenEntity>>

    @Query("SELECT * FROM token where token.id == 1") // 지속적으로 관찰하는 쿼리
    fun observeToken() : Flowable<List<TokenEntity>>

    @Query("SELECT * FROM token")
    fun findAll() : Single<List<TokenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) 
    fun refreshToken(entity: TokenEntity) : Completable

    @Query("DELETE FROM token WHERE id == :id")
    fun delete(id : Long = 1) : Completable
}

class TokenIsNotValidated() : RuntimeException("토큰이 정상적이지 않습니다")
```

<br>

- ### Database 정의 

```kotlin

@Database(
    entities = [TokenEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun tokenDao() : TokenDao 
}

class DatabaseFailException : RuntimeException("데이터 베이스 작업이 실패하였습니다.")
```

<br>

- ### Hilt Module에 등록 

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(
            context, LocalDatabase::class.java,"pinstagram.db"
        ).build()
}
```


<br>

- ### DAO 테스트 

```kotlin

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoomTests {
    private lateinit var tokenDao: TokenDao
    private lateinit var database: LocalDatabase
    private val compositeDisposable = CompositeDisposable()

    @Before
    fun `00_테스트_셋업`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,LocalDatabase::class.java
        ).build()
        tokenDao = database.tokenDao()
    }

    @Test
    @Throws(Exception::class)
    fun `01_토큰_생성`(){
        val token = "testToken"
        val request = TokenEntity(1L,token)
        compositeDisposable += tokenDao
            .refreshToken(request)
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.size == 1 && it[0].id == 1L && it[0].token == "testToken"
            }
    }

    @Test
    @Throws(Exception::class)
    fun `02_토큰_삭제`(){
        compositeDisposable += tokenDao
            .delete()
            .subscribe {  }

        tokenDao
            .findToken()
            .test()
            .assertComplete()
            .assertValue{
                it.isEmpty()
            }
    }

    @After
    fun `99_테스트_완료처리`() {
        database.close()
        compositeDisposable.clear()
    }
}
```