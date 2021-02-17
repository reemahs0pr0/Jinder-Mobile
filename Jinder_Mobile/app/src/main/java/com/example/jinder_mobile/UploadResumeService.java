package com.example.jinder_mobile;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadResumeService {

    @Multipart
    @POST("uploadResume")
    Call<ResponseBody> uploadResume(
            @Part MultipartBody.Part file
    );
}
