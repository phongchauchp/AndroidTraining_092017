package com.example.sony.training;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.sony.training.services.response.SearchGitHubUsersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainApplication.getmIGitHubApi()
                .searchUserGitHub(12, "")
                .enqueue(new Callback<SearchGitHubUsersResponse>() {
                    @Override
                    public void onResponse(Call<SearchGitHubUsersResponse> call, Response<SearchGitHubUsersResponse> response) {
                        Log.d(TAG, "onRespone: " + response.body().getItems().size());
                    }

                    @Override
                    public void onFailure(Call<SearchGitHubUsersResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: ", t);
                    }
                });

    }
}
