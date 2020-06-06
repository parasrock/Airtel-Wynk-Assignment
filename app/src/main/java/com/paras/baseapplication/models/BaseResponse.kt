package com.paras.baseapplication.models

data class BaseResponse<T>(var loading: Boolean, var error: Throwable?, var anyObject: T?)