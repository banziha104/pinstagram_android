package com.lyj.core.rx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.reactivestreams.Publisher


interface DisposableLifecycleController : LifecycleOwner {
    val disposableLifecycleObserver: RxLifecycleObserver

    fun add(disposable: Disposable, lifecycleEvent: Lifecycle.Event) =
        disposableLifecycleObserver.add(disposable, lifecycleEvent)

    fun <T> disposeByOnStop(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_STOP)

    fun <T> disposeByOnPause(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_PAUSE)

    fun <T> disposeByOnDestroy(): LifecycleTransformer<T> =
        disposableLifecycleObserver.transformer(disposeBy = Lifecycle.Event.ON_DESTROY)
}

class RxLifecycleObserver(
    private val lifecycleOwner: LifecycleOwner
) : LifecycleObserver {

    private val onPauseDisposable = CompositeDisposable()
    private val onStopDisposable = CompositeDisposable()
    private val onDestroyDisposable = CompositeDisposable()

    private val publisher: BehaviorSubject<Lifecycle.Event> = BehaviorSubject.create()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun add(disposable: Disposable, lifecycleEvent: Lifecycle.Event) {
        when (lifecycleEvent) {
            Lifecycle.Event.ON_PAUSE -> onPauseDisposable.add(disposable)
            Lifecycle.Event.ON_STOP -> onStopDisposable.add(disposable)
            Lifecycle.Event.ON_DESTROY -> onDestroyDisposable.add(disposable)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun any(owner: LifecycleOwner, event: Lifecycle.Event) {
        publisher.onNext(event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            publisher.onComplete()
            lifecycleOwner.lifecycle.removeObserver(this)
        }

        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                onPauseDisposable.clear()
            }
            Lifecycle.Event.ON_STOP -> {
                onStopDisposable.clear()
            }
            Lifecycle.Event.ON_DESTROY -> {
                onDestroyDisposable.dispose()
                onPauseDisposable.dispose()
                onStopDisposable.dispose()
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }
    }

    fun <T> transformer(disposeBy: Lifecycle.Event) =
        LifecycleTransformer<T>(disposeBy, publisher)
}


class LifecycleTransformer<T>(
    private val lifecycleEvent: Lifecycle.Event,
    lifecycleObserver: Observable<Lifecycle.Event>
) : ObservableTransformer<T, T>,
    FlowableTransformer<T, T>,
    SingleTransformer<T, T>,
    MaybeTransformer<T, T>,
    CompletableTransformer {

    private val observable = lifecycleObserver.filter { it == lifecycleEvent }

    override fun apply(upstream: Observable<T>): ObservableSource<T> =
        upstream.takeUntil(observable)

    override fun apply(upstream: Flowable<T>): Publisher<T> =
        upstream.takeUntil(observable.toFlowable(BackpressureStrategy.LATEST))

    override fun apply(upstream: Single<T>): SingleSource<T> =
        upstream.takeUntil(observable.firstOrError())

    override fun apply(upstream: Maybe<T>): MaybeSource<T> =
        upstream.takeUntil(observable.firstElement())

    override fun apply(upstream: Completable): CompletableSource =
        upstream.ambWith(observable.concatMapCompletable { Completable.complete() })
}

fun <T> Observable<T>.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop())

fun <T> Observable<T>.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause())

fun <T> Observable<T>.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy())


fun <T> Flowable<T>.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop())

fun <T> Flowable<T>.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause())

fun <T> Flowable<T>.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy())


fun <T> Single<T>.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop())

fun <T> Single<T>.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause())

fun <T> Single<T>.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy())


fun <T> Maybe<T>.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop())

fun <T> Maybe<T>.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause())

fun <T> Maybe<T>.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy())


fun Completable.disposeByOnStop(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnStop<Any?>())

fun Completable.disposeByOnPause(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnPause<Any?>())

fun Completable.disposeByOnDestory(lifecycleController: DisposableLifecycleController) =
    this.compose(lifecycleController.disposeByOnDestroy<Any?>())


fun Disposable.disposedBy(
    lifecycleController: DisposableLifecycleController,
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) = lifecycleController.add(this, lifecycleEvent)