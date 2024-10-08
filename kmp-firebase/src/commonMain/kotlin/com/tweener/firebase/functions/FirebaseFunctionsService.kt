package com.tweener.firebase.functions

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.functions.FirebaseFunctions
import dev.gitlive.firebase.functions.functions
import io.github.aakira.napier.Napier
import kotlin.time.Duration.Companion.seconds

/**
 * @author Vivien Mahe
 * @since 24/01/2024
 */
class FirebaseFunctionsService {

    companion object {
        val TIMEOUT = 30.seconds
    }

    fun getFunctions(): FirebaseFunctions? =
        try {
            Firebase.functions
        } catch (throwable: Throwable) {
            null
        }

    suspend inline fun <reified RequestType, reified ResponseType> callFunction(functionName: String, data: RequestType): ResponseType? =
        try {
            getFunctions()
                ?.httpsCallable(name = functionName, timeout = TIMEOUT)
                ?.invoke(data = data)
                ?.data<ResponseType>()
        } catch (throwable: Throwable) {
            Napier.e(throwable) { "Couldn't get response for function $functionName." }
            null
        }
}
