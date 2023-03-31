package com.cjcj55.literallynot.db;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @Multipart
    @POST("upload-audio")
    Call<ResponseBody> uploadAudio(@Part MultipartBody.Part file, @Part("user_id") RequestBody userId);

    @GET("retrieve-audio-files.php")
    Call<List<AudioFile>> getAudioFiles(@Query("user_id") int userId);

    @GET("retrieve-audio-file.php")
    Call<ResponseBody> getAudioFile(@Query("user_id") int userId, @Query("file_name") String fileName);
}
