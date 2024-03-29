package com.spgroup.spapp.util

import android.util.Log
import com.spgroup.spapp.BuildConfig

const val TAG_MAX_LENGTH = 23
const val TAG_PREFIX = "SP_"
const val TAG_DEFAULT = "DEFAULT"
val DEBUGGABLE = BuildConfig.DEBUG

fun Any.createTag(tag: String): String {
    var newTag = TAG_PREFIX + tag
    return if (newTag.length <= TAG_MAX_LENGTH) newTag else newTag.substring(0, TAG_MAX_LENGTH)
}

fun Any.createMessage(msg: String) = "${this::class.java.simpleName} $msg"

fun Any.doLogD(tag: String = TAG_DEFAULT, msg: String) {
    if (DEBUGGABLE) {
        Log.d(createTag(tag), createMessage(msg))
    }
}

fun Any.doLogI(tag: String = TAG_DEFAULT, msg: String) {
    if (DEBUGGABLE) {
        Log.i(createTag(tag), createMessage(msg))
    }
}

fun Any.doLogE(tag: String = TAG_DEFAULT, msg: String) {
    if (DEBUGGABLE) {
        Log.e(createTag(tag), createMessage(msg))
    }
}