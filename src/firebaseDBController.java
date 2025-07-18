import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * fireBaseDBController class that can be called to get the specific instance of the firestore database
 * the constructor takes in the uID to find specific user data
 */
public class firebaseDBController {
    public TableView<MealEntry> tableView;
    private final String uId;
    private static final String USERS = "users";
    private static final String col = "mealEntries";

    /**
     * Parameters contructor to take in the uId of a specific user
     * @param uId uId passed in for specific user data retrieval
     */
    public firebaseDBController(String uId) {
        this.uId = uId;
    }

    /**
     * public method to add a MealEntry object to the firestore db
     * @param entry passed in MealEntry Object
     */
    public void addData(MealEntry entry) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(USERS)
                .document(uId)
                .collection(col)
                .document(entry.getID())
                .set(entry);
    }

    /**
     * public method to populate the passed in tableView object
     * This reads from the database and sets the values to the tableView
     * @param tb
     */
    public void loadData(TableView<MealEntry> tb){
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(USERS)
                .document(uId)
                .collection("mealEntries")
                .orderBy("dateEntered")
                .get();

        try{
            List<QueryDocumentSnapshot> docs = future.get().getDocuments();
            List<MealEntry> mealEntries = new ArrayList<>(docs.size());

            for(QueryDocumentSnapshot doc : docs){
                mealEntries.add(new MealEntry(
                        doc.getId(),
                        doc.getString("dateEntered"),
                        doc.getString("mealName"),
                        doc.getString("calories"),
                        doc.getString("protein"),
                        doc.getString("carbs"),
                        doc.getString("fat")
                ));
            }
            tb.getItems().setAll(mealEntries);

        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to Load Data\n" +e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * public method to loadProfileData when the profile screen is accessed
     * @param names first/last name label passed in for update after data is retrieved from the db
     * @param goal goal weight label passed in for update after data is retrieve from the db
     */
    public void loadProfileData(Label names, Label goal){
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> future = db.collection(USERS)
                .document(uId)
                .get();
        try{
            DocumentSnapshot docs = future.get();
            String fn = "";
            String ln = "";
            String weightGoal = "";

            if(docs.exists()){
                fn = docs.getString("firstName");
                ln = docs.getString("lastName");
                weightGoal = String.valueOf(docs.getDouble("weightGoal"));
            }
            names.setText("Full Name: " + fn + " " + ln);
            goal.setText("Goal Weight: " + weightGoal);
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to Load Profile Data\n" +e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}
