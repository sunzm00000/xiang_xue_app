package com.example.fishingport.app.api;



import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Lenovo on 2017/5/22.
 */

public interface DownloadPicturesService {

    @GET
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);
}
