package com.lyj.domain.usecase

import android.media.metrics.Event
import com.lyj.core.rx.DisposableLifecycleController
import io.reactivex.rxjava3.core.*

interface UseCase<T>{
    fun disposeBy(controller: DisposableLifecycleController,event: Event) : T
}

abstract class SingleUseCase<T> : UseCase<Single<T>>{

}

abstract class  ObservableUseCase<T> : UseCase<Observable<T>>{

}

abstract class  FlowableUseCase<T> : UseCase<Flowable<T>>{

}

abstract class  MaybeUseCase<T> : UseCase<Maybe<T>>{

}

abstract class  CompletableUseCase : UseCase<Completable>{

}