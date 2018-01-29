package robert.deford.personalizedcookbook;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AddRecipe extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener, View.OnCreateContextMenuListener {

    TextView title;
    String mealType;
    Spinner measurement;

    EditText recipeName;
    EditText ingredQuant;
    EditText ingredName;
    EditText recipeInstruct;

    String recipeNameString = "";
    String ingredQuantString = "";
    String ingredMeasString = "";
    String ingredNameString = "";
    String recipeInstructString = "";

    List<IngredientClass> ingredientClasses = new ArrayList<IngredientClass>();
    IngredientClass ingredientClass;
    RecipeClass recipeClass;

    Button newIngredient;
    Button cancel;
    Button save;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        title = (TextView) findViewById(R.id.txtTitle);

        mealType = getIntent().getStringExtra("MEAL");
        title.setText("Add " + mealType + " Recipe");

        measurement = (Spinner) findViewById(R.id.spinMeasure);
        measurement.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerMeasurement, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurement.setAdapter(spinnerAdapter);

        recipeName = (EditText) findViewById(R.id.editName);
        ingredQuant = (EditText) findViewById(R.id.editQuantity);
        ingredName = (EditText) findViewById(R.id.editIngredName);
        recipeInstruct = (EditText) findViewById(R.id.editInstructions);

        newIngredient = (Button) findViewById(R.id.btnNextIngred);
        newIngredient.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_add_recipe);
        relativeLayout.setOnTouchListener(this);

        registerForContextMenu(recipeInstruct);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //stores value of the ingredient measurement when selected in the drop down list
        ingredMeasString = String.valueOf(parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            //when the plus is pressed under the ingredients, saves the ingredients to array, and allows another ingredient to be entered
            case R.id.btnNextIngred:
                ingredQuantString = ingredQuant.getText().toString();
                ingredNameString = ingredName.getText().toString();
                ingredientClass = new IngredientClass();

                if (checkEmpty(ingredQuantString, ingredNameString))
                {
                    ingredientClass.setIngredientAmount(ingredQuantString);
                    ingredientClass.setIngredientMeasurement(ingredMeasString);
                    ingredientClass.setIngredientName(ingredNameString);

                    ingredientClasses.add(ingredientClass);

                    ingredQuant.setText("");
                    ingredName.setText("");
                    ingredQuant.requestFocus();
                }
                else
                {
                    Toast toast = Toast.makeText(this, "Fill All Ingredient Fields", Toast.LENGTH_LONG);
                    toast.show();
                }

                break;
            case R.id.btnCancel:

                //cancels the add recipe and returns to the menu of the meal type that was selected before hand
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

            //saves the recipe into the database
            case R.id.btnSave:
                ingredientClass = new IngredientClass();
                recipeNameString = recipeName.getText().toString();
                recipeInstructString = recipeInstruct.getText().toString();
                ingredQuantString = ingredQuant.getText().toString();
                ingredNameString = ingredName.getText().toString();

                if(checkNameInstruct(recipeNameString, recipeInstructString))
                {
                    if (checkEmpty(ingredQuantString, ingredNameString))
                    {
                        recipeClass = new RecipeClass(recipeNameString, recipeInstructString, mealType);

                        ingredientClass.setIngredientAmount(ingredQuantString);
                        ingredientClass.setIngredientMeasurement(ingredMeasString);
                        ingredientClass.setIngredientName(ingredNameString);

                        ingredientClasses.add(ingredientClass);

                        saveRecipe();
                    }
                    else if(ingredientClasses.size() == 0)
                    {
                            Toast toast = Toast.makeText(this, "Make sure to add Recipe Ingredients", Toast.LENGTH_LONG);
                            toast.show();
                    }
                    else
                    {
                        recipeClass = new RecipeClass(recipeNameString, recipeInstructString, mealType);
                        saveRecipe();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(this, "Recipe Needs both a Name and Instructions", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
        }
    }

    //verifies all ingredient fields have been filled out
    private boolean checkEmpty(String ingredQuantity, String ingredName)
    {
        boolean notEmpty = true;

        if(ingredQuantity.isEmpty() || ingredName.isEmpty())
        {
            notEmpty = false;
        }

        return notEmpty;
    }

    //verifies the recipe name and instructions have been filled out
    private boolean checkNameInstruct(String recipeName, String instructions)
    {
        boolean notEmpty = true;

        if(recipeName.isEmpty() || instructions.isEmpty())
        {
            notEmpty = false;
        }

        return notEmpty;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.paste, menu);
    }

    public boolean onContextItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.paste_text:
                //pastes info in clipboard to the instructions panel if user chooses option from context menu
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(this.CLIPBOARD_SERVICE);
                String pasteData = "";

                try {
                    ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                    pasteData = item.getText().toString();

                    recipeInstruct.getText().insert(recipeInstruct.getSelectionStart(), pasteData);
                }
                catch(Exception e)
                {
                    Toast toast = Toast.makeText(this, "No Data Found in Clipboard", Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
            default:
                return super.onContextItemSelected(menuItem);
        }
    }

    //saves recipe and goes back to meal type activity based on the type the user was in when adding recipe
    private void saveRecipe()
    {
        RecipeDB recipeDB = new RecipeDB(this);
        recipeDB.addRecipe(recipeClass, ingredientClasses, recipeDB.getWritableDatabase());

        Toast toast = Toast.makeText(this, "Recipe Saved", Toast.LENGTH_LONG);
        toast.show();

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
    }
}
