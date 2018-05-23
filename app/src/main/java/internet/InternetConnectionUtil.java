package internet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SEUXXD on 2017-09-11.
 */

public class InternetConnectionUtil {
    public static Retrofit getRetrofit(){
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://47.93.227.21:8080/Elevator/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return mRetrofit;
    }
}
