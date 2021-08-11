package com.lyj.api.storage

import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lyj.core.extension.testTag
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.concurrent.TimeUnit

//@RunWith(AndroidJUnit4::class)

// TODO::Firebase 설정과 초기화가 app module에 있어 app module에서 테스트 추후 방법이 있으면 복귀
class StorageTests {
//    lateinit var uploader: StorageUploader

//    @Before
//    fun setUp(){
//        uploader = FirebaseStorageUploader()
//    }
//
//    @Test
//    fun `업로드_테스트`(){
////        uploader.upload()
//        val uri = Uri.parse("file:///storage/emulated/0/DCIM/Screenshots/Screenshot_20210624-232521_YouTube.jpg")
//        uploader
//            .upload(uri)
//            .test()
//            .awaitDone(5,TimeUnit.SECONDS)
//            .assertComplete()
//            .assertNoErrors()
//            .assertValue {
//                Log.d(testTag,it)
//                it.isNotBlank()
//            }
//    }

}