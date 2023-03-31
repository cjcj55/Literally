package com.cjcj55.literallynot.db;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("upload-audio")
    Call<ResponseBody> uploadAudio(@Part MultipartBody.Part file, @Part("user_id") RequestBody userId);
}
