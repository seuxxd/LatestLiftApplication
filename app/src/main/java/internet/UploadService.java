package internet;

import jsonmodel.Data;
import jsonmodel.UploadData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by SEUXXD on 2017-09-13.
 */

public interface UploadService {
    @POST("data/add.do")
    Call<ResponseBody> upload(@Header("token") String token ,
                              @Body UploadData data);
}
