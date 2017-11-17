package com.example.sony.training;

import android.app.Application;

import com.example.sony.training.services.config.IGitHubApi;
import com.example.sony.training.services.config.ServiceGenerators;

/**
 * Created by phong on 11/17/17.
 */

public class MainApplication extends Application {
    public static IGitHubApi mIGitHubApi;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mIGitHubApi == null){
            mIGitHubApi = ServiceGenerators.createApiService(this);
        }
    }
    public static IGitHubApi getmIGitHubApi(){
        return mIGitHubApi;
    }
}
