package robert.deford.personalizedcookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    Button breakfast, lunch, dinner, dessert, snacks, drinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        breakfast = (Button) findViewById(R.id.btnBreakfast);
        breakfast.setOnClickListener(this);
        lunch = (Button) findViewById(R.id.btnLunch);
        lunch.setOnClickListener(this);
        dinner = (Button) findViewById(R.id.btnDinner);
        dinner.setOnClickListener(this);
        dessert = (Button) findViewById(R.id.btnDessert);
        dessert.setOnClickListener(this);
        snacks = (Button) findViewById(R.id.btnSnacks);
        snacks.setOnClickListener(this);
        drinks = (Button) findViewById(R.id.btnDrinks);
        drinks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId())
        {
            case R.id.btnBreakfast:
                intent = new Intent(this, BreakfastMenu.class);
                startActivity(intent);
                break;
            case R.id.btnLunch:
                intent = new Intent(this, LunchMenu.class);
                startActivity(intent);
                break;
            case R.id.btnDinner:
                intent = new Intent(this, DinnerMenu.class);
                startActivity(intent);
                break;
            case R.id.btnDessert:
                intent = new Intent(this, DessertMenu.class);
                startActivity(intent);
                break;
            case R.id.btnSnacks:
                intent = new Intent(this, SnacksMenu.class);
                startActivity(intent);
                break;
            case R.id.btnDrinks:
                intent = new Intent(this, DrinksMenu.class);
                startActivity(intent);
                break;
        }

    }
}
