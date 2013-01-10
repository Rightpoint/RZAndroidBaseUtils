package com.raizlabs.baseutils.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.raizlabs.baseutils.examples.viewgroupadapter.ViewGroupAdapterExampleActivity;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.main_button_viewGroupAdapter).setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, ViewGroupAdapterExampleActivity.class));
			}
		});
    }
}
