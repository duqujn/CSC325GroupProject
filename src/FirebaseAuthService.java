import java.io.IOException;
import java.io.InputStream;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * FirebaseAuthService controller to facilitate user login and authentication
 */
public class FirebaseAuthService {
private final String apiKey;
private final String restURL;
private final String finalURL;

    /**
     * constructor for FirebaseAuthService
     */
    public FirebaseAuthService(){
    InputStream in = getClass()
            .getResourceAsStream("/firebaseConfig.json");

    if(in == null){
        throw new IllegalStateException("Could not find firebase config file");
    }

    //get resources from file and create the finalURL
    JSONObject config = new JSONObject(new JSONTokener(in));
    apiKey = config.getString("apiKey");
    restURL = config.getString("authURL");
    finalURL = restURL + apiKey;
}

    /**
     * Sign in method that takes in user email and password and checks it against the firestore
     *
     * @param email valid user email
     * @param pass valid user password
     * @return returns the uID of the sucessfully authenticated user to load specific user data
     * @throws IOException expection thrown if authentication fails
     */
    public String signIn(String email, String pass) throws IOException {
        //create the HTTP client for authentication
        OkHttpClient client = new OkHttpClient();
        //create the json payload of email/password
        JSONObject payload = new JSONObject()
                .put("email", email)
                .put("password", pass)
                .put("returnSecureToken", true);
        //build auth request
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
            //return local uID for specific user data retrieval
            return o.getString("localId");
        }
    }
}
