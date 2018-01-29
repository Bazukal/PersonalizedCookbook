package robert.deford.personalizedcookbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditRecipe extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener{

    EditText name;
    EditText instructions;

    Button cancel;
    Button save;

    String recipeName;
    String mealType;
    String instructionString;

    RecipeDB recipeDB = new RecipeDB(this);
    RecipeClass recipeClass = new RecipeClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        name = (EditText) findViewById(R.id.editName);
        instructions = (EditText) findViewById(R.id.editInstructions);

        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(this);

        recipeName = getIntent().getStringExtra("RECIPE");
        mealType = getIntent().getStringExtra("MEAL");

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_edit_recipe);
        relativeLayout.setOnTouchListener(this);

        recipeClass = recipeDB.getRecipe(recipeName, mealType, recipeDB.getWritableDatabase());
        instructionString = recipeClass.getRecipeInstructions();

        name.setText(recipeName);
        instructions.setText(instructionString);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId())
        {
            case R.id.btnCancel:
                //cancels editing of recipe
                intent = new Intent(this, ViewWholeRecipe.class);
                intent.putExtra("RECIPE", recipeName);
                intent.putExtra("MEAL", mealType);
                startActivity(intent);
                break;
            case R.id.btnSave:
                //saves recipe name and instructions and carries the information over to the ingredient edit for future saving to the database
                intent = new Intent(this, EditIngredients.class);
                intent.putExtra("RECIPE", recipeName);
                intent.putExtra("MEAL", mealType);
                intent.putExtra("NEWNAME", name.getText().toString());
                intent.putExtra("NEWINSTRUCT", instructions.getText().toString());
                startActivity(intent);
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
    }
}
