package com.raizlabs.baseutils.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.raizlabs.baseutils.examples.simplegenericadapter.SimpleGenericAdapterExampleActivity;
import com.raizlabs.baseutils.examples.viewgroupadapter.ViewGroupAdapterExampleActivity;
import com.raizlabs.baseutils.examples.viewholderstrategy.SimpleViewHolderStrategyExampleActivity;

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
        
        findViewById(R.id.main_button_simpleGenericAdapter).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SimpleGenericAdapterExampleActivity.class));
			}
		});
        
        findViewById(R.id.main_button_simpleViewHolderStrategy).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SimpleViewHolderStrategyExampleActivity.class));
			}
		});
    }
}
