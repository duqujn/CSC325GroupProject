import javafx.beans.property.SimpleStringProperty;


//separate class to facilitate MealEntry in the Main Screen
public class MealEntry{
   //private variables for entered data
    private final SimpleStringProperty dateEntered;
    private final SimpleStringProperty mealName;
    private final SimpleStringProperty calories;
    private final SimpleStringProperty protein;
    private final SimpleStringProperty carbs;
    private final SimpleStringProperty fat;


    //constructor
    public MealEntry(String dateEntered, String mealName, String calories, String protein, String carbs, String fat){
        this.mealName = new SimpleStringProperty(mealName);
        this.calories = new SimpleStringProperty(calories);
        this.protein = new SimpleStringProperty(protein);
        this.dateEntered = new SimpleStringProperty(dateEntered);
        this.carbs = new SimpleStringProperty(carbs);
        this.fat = new SimpleStringProperty(fat);
    }

    //getmethods for retrieving data
    public String getMealName(){ return mealName.get(); }
    public String getCalories(){ return calories.get(); }
    public String getProtein(){ return protein.get(); }
    public String getCarbs(){return carbs.get(); }
    public String getDateEntered(){ return dateEntered.get(); }
    public String getFat() {return fat.get();}
}
