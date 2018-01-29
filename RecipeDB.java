package robert.deford.personalizedcookbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import static android.database.DatabaseUtils.dumpCursorToString;

/**
 * Created by Robert on 7/18/2017.
 */

public class RecipeDB extends SQLiteOpenHelper {

    //Database variables
    private static final String DATABASE_NAME = "RecipeDatabase";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_RECIPE = "Recipe";
    private static final String TABLE_INGREDIENT = "Ingredients";

    private static final String RECIPE_ID = "ID";
    private static final String RECIPE_NAME = "Name";
    private static final String RECIPE_INSTRUCTIONS = "Instructions";
    private static final String RECIPE_MEALTYPE = "MealType";

    private static final String INGREDIENT_ID = "IngredientID";
    private static final String INGREDIENT_NAME = "IngredientName";
    private static final String INGREDIENT_AMOUNT = "IngredientAmount";
    private static final String INGREDIENT_MEASUREMENT = "IngredientMeasurement";

    //SQLiteDatabase db = this.getWritableDatabase();


    //Database Constructor
    RecipeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Table Create Strings
    private static final String CREATE_TABLE_RECIPE = "CREATE TABLE " + TABLE_RECIPE + "(" + RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RECIPE_NAME + " VARCHAR (50)," + RECIPE_INSTRUCTIONS + " VARCHAR," + RECIPE_MEALTYPE + " VARCHAR (9)" + ")";

    private static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + TABLE_INGREDIENT + "(" + INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  + RECIPE_ID + " INTEGER, "
            + INGREDIENT_NAME + " VARCHAR (25)," + INGREDIENT_AMOUNT + " VARCHAR (10)," + INGREDIENT_MEASUREMENT + " VARCHAR (25)" +
            ")";

    //Create the Database Tables if not created already
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
    }

    //Upgrade Database Tables on Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //checks if the old database version is less than 2, and applies update
        if(oldVersion < 3)
        {
            db.execSQL(CREATE_TABLE_RECIPE);
            db.execSQL(CREATE_TABLE_INGREDIENTS);

            String breakfastRecipeQuery = "SELECT * FROM BreakfastTable";
            upgradeTables(breakfastRecipeQuery, "Breakfast", db);

            String lunchRecipeQuery = "SELECT * FROM LunchTable";
            upgradeTables(lunchRecipeQuery, "Lunch", db);

            String dinnerRecipeQuery = "SELECT * FROM DinnerTable";
            upgradeTables(dinnerRecipeQuery, "Dinner", db);

            String dessertRecipeQuery = "SELECT * FROM DessertTable";
            upgradeTables(dessertRecipeQuery, "Dessert", db);

            String snacksRecipeQuery = "SELECT * FROM SnacksTable";
            upgradeTables(snacksRecipeQuery, "Snacks", db);

            String drinksRecipeQuery = "SELECT * FROM DrinksTable";
            upgradeTables(drinksRecipeQuery, "Drinks", db);
        }
    }

    //Add a new recipe to the Database Table, based on which Meal Type the recipe is for
    public void addRecipe(RecipeClass recipeClass, List<IngredientClass> ingredientClasses, SQLiteDatabase db)
    {
        ContentValues recipeValues = new ContentValues();

        recipeValues.put(RECIPE_NAME, recipeClass.getRecipeName());
        recipeValues.put(RECIPE_INSTRUCTIONS, recipeClass.getRecipeInstructions());
        recipeValues.put(RECIPE_MEALTYPE, recipeClass.getRecipeMealType());
        db.insert(TABLE_RECIPE, null, recipeValues);

        String selectQuery = "SELECT " + RECIPE_ID + " FROM " + TABLE_RECIPE + " WHERE " + RECIPE_NAME + " = \'" + recipeClass.getRecipeName() + "\'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Integer idNum = null;

        if(cursor.moveToFirst())
        {
            do
            {
                idNum = cursor.getInt(0);
            }
            while(cursor.moveToNext());
        }

        IngredientClass ingredientClass = new IngredientClass();

        for(int i = 0; i < ingredientClasses.size(); i++)
        {
            ingredientClass = ingredientClasses.get(i);

            ContentValues ingredientValues = new ContentValues();

            ingredientValues.put(RECIPE_ID, idNum);
            ingredientValues.put(INGREDIENT_NAME, ingredientClass.getIngredientName());
            ingredientValues.put(INGREDIENT_AMOUNT, ingredientClass.getIngredientAmount());
            ingredientValues.put(INGREDIENT_MEASUREMENT, ingredientClass.getIngredientMeasurement());

            db.insert(TABLE_INGREDIENT, null, ingredientValues);
        }
    }

    //Get listing of the Recipe Names to show in a List View for user to Select Recipe
    public List<String> getAllRecipes(String mealType, SQLiteDatabase db)
    {
        List<String> getName = new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPE + " WHERE " + RECIPE_MEALTYPE + " = \'" + mealType + "\'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                RecipeClass recipeClass = new RecipeClass();
                recipeClass.setRecipeName(cursor.getString(1));
                getName.add(recipeClass.getRecipeName());
            }
            while(cursor.moveToNext());
        }

        return getName;
    }

    //Get Recipe to display to user based on the recipe that the user selected in the List View
    public RecipeClass getRecipe(String recipeName, String mealType, SQLiteDatabase db)
    {

        RecipeClass recipeClass = new RecipeClass();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPE + " WHERE " + RECIPE_NAME + " = \'" + recipeName + "\' AND " + RECIPE_MEALTYPE + " = \'" + mealType + "\'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do {
                recipeClass.setRecipeID(cursor.getInt(0));
                recipeClass.setRecipeName(recipeName);
                recipeClass.setRecipeInstructions(cursor.getString(2));
                recipeClass.setRecipeMealType(cursor.getString(3));
            }
            while(cursor.moveToNext());
        }

        return recipeClass;
    }

    //gets listing of ingredients for the recipe to display to the user
    public List<IngredientClass> getIngredients(int RecipeID, SQLiteDatabase db)
    {
        List<IngredientClass> ingredientClasses = new ArrayList<IngredientClass>();
        String selectQuery = "SELECT * FROM " + TABLE_INGREDIENT + " WHERE " + RECIPE_ID + " = " + RecipeID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do {
                IngredientClass ingredientClass = new IngredientClass();
                ingredientClass.setRecipeID(cursor.getInt(1));
                ingredientClass.setIngredientName(cursor.getString(2));
                ingredientClass.setIngredientAmount(cursor.getString(3));
                ingredientClass.setIngredientMeasurement(cursor.getString(4));
                ingredientClasses.add(ingredientClass);
            }
            while(cursor.moveToNext());
        }

        return ingredientClasses;
    }

    //updates recipe based on edit changes applied by the user
    public int updateRecipe(RecipeClass recipeClass, List<IngredientClass> ingredientClasses, List<IngredientClass> ingredientClassesNew, String oldIngred, SQLiteDatabase db)
    {
        ContentValues recipeValues = new ContentValues();

        recipeValues.put(RECIPE_NAME, recipeClass.getRecipeName());
        recipeValues.put(RECIPE_INSTRUCTIONS, recipeClass.getRecipeInstructions());
        recipeValues.put(RECIPE_MEALTYPE, recipeClass.getRecipeMealType());

        db.update(TABLE_RECIPE, recipeValues, RECIPE_ID + " =?", new String[]{String.valueOf(recipeClass.getRecipeID())});

        for(int i = 0; i < ingredientClasses.size(); i++)
        {
            IngredientClass ingredientClass = new IngredientClass();
            ingredientClass = ingredientClasses.get(i);

            ContentValues ingredientValues = new ContentValues();

            ingredientValues.put(RECIPE_ID, recipeClass.getRecipeID());
            ingredientValues.put(INGREDIENT_NAME, ingredientClass.getIngredientName());
            ingredientValues.put(INGREDIENT_AMOUNT, ingredientClass.getIngredientAmount());
            ingredientValues.put(INGREDIENT_MEASUREMENT, ingredientClass.getIngredientMeasurement());

            db.update(TABLE_INGREDIENT, ingredientValues, RECIPE_ID + " =? AND " + INGREDIENT_NAME + " =?", new String[]{String.valueOf(recipeClass.getRecipeID()), oldIngred});
        }

        for(int i = 0; i < ingredientClassesNew.size(); i++)
        {
            IngredientClass ingredientClassNew = new IngredientClass();
            ingredientClassNew = ingredientClassesNew.get(i);

            ContentValues ingredientValuesNew = new ContentValues();

            ingredientValuesNew.put(RECIPE_ID, recipeClass.getRecipeID());
            ingredientValuesNew.put(INGREDIENT_NAME, ingredientClassNew.getIngredientName());
            ingredientValuesNew.put(INGREDIENT_AMOUNT, ingredientClassNew.getIngredientAmount());
            ingredientValuesNew.put(INGREDIENT_MEASUREMENT, ingredientClassNew.getIngredientMeasurement());

            db.insert(TABLE_INGREDIENT, null, ingredientValuesNew);
        }

        return 0;
    }

    //deletes a recipe from the database
    public void deleteRecipe(RecipeClass recipeClass, SQLiteDatabase db)
    {

        db.delete(TABLE_RECIPE, RECIPE_ID + " =?", new String[]{String.valueOf(recipeClass.getRecipeID())});
        db.delete(TABLE_INGREDIENT, RECIPE_ID + " =?", new String[]{String.valueOf(recipeClass.getRecipeID())});

    }

    //moves a recipe to a new Meal Type that the user selects
    public void moveRecipe(RecipeClass recipeClass, String targetMeal, SQLiteDatabase db)
    {
        ContentValues moveValues = new ContentValues();

        moveValues.put(RECIPE_MEALTYPE, targetMeal);

        db.update(TABLE_RECIPE, moveValues, RECIPE_ID + " =?", new String[]{String.valueOf(recipeClass.getRecipeID())});

    }

    //transfers data from Version 1 Database to Version 2
    private void upgradeTables(String query, String mealType, SQLiteDatabase db)
    {
        List<IngredientClass> ingredientClasses = new ArrayList<>();
        String ingredientsString = null;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            RecipeClass recipeClass = new RecipeClass();
            do {
                recipeClass.setRecipeID(cursor.getInt(0));
                recipeClass.setRecipeName(cursor.getString(1));
                ingredientsString = cursor.getString(2);
                recipeClass.setRecipeInstructions(cursor.getString(3));
                recipeClass.setRecipeMealType(mealType);

                try {
                    String[] fullIngredients;

                    ingredientsString = ingredientsString.replace("[", "");
                    ingredientsString = ingredientsString.replace("]", "");
                    ingredientsString = ingredientsString.replace(",, ", ",");
                    fullIngredients = ingredientsString.split(",");


                    for (int i = 0; i < fullIngredients.length; i++) {
                        IngredientClass ingredientClass = new IngredientClass();
                        String[] brokenIngredient = fullIngredients[i].split(" ");
                        int ingIndex = brokenIngredient.length;
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int j = 2; j < ingIndex; j++) {
                            stringBuffer.append(brokenIngredient[j] + " ");
                        }
                        ingredientClass.setIngredientAmount(brokenIngredient[0]);
                        ingredientClass.setIngredientMeasurement(brokenIngredient[1]);
                        ingredientClass.setIngredientName(String.valueOf(stringBuffer));
                        ingredientClasses.add(ingredientClass);
                    }
                }
                catch (Exception e)
                {

                }

                addRecipe(recipeClass, ingredientClasses, db);
            }
            while(cursor.moveToNext());
        }


    }
}
