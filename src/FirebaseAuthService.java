import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONTokener;


public class FirebaseAuthService {
private final String apiKey;
private final String restURL;
private final String finalURL;

public FirebaseAuthService(){
    InputStream in = getClass()
            .getResourceAsStream("/firebaseConfig.json");

    if(in == null){
        throw new IllegalStateException("Could not find firebase config file");
    }

    JSONObject config = new JSONObject(new JSONTokener(in));
    apiKey = config.getString("apiKey");
    restURL = config.getString("authURL");
    finalURL = restURL + apiKey;
}


    public String signIn(String email, String pass) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", pass)
                .put("returnSecureToken", true);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json: charset=utf-8"),
                payload.toString());
        Request req = new Request.Builder()
                .url(finalURL)
                .post(body)
                .build();

        try (Response resp = client.newCall(req).execute()) {
            String respBody = resp.body().string();
            System.out.println("signIn() got HTTP " + respBody.codePoints() + ": " + body);

            if (!resp.isSuccessful())
                throw new IOException("Auth failed: " + resp.body().string());

            // parse the ID token out of the response JSON
            JSONObject o = new JSONObject(respBody);

            if(!o.has("localId")){
                throw new IOException("signIn() is missing localId field");
            }
            //String uID = o.getString("localID");
            return o.getString("localId");
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
