package robert.deford.personalizedcookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SnacksMenu extends AppCompatActivity implements View.OnClickListener{

    ImageView title;
    Button viewRecipe, addRecipe, mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks_menu);

        title = (ImageView) findViewById(R.id.imgTitle);
        title.setBackgroundResource(R.drawable.snackstitle);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById (R.id.meal_menu_fragment);
        relativeLayout.setBackgroundResource(R.drawable.snackpic);

        viewRecipe = (Button) findViewById(R.id.btnView);
        viewRecipe.setOnClickListener(this);
        addRecipe = (Button) findViewById(R.id.btnAdd);
        addRecipe.setOnClickListener(this);
        mainMenu = (Button) findViewById(R.id.btnMainMenu);
        mainMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId())
        {
            case R.id.btnView:
                intent = new Intent(this, ViewRecipeList.class);
                intent.putExtra("MEAL", "Snacks");
                startActivity(intent);
                break;
            case R.id.btnAdd:
                intent = new Intent(this, AddRecipe.class);
                intent.putExtra("MEAL", "Snacks");
                startActivity(intent);
                break;
            case R.id.btnMainMenu:
                intent = new Intent(this, MainMenu.class);
                startActivity(intent);
                break;
        }
    }
}
