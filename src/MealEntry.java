import javafx.beans.property.SimpleStringProperty;


/**
 * public MealEntry class that is used for display in a tableView as well as
 * values to be stored in the firestore db
 */
public class MealEntry{
   //private variables for entered data
    private final SimpleStringProperty dateEntered;
    private final SimpleStringProperty mealName;
    private final SimpleStringProperty calories;
    private final SimpleStringProperty protein;
    private final SimpleStringProperty carbs;
    private final SimpleStringProperty fat;
    private final SimpleStringProperty id;

    /**
     * MealEntry parameterized constructor
     * creates a MealEntry to be saved in the firestore db and displayed in a listView
     * @param id uId of specific user who entered the meal
     * @param dateEntered valid date of entry
     * @param mealName valid meal name
     * @param calories valid calorie count
     * @param protein valid protein count
     * @param carbs valid carb count
     * @param fat valid fat count
     */
    public MealEntry(String id, String dateEntered, String mealName, String calories, String protein, String carbs, String fat){
        this.mealName = new SimpleStringProperty(mealName);
        this.calories = new SimpleStringProperty(calories);
        this.protein = new SimpleStringProperty(protein);
        this.dateEntered = new SimpleStringProperty(dateEntered);
        this.carbs = new SimpleStringProperty(carbs);
        this.fat = new SimpleStringProperty(fat);
        this.id = new SimpleStringProperty(id);
    }

    /**
     * public getter methods
     * @return string of the uID
     */
    public String getID(){ return id.get(); }
    public String getMealName(){ return mealName.get(); }
    public String getCalories(){ return calories.get(); }
    public String getProtein(){ return protein.get(); }
    public String getCarbs(){return carbs.get(); }
    public String getDateEntered(){ return dateEntered.get(); }
    public String getFat() {return fat.get();}
}
