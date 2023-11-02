package com.fpoly.sdeliverydriver.ultis

import com.fpoly.sdeliverydriver.data.model.TokenResponse
import io.reactivex.Observable

fun getFakeTokenLogin(): Observable<TokenResponse> {
    return Observable.just(TokenResponse("accessToken", "refreshTOken"))
}