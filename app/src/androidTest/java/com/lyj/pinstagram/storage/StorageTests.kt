package com.lyj.api.storage

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

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