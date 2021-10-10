package com.lyj.data.repository
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.data.repository.network.AuthRepositoryImpl
import com.lyj.data.source.remote.entity.auth.request.SignInRequest
import com.lyj.domain.repository.network.AuthRepository
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AuthRepositoryTests : RepositoryTests(){

    private val authRepository: AuthRepository = AuthRepositoryImpl(generateService())

    @Test
    fun signIn(){
        authRepository
            .signIn(SignInRequest("banziha104@gmail.com","dl1212"))
            .test()
            .awaitDone(3,TimeUnit.SECONDS)
            .assertValue {
                it.data != null
            }
    }

//    @Test
//    fun signUp(){
//        authRepository
//            .signUp(SignUpRequest("banziha104@gmail.com","dl1212","테스트"))
//            .test()
//            .awaitDone(3,TimeUnit.SECONDS)
//            .assertValue {
//                it.data != null
//            }
//    }
}
