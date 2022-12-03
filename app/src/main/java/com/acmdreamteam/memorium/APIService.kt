package com.acmdreamteam.memorium

import android.telecom.Call
import com.google.android.gms.common.internal.safeparcel.SafeParcelable

interface APIService {
    @FormUrlEncoded
    @POST("sarora")
    fun chatwithBot(@SafeParcelable.Field("chatInput") chatText : String ): Call<ChatResponse>
}

data class ChatResponse(val Sarora : String) {

}