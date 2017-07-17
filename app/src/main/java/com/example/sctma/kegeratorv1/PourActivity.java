package com.example.sctma.kegeratorv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.sctma.kegeratorv1.Util.kegInfo;

public class PourActivity extends AppCompatActivity {

    private int kegPos;
    private double poured;
    private double cost;
    private TextView pouredText;
    private TextView costText;
    private TextView beersLeftText;
    private double beersLeft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pour);
        kegPos = getIntent().getIntExtra("KegPos",0);
        poured = 0;
        cost = 0.0;
        pouredText = (TextView)findViewById(R.id.pouredText);
        costText = (TextView)findViewById(R.id.costText);
        beersLeftText = (TextView)findViewById(R.id.beersLeftText);
        beersLeft = kegInfo[kegPos].getBeersLeft();
        beersLeftText.setText("" + roundNumber(beersLeft));
        ((ImageView)findViewById(R.id.chosenKeg1Image)).setImageBitmap(Util.imageBitmaps[kegPos]);
        ((TextView)(findViewById(R.id.chosenStyle1Text))).setText(kegInfo[kegPos].getStyle());
        ((TextView)(findViewById(R.id.pricePerBeerText))).setText(kegInfo[kegPos].getRoundedPricePerBeer());
        ((TextView)(findViewById(R.id.pricePerOunceText))).setText(kegInfo[kegPos].getRoundedPricePerOunce());
        ((TextView)(findViewById(R.id.chosenName1Text))).setText(kegInfo[kegPos].getName());
        //addToPoured(12.1);
        updatePouredCostAndBeerLeftText();
    }
    public void addToPoured(double d)
    {
        poured += d;
        cost += d*kegInfo[kegPos].getPricePerOunce();
        beersLeft -= d/12.0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.mContext = getApplicationContext();
    }

    public void updatePouredCostAndBeerLeftText()
    {
        pouredText.setText("" + roundNumber(poured));
        costText.setText("" + roundNumber(cost));
        beersLeftText.setText("" + beersLeft);
    }
    public double roundNumber(double d)
    {
        return (Math.round(d*100.0)/100.0);
    }

}
