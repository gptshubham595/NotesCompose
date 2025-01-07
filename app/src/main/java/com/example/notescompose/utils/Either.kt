package com.example.notescompose.utils


sealed class Either<out L, out R> where R : Any? {

    data class Error<out L>(val errorVal: L) : Either<L, Nothing>()

    data class Success<out R>(val successVal: R) : Either<Nothing, R>()

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Error -> fnL(errorVal)
            is Success -> fnR(successVal)
        }

    fun isError() = this is Error

    fun isSuccess() = this is Success

    fun errorValue() = if (this is Error) errorVal else null

    fun successValue() = if (this is Success) successVal else null
}

fun <T : Any?> T.toSuccess() = Either.Success(this)
val UnitSuccess = Either.Success(Unit)
