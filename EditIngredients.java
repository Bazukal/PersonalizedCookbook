package robert.deford.personalizedcookbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditIngredients extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener {

    TextView title;

    ListView ingredList;

    EditText quantity;
    EditText ingredName;
    String oldIngredName;

    Spinner measurement;

    Button cancel;
    Button saveIngred;
    Button saveRecipe;

    String recipeName;
    String mealType;
    String newName;
    String newInstructions;
    String newIngredMeasurement;
    List<IngredientClass> ingredientClasses = new ArrayList<IngredientClass>();
    List<IngredientClass> ingredientClassesNew = new ArrayList<IngredientClass>();
    List<IngredientClass> spinnerClasses = new ArrayList<IngredientClass>();
    List<String> ingredientNames = new ArrayList<String>();

    int ingredPos = -1;

    ArrayAdapter<String> arrayAdapter;

    RecipeDB recipeDB = new RecipeDB(this);

    RecipeClass recipeClass = new RecipeClass();
    IngredientClass ingredientClass = new IngredientClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredients);

        title = (TextView) findViewById(R.id.txtTitle);

        ingredList = (ListView) findViewById(R.id.listIngredients);
        ingredList.setOnItemClickListener(this);

        quantity = (EditText) findViewById(R.id.editQuantity);
        ingredName = (EditText) findViewById(R.id.editIngredName);

        measurement = (Spinner) findViewById(R.id.spinMeasure);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerMeasurement, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        measurement.setAdapter(spinnerAdapter);

        cancel = (Button) findViewById(R.id.btnCancel);
        cancel.setOnClickListener(this);
        saveIngred = (Button) findViewById(R.id.btnSaveIngred);
        saveIngred.setOnClickListener(this);
        saveRecipe = (Button) findViewById(R.id.btnSave);
        saveRecipe.setOnClickListener(this);

        recipeName = getIntent().getStringExtra("RECIPE");
        mealType = getIntent().getStringExtra("MEAL");
        newName = getIntent().getStringExtra("NEWNAME");
        newInstructions = getIntent().getStringExtra("NEWINSTRUCT");

        title.setText(newName);

        recipeClass = recipeDB.getRecipe(recipeName, mealType, recipeDB.getWritableDatabase());

        ingredientClasses = recipeDB.getIngredients(recipeClass.getRecipeID(), recipeDB.getWritableDatabase());

        for(int i = 0; i < ingredientClasses.size(); i++)
        {
            ingredientNames.add(ingredientClasses.get(i).getIngredientName());
            spinnerClasses.add(ingredientClasses.get(i));
        }

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
        ingredList.setAdapter(arrayAdapter);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_edit_ingredients);
        relativeLayout.setOnTouchListener(this);

        ingredList.requestFocus();
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch(v.getId())
        {
            case R.id.btnCancel:
                //cancels editing of recipe
                intent = new Intent(this, ViewWholeRecipe.class);
                intent.putExtra("RECIPE", recipeName);
                intent.putExtra("MEAL", mealType);
                startActivity(intent);
                break;
            case R.id.btnSaveIngred:
                //saves single ingredient for recipe editing
                String ingredQuantity = quantity.getText().toString();
                String ingredientName = ingredName.getText().toString();
                String ingredMeas = measurement.getSelectedItem().toString();

                Toast toast;

                if(ingredPos == -1)
                {
                    ingredientClass.setIngredientAmount(ingredQuantity);
                    ingredientClass.setIngredientMeasurement(ingredMeas);
                    ingredientClass.setIngredientName(ingredientName);

                    ingredientClassesNew.add(ingredientClass);
                    spinnerClasses.add(ingredientClass);
                    ingredientNames.add(ingredientClass.getIngredientName());

                    toast = Toast.makeText(this, "New Ingredient Saved", Toast.LENGTH_SHORT);
                }
                else
                {
                    ingredientClass.setIngredientAmount(ingredQuantity);
                    ingredientClass.setIngredientMeasurement(ingredMeas);
                    ingredientClass.setIngredientName(ingredientName);

                    ingredientClasses.set(ingredPos, ingredientClass);

                    for(int i = 0;i < ingredientNames.size(); i++)
                    {
                        if(ingredientNames.get(i).equals(oldIngredName))
                        {
                            ingredientNames.set(i, ingredientName);
                            spinnerClasses.get(ingredPos).setIngredientName(ingredientName);
                            spinnerClasses.get(ingredPos).setIngredientAmount(ingredQuantity);
                            spinnerClasses.get(ingredPos).setIngredientMeasurement(ingredMeas);
                        }
                    }

                    toast = Toast.makeText(this, "Ingredient Updated", Toast.LENGTH_SHORT);
                }

                ingredPos = -1;
                quantity.setText("");
                ingredName.setText("");
                measurement.setSelection(0);
                ingredList.requestFocus();
                updateList();
                toast.show();
                break;
            case R.id.btnSave:
                //saves the edits made to the database
                recipeClass.setRecipeName(newName);
                recipeClass.setRecipeInstructions(newInstructions);

                recipeDB.updateRecipe(recipeClass, ingredientClasses, ingredientClassesNew, oldIngredName, recipeDB.getWritableDatabase());

                Toast toast1 = Toast.makeText(this, "Recipe Saved", Toast.LENGTH_LONG);
                toast1.show();

                Intent intent1 = new Intent(this, ViewWholeRecipe.class);
                intent1.putExtra("RECIPE", newName);
                intent1.putExtra("MEAL", mealType);
                startActivity(intent1);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ingredPos = position;

        String ingredMeas = spinnerClasses.get(ingredPos).getIngredientMeasurement();
        quantity.setText(spinnerClasses.get(ingredPos).getIngredientAmount());
        ingredName.setText(spinnerClasses.get(ingredPos).getIngredientName());
        oldIngredName = ingredName.getText().toString();

        //finds the position of the measurement for the recipe in the spinner, and sets the position of the spinner to that measurement
        if (!ingredMeas.equals(null)) {
            String[] spinnerItems = getResources().getStringArray(R.array.spinnerMeasurement);
            int counter = 0;

            for(int i = 0;i < spinnerItems.length; i++)
            {
                if(spinnerItems[i].equals(ingredMeas))
                {
                    counter = i;
                }
            }
            measurement.setSelection(counter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newIngredMeasurement = String.valueOf(parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    //updates list of ingredients when a new ingredient is added
    private void updateList()
    {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
        ingredList.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
    }
}
