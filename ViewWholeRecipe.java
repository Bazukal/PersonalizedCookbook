package robert.deford.personalizedcookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewWholeRecipe extends AppCompatActivity implements View.OnClickListener{

    String mealType;
    String recipeName;
    List<IngredientClass> ingredientClasses = new ArrayList<IngredientClass>();

    RecipeClass recipeClass = new RecipeClass();

    RecipeDB recipeDB = new RecipeDB(this);

    Button edit;
    Button back;
    Button main;

    TextView recipeTitle;
    TextView recipeInstruct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_whole_recipe);

        recipeName = getIntent().getStringExtra("RECIPE");
        mealType = getIntent().getStringExtra("MEAL");

        edit = (Button) findViewById(R.id.btnEdit);
        edit.setOnClickListener(this);
        back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        main = (Button) findViewById(R.id.btnMain);
        main.setOnClickListener(this);

        recipeTitle = (TextView) findViewById(R.id.txtTitle);
        recipeInstruct = (TextView) findViewById(R.id.txtRecipe);

        recipeClass = recipeDB.getRecipe(recipeName, mealType, recipeDB.getWritableDatabase());

        recipeTitle.setText(recipeClass.getRecipeName());

        int recipeID = recipeClass.getRecipeID();

        ingredientClasses = recipeDB.getIngredients(recipeID, recipeDB.getWritableDatabase());

        recipeInstruct.setText("Ingredients:\n\n");

        for(int i = 0; i < ingredientClasses.size(); i++)
        {
            recipeInstruct.append(ingredientClasses.get(i).getIngredientAmount() + " " + ingredientClasses.get(i).getIngredientMeasurement() + " " +
            ingredientClasses.get(i).getIngredientName() + "\n");
        }

        //Takes the StringBuilder and displays the Ingredients within, as well as the Instructions for the recipe
        recipeInstruct.append("\n\nInstructions:\n\n" + recipeClass.getRecipeInstructions());
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId())
        {
            case R.id.btnEdit:
                //takes user to the edit recipe screen
                intent = new Intent(this, EditRecipe.class);
                intent.putExtra("RECIPE", recipeName);
                intent.putExtra("MEAL", mealType);
                startActivity(intent);
                break;
            case R.id.btnBack:
                //takes user back to the meal type menu
                switch (mealType)
                {
                    case "Breakfast":
                        intent = new Intent(this, BreakfastMenu.class);
                        startActivity(intent);
                        break;
                    case "Lunch":
                        intent = new Intent(this, LunchMenu.class);
                        startActivity(intent);
                        break;
                    case "Dinner":
                        intent = new Intent(this, DinnerMenu.class);
                        startActivity(intent);
                        break;
                    case "Dessert":
                        intent = new Intent(this, DessertMenu.class);
                        startActivity(intent);
                        break;
                    case "Snacks":
                        intent = new Intent(this, SnacksMenu.class);
                        startActivity(intent);
                        break;
                    case "Drinks":
                        intent = new Intent(this, DrinksMenu.class);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.btnMain:
                //takes user back to the main menu
                intent = new Intent(this, MainMenu.class);
                startActivity(intent);
                break;
        }

    }
}
