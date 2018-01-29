package robert.deford.personalizedcookbook;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Robert on 7/18/2017.
 */

public class RecipeClass{

    //Variables
    private int recipeID;
    private String recipeName;
    private String recipeInstructions;
    private String recipeMealType;

    public RecipeClass() {

    }

    public RecipeClass(String recipeNameString, String recipeInstructString, String recipeMealTypeString) {
        recipeName = recipeNameString;
        recipeInstructions = recipeInstructString;
        recipeMealType = recipeMealTypeString;
    }

    //Setters and Getters
    public int getRecipeID(){return recipeID;}
    public void setRecipeID(int Id){recipeID = Id;}

    public String getRecipeName(){return recipeName;}
    public void setRecipeName(String name){recipeName = name;}

    public String getRecipeInstructions(){return recipeInstructions;}
    public void setRecipeInstructions(String instructions){recipeInstructions = instructions;}

    public String getRecipeMealType() {return recipeMealType;}
    public void setRecipeMealType(String mealType) {recipeMealType = mealType;}
}