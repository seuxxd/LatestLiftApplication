package internet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by SEUXXD on 2017-09-12.
 */

public interface TaskIdService {
    @GET("data/get.do")
    Call<ResponseBody> getTaskID(@Header("token") String token);
}
