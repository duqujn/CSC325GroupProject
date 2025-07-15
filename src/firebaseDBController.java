import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class firebaseDBController {
    public TableView<MealEntry> tableView;
    private static final String col = "mealEntries";

    public void addData(MealEntry entry) {
        Firestore db = FirestoreClient.getFirestore();
        String docId = entry.getID();

        DocumentReference docRef = db.collection(col).document(docId);
        ApiFuture<WriteResult> future = docRef.set(entry);
    }

    public void loadData(TableView<MealEntry> tb){
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(col).get();

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
}
