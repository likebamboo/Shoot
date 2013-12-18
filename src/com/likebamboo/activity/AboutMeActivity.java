
package com.likebamboo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.likebamboo.shoot.R;

/**
 * 关于作者
 * 
 * @author likebamboo
 */
public class AboutMeActivity extends Activity {

    /**
     * 返回按钮
     */
    private Button mBackBt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_me);

        ((TextView)findViewById(R.id.title_title_tv)).setText(R.string.about_me);
        mBackBt = (Button)findViewById(R.id.title_back_bt);
        
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
}
