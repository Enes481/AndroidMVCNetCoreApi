package com.enestigli.androidmvcnetcoreapi.Remote;

import com.enestigli.androidmvcnetcoreapi.Model.tblUser;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IMyApi {
    @POST("api/register")
    Observable<String> registerUser(@Body tblUser user);

}
