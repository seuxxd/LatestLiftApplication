package internet;

import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by SEUXXD on 2017-09-11.
 */

// use retrofit and rxjava2 to connect to the web server
public interface LoginService {
    @POST("users/login.do")
    Call<ResponseBody> getLoginToken(@Body User user);
}
