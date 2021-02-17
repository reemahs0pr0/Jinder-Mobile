package com.example.jinder_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadResumeActivity extends AppCompatActivity {

    TextView messageText;
    Button uploadButton;

    private final int READ_EXTERNAL_STORAGE = 1;
    private final int READ_SDCARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);

        uploadButton = (Button)findViewById(R.id.uploadButton);
        messageText  = (TextView)findViewById(R.id.messageText);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    read_sdcard();
                }
                else {
                    ActivityCompat.requestPermissions(UploadResumeActivity.this,
                            permissions, READ_EXTERNAL_STORAGE);
                }
            }
        });

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                read_sdcard();
            } else {
                Toast msg = Toast.makeText(this,
                        "Permission denied", Toast.LENGTH_SHORT);
            }
        }
    }

    protected void read_sdcard() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, READ_SDCARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null)
            return;
        else if (requestCode == READ_SDCARD)
            on_read_sdcard(data);
    }

    protected void on_read_sdcard(Intent data) {

        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        messageText.setText("Uploading...");
                    }
                });
                uploadFile(data.getData());
            }
        }).start();
    }

    private void uploadFile(Uri fileUri) {

        String filePath = fileUri.getPath();
        String[] stringSplit = filePath.split("[:/]");
        String newPath = "/storage/" + stringSplit[2] + "/" + stringSplit[3];

        File originalFile = new File(newPath);

        RequestBody filePart = RequestBody.create(MediaType.parse("application/pdf"), originalFile);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file",
                originalFile.getName(), filePart);

        SharedPreferences pref =
                getSharedPreferences("user_credentials", MODE_PRIVATE);
        String username = pref.getString("username", null);

        // create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/user/" + username + "/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        // get client & call object for the request
        UploadResumeService client = retrofit.create(UploadResumeService.class);

        // finally, execute the request
        Call<ResponseBody> call = client.uploadResume(file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    public void run() {
                        messageText.setText(stringSplit[3] + " uploaded");

                        SharedPreferences pref =
                                getSharedPreferences("user_credentials",
                                        MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("resumeUploaded", true);
                        editor.commit();

                        Intent data = new Intent(UploadResumeActivity.this,
                                JobListActivity.class);
                        startActivity(data);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}