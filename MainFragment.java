package robert.deford.personalizedcookbook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Robert on 7/17/2017.
 */

public class MainFragment extends Fragment {

    mainButtonListener whichButton;

    public interface mainButtonListener
    {
        public void passButtonPressed(String button);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            whichButton = (mainButtonListener) activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString());
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        final Button viewRecipe = (Button) view.findViewById(R.id.btnView);
        viewRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });
        final Button addRecipe = (Button) view.findViewById(R.id.btnAdd);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(v);
            }
        });

        return view;
    }

    public void buttonClicked(View view)
    {
        mainButtonListener.passButtonPressed();
    }
}
