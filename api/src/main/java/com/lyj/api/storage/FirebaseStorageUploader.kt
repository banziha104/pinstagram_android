package com.lyj.api.storage

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.lyj.core.extension.testTag
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.internal.wait
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

class FirebaseStorageUploader : StorageUploader {
    private val storage : FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    override fun upload(uri: Uri): Single<String> = Single.create{ emitter ->
        val date = Calendar.getInstance()

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