package org.akvo.rsr.android;

import org.akvo.rsr.android.dao.RsrDbAdapter;
import org.akvo.rsr.android.domain.Project;
import org.akvo.rsr.android.util.ConstantUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ProjectDetailActivity extends Activity {

	private String projId = null;
	private TextView projTitleLabel;
	private TextView projLocationText;
	private TextView projSummaryText;

	private RsrDbAdapter dba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//find which project we will be showing
		Bundle extras = getIntent().getExtras();
		projId = extras != null ? extras.getString(ConstantUtil.PROJECT_ID_KEY)
				: null;
		if (projId == null) {
			projId = savedInstanceState != null ? savedInstanceState
					.getString(ConstantUtil.PROJECT_ID_KEY) : null;
		}

		//get the look
		setContentView(R.layout.activity_project_detail);
		//find the fields
		projTitleLabel = (TextView) findViewById(R.id.text_proj_detail_title);
		projLocationText = (TextView) findViewById(R.id.text_proj_location);

		dba = new RsrDbAdapter(this);
		
		// Show the Up button in the action bar.
		//		setupActionBar();
	}

	@Override
	protected void onResume() {
		super.onResume();
		dba.open();
		Project project = dba.findProject(projId);
		
		projTitleLabel.setText(project.getTitle());
		projLocationText.setText(project.getLocation());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		dba.close();
	}
	

	@Override
	protected void onDestroy() {
		if (dba != null) {
			dba.close();
		}
		super.onDestroy();
	}

	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 *
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_detail, menu);
		return true;
	}


}
