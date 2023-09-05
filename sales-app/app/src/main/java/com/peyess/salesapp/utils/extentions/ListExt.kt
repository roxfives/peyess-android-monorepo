package com.peyess.salesapp.utils.extentions

fun <T> MutableList<T>.transformItem(
    mutate: (T) -> Boolean,
    newValueFactory: (T) -> T,
) {
    val iterator = listIterator()

    var curr: T
    while(iterator.hasNext()) {
        curr = iterator.next()

        if (mutate(curr)) {
            iterator.set(newValueFactory(curr))
        }
    }
}

fun <T> MutableList<T>.transformAll(
    newValueFactory: (T) -> T
) {
    val iterator = listIterator()

    while(iterator.hasNext()) {
        iterator.set(newValueFactory(iterator.next()))
    }
}