package net.bradball.allowance.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations

/**
 * http://reactivex.io/documentation/operators/do.html
 * Because do is a reserved keyword and RxJS calls theirs "tap".
 */
inline fun <T> LiveData<T>.tap(
        crossinline fn: (value: T) -> Unit
): LiveData<T> {
    return Transformations.map(this) { value: T ->
        fn(value)
        return@map value
    }
}


/**
 * http://reactivex.io/documentation/operators/map.html
 * Because the transformation syntax is ugly and not chainable
 */
inline fun <X, Y> LiveData<X>.map(
        crossinline fn: (value: X) -> Y
): LiveData<Y> {
    return Transformations.map(this) {
        fn(it)
    }
}

/**
 * Filter a LiveData.
 *
 * @param - passesMuster - a predicate function that takes each emitted value form the
 * source LiveData, and returns a boolean indicating whether the value should
 * be emitted (true) or filtered out (false).
 *
 * @return - a Live data of the filtered values.
 */
inline fun <X> LiveData<X>.filter(crossinline passesMuster: (value: X?) -> Boolean): LiveData<X> {
    val m = MediatorLiveData<X>()
    m.addSource(this) {
        if (passesMuster(it)) {
            m.value = it
        }
    }
    return m
}

/**
 * This will allow you to observe on a LiveData until YOU
 * say when to stop. This creates an observer that will
 * remove itself when the provided `weAreDoneHere` function
 * returns true for an emission.
 */
inline fun <T> LiveData<T>.observeUntil(
        crossinline weAreDoneHere: (value: T, emissionCount: Int) -> Boolean,
        crossinline onChanged: (value: T) -> Unit) {

    val source = this
    val observer = object : Observer<T> {
        private var count = 0
        override fun onChanged(t: T?) {
            if (t != null) {
                count = count.inc()

                onChanged(t)

                if (weAreDoneHere(t, count)) {
                    source.removeObserver(this)
                }
            }
        }
    }
    source.observeForever(observer)
}


/**
 * Converts a LiveData into
 * one that will stop when the predicate returns false.
 *
 * @param shouldStop - a function that is passed in the emitted value from the source observable
 * and returns a boolean indicating whether or not the source should stop emitting values.
 */
fun <T> LiveData<T>.takeUntil(shouldStop: (T) -> Boolean): LiveData<T> {
    val m = MediatorLiveData<T>()
    m.addSource(this) {
        m.postValue(it)

        if (shouldStop(it)) {
            m.removeSource(this)
        }
    }
    return m
}


/**
 * Converts a LiveData into one that will only emit X
 * values, and then will stop.
 *
 * @param - number of emissions that should be observed.
 */
fun <T> LiveData<T>.take(count: Int): LiveData<T> {
    val m = MediatorLiveData<T>()
    var emitCount = 0
    m.addSource(this) {
        emitCount++
        if (emitCount <= count) {
            m.value = it
        } else {
            m.removeSource(this)
        }
    }
    return m
}


/**
 * When you only want an emission that meets a certain criteria,
 * and then you want to stop after an item that meets that criteria is emitted
 * once.
 *
 * Marbles Example for: takeFirst { D }
 * source: |-A---B---C---D---E---F---...---Z---A---B---C---D---...---|
 * result: |---------------D-|
 */
fun <T> LiveData<T>.takeFirst(predicate: (T?) -> Boolean): LiveData<T> {
    return this.takeUntil {
        predicate(it)
    }.filter {
        predicate(it)
    }
}

/**
 * Helpful for writing tests and mocks
 * LiveData<Boolean>.of(true) => LiveData.value == true
 */
fun <T> LiveData<T>.of(value: T): LiveData<T> = MutableLiveData<T>().apply {
    this.postValue(value)
}


/**
 * Wraps the switch map transformation so it can be used in a more functional pattern.
 */
fun <X, Y> LiveData<X>.switchMap(
        fn: (value: X) -> LiveData<Y>
): LiveData<Y> {
    return Transformations.switchMap(this, fn)
}


/**
 * Provides a live data that will only emit out when the given
 * predicate function returns true for the previous and current
 * values being emitted from the original source.
 *
 * Example Predicate:
 *
 * areDifferent(curr, prev) {
 *   return !curr.equals(prev)
 * }
 *
 * The first value for prevValue to the predicate function will
 * be null.
 */
inline fun <T> LiveData<T>.whenChanged(
        crossinline areDifferent: (currentValue: T?, prevValue: T?) -> Boolean = { curr, prev -> curr != prev }
): LiveData<T> {
    var previousValue: T? = null

    return MediatorLiveData<T>().apply {
        addSource(this@whenChanged) { newValue ->
            if (areDifferent(newValue, previousValue)) {
                previousValue = newValue
                postValue(newValue)
            }
        }
    }
}

/**
 * Provides a live data that will emit each time either the source, or
 * {@code secondSource} emits. The Emission will be a {@code Pair} of the
 * latest values from both sources. The resulting LiveData will not emit
 * until a value has been emitted from both sources. Null values do not
 * result in an emission.
 *
 * Marbles
 * this:   |-A-----B-------------C----NULL-----D------------|
 * other:  |--1-----------2-------------NULL----------4-----|
 * result: |--(A,1)-(B,1)--(B,2)--(C,2)---------(D,2)--(D,4)|
 *
 */
fun <X, Y> LiveData<X>.combineLatest(secondSource: LiveData<Y>): LiveData<Pair<X, Y>> {
    val mdl = MediatorLiveData<Pair<X, Y>>()
    val lhs = this@combineLatest.filter { data -> data != null }
    val rhs = secondSource.filter { data -> data != null }

    mdl.addSource(lhs) { first ->
        if (rhs.value != null) {
            mdl.postValue(Pair(first, rhs.value!!))
        }
    }

    mdl.addSource(rhs) { second ->
        if (lhs.value != null) {
            mdl.postValue(Pair(lhs.value!!, second))
        }
    }

    return mdl
}


fun <X, Y, Z> LiveData<X>.mergeMap(secondSource: LiveData<Y>, mapFn: (first: X, second: Y) -> Z): LiveData<Z> {
    return this@mergeMap.combineLatest(secondSource).map {
        mapFn(it.first, it.second)
    }
}


