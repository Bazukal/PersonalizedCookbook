package robert.deford.personalizedcookbook;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnCreateContextMenuListener {

    Button back;
    Button main;
    ListView recipeList;
    TextView title;

    String mealType;
    String name;
    List<String> recipeNames = new ArrayList<String>();

    ArrayAdapter<String> arrayAdapter;

    RecipeDB recipeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe_list);

        title = (TextView) findViewById(R.id.txtTitle);

        recipeDB = new RecipeDB(this);

        back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(this);
        main = (Button) findViewById(R.id.btnMainMenu);
        main.setOnClickListener(this);

        mealType = getIntent().getStringExtra("MEAL");

        switch (mealType)
        {
            case "Breakfast":
                title.setText("Breakfast Recipes");
                break;
            case "Lunch":
                title.setText("Lunch Recipes");
                break;
            case "Dinner":
                title.setText("Dinner Recipes");
                break;
            case "Dessert":
                title.setText("Dessert Recipes");
                break;
            case "Snacks":
                title.setText("Snack Recipes");
                break;
            case "Drinks":
                title.setText("Drink Recipes");
                break;
        }

        recipeNames = recipeDB.getAllRecipes(mealType, recipeDB.getWritableDatabase());

        recipeList = (ListView) findViewById(R.id.listRecipe);
        registerForContextMenu(recipeList);
        recipeList.setOnItemClickListener(this);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeNames);
        recipeList.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        //changes activity app is displaying
        switch(v.getId())
        {
            case R.id.btnBack:
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
            case R.id.btnMainMenu:
                intent = new Intent(this, MainMenu.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    //views entire recipe for the recipe that the user selects
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String recipeName = parent.getItemAtPosition(position).toString();
        Intent intent = new Intent(this, ViewWholeRecipe.class);
        intent.putExtra("RECIPE", recipeName);
        intent.putExtra("MEAL", mealType);
        startActivity(intent);
    }

    //creates context menu when user holds down a recipe name
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (v.getId() == R.id.listRecipe)
        {
            recipeList = (ListView) v;
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            name = recipeList.getItemAtPosition(adapterContextMenuInfo.position).toString();
            menu.add(name);

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.list_view_context_menu, menu);
        }
    }

    //deletes or moves a recipe based on user selection from the context menu
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        RecipeDB recipeDB = new RecipeDB(this);
        RecipeClass recipeClass = recipeDB.getRecipe(name, mealType, recipeDB.getWritableDatabase());
        Toast toast;

        switch (menuItem.getItemId())
        {
            case R.id.deleteRecipe:
                recipeDB.deleteRecipe(recipeClass, recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " Deleted.", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveBreakfast:
                recipeDB.moveRecipe(recipeClass, "Breakfast", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Breakfast", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveLunch:
                recipeDB.moveRecipe(recipeClass, "Lunch", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Lunch", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveDinner:
                recipeDB.moveRecipe(recipeClass, "Dinner", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Dinner", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveDessert:
                recipeDB.moveRecipe(recipeClass, "Dessert", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Dessert", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveSnacks:
                recipeDB.moveRecipe(recipeClass, "Snacks", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Snacks", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.moveDrinks:
                recipeDB.moveRecipe(recipeClass, "Drinks", recipeDB.getWritableDatabase());

                updateList();

                toast = Toast.makeText(this, name + " moved to Drinks", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    //reloads recipe list
    private void updateList()
    {
        recipeNames = recipeDB.getAllRecipes(mealType, recipeDB.getWritableDatabase());
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeNames);
        recipeList.setAdapter(arrayAdapter);
    }
}
