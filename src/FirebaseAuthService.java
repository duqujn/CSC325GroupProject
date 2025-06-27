import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class FirebaseAuthService {
    private static final String API_KEY = "AIzaSyArBW7BNqZ6bU0l2VNXRN_3IY0oeC7EwkM";
    private static final String Rest_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;


    public String signIn(String email, String pass) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String json = "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + pass + "\","
                + "\"returnSecureToken\":true"
                + "}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request req = new Request.Builder()
                .url(Rest_URL)
                .post(body)
                .build();

        try (Response resp = client.newCall(req).execute()) {
            if (!resp.isSuccessful())
                throw new IOException("Auth failed: " + resp.body().string());
            // parse the ID token out of the response JSON
            JSONObject o = new JSONObject(resp.body().string());
            return o.getString("idToken");
        }
    }
}
