package com.phuong.myweather

import org.mockito.ArgumentMatcher
import org.mockito.Mockito

fun <T> any(type: Class<T>): T = Mockito.any(type)

fun safeAnyInt(): Int = any(Int::class.java)

fun safeAnyString(): String = any(String::class.java)

fun <T> safeAnyList(): List<T> {
    val dummyListClass = arrayListOf<T>().javaClass
    return any(dummyListClass)
}

fun <T : Any> safeEq(value: T): T = Mockito.eq(value) ?: value

fun <T : Any> safeArgThat(defaultValue: T, argumentMatcher: ArgumentMatcher<T>): T =
    Mockito.argThat(argumentMatcher) ?: defaultValue
