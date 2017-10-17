package io.julian.imitatecoolui.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import io.julian.imitate.jikelike.widget.FavorView;
import io.julian.imitate.jikelike.widget.PopTextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FavorView mFavorView;
    private PopTextView mPopTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFavorView = (FavorView) findViewById(R.id.favorView);
        mFavorView.setOnFavorViewClickListener(new FavorView.OnFavorViewClickListener() {
            @Override
            public void onFavorClick(boolean checked) {
                Log.d(TAG, "onFavorClick: " + checked);
            }
        });

        mPopTextView = (PopTextView) findViewById(R.id.popTextView);
        mPopTextView.setOnPopTextViewClickListener(new PopTextView.OnPopTextViewClickListener() {
            @Override
            public void onPopTextClick(int beforeNum, int afterNum) {
                Log.d(TAG, "onPopTextClick: beforeNum: " + beforeNum + ", afterNum: " + afterNum);
            }
        });
    }
}
