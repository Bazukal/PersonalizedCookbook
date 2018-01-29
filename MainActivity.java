package robert.deford.personalizedcookbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private InterstitialAd mInterstitialAd;
    boolean failedLoad = false;

    Intent intent;

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout splashScreen = (RelativeLayout) findViewById(R.id.activity_main);
        splashScreen.setOnClickListener(this);

        spinner = (ProgressBar)findViewById(R.id.adProg);

        MobileAds.initialize(this, "ca-app-pub-1611268903138960~5520410254");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1611268903138960/2443299702");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        spinner.setVisibility(View.VISIBLE);

        mInterstitialAd.setAdListener(new AdListener()
        {
            public void onAdClosed()
            {
                startActivity(intent);
            }

            public void onAdFailedToLoad(int errorCode)
            {
                failedLoad = true;
                spinner.setVisibility(View.GONE);
            }

            public void onAdLoaded()
            {
                spinner.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this, MainMenu.class);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else if(failedLoad == true)
        {
            startActivity(intent);
        }
    }
}
