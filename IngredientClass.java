package robert.deford.personalizedcookbook;

/**
 * Created by Robert on 10/17/2017.
 */

public class IngredientClass {

    private int recipeID;
    private String ingredientName;
    private String ingredientAmount;
    private String ingredientMeasurement;

    public IngredientClass() {}

    public IngredientClass (int recipe_ID, String ingredient_Name, String ingredient_Amount, String ingredient_Measurement)
    {
        recipeID = recipe_ID;
        ingredientName = ingredient_Name;
        ingredientAmount = ingredient_Amount;
        ingredientMeasurement = ingredient_Measurement;
    }

    public int getRecipeID() {return recipeID;}
    public void setRecipeID (int recipe_ID) {recipeID = recipe_ID;}

    public String getIngredientName() {return ingredientName;}
    public void setIngredientName (String ingredient_Name) {ingredientName = ingredient_Name;}

    public String getIngredientAmount() {return ingredientAmount;}
    public void setIngredientAmount(String ingredient_Amount) {ingredientAmount = ingredient_Amount;}

    public String getIngredientMeasurement() {return ingredientMeasurement;}
    public void setIngredientMeasurement(String ingredient_Measurement) {ingredientMeasurement = ingredient_Measurement;}
}
