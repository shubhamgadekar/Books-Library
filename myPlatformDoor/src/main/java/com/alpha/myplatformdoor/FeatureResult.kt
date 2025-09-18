package com.alpha.myplatformdoor

sealed class FeatureResult {
    data class Success(val data: FeatureCommand) : FeatureResult()
    data class Failure(val throwable: Throwable) : FeatureResult()
}
