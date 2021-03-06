/*
 *  Copyright (C) 2012-2015 Stichting Akvo (Akvo Foundation)
 *
 *  This file is part of Akvo RSR.
 *
 *  Akvo RSR is free software: you can redistribute it and modify it under the terms of
 *  the GNU Affero General Public License (AGPL) as published by the Free Software Foundation,
 *  either version 3 of the License or any later version.
 *
 *  Akvo RSR is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Affero General Public License included with this program for more details.
 *
 *  The full license text can also be seen at <http://www.gnu.org/licenses/agpl.html>.
 */

package org.akvo.rsr.up;

import java.util.List;

import org.akvo.rsr.up.R;
import org.akvo.rsr.up.dao.RsrDbAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.database.Cursor;

public class DiagnosticActivity extends ActionBarActivity {

	private TextView mTextView;
	private Button mBtnUpdates;
	private Button mBtnClearDb;

	private RsrDbAdapter mDb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//get the look
		setContentView(R.layout.activity_diagnostic);
		//find the fields
		mTextView = (TextView) findViewById(R.id.text_field);
		//Activate buttons
/*
		mBtnUpdates = (Button) findViewById(R.id.btn_diag_a);
		mBtnUpdates.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {//delete image cache files
				clearCache(DiagnosticActivity.this);
			}
		});
		mBtnClearDb = (Button) findViewById(R.id.btn_diag_b);
		mBtnClearDb.setOnClickListener( new View.OnClickListener() {
			public void onClick(View view) {
				clearData();
			}
		});
 */
		mDb = new RsrDbAdapter(this);
		mDb.open();
	}

	
	@Override
	protected void onResume() {
		super.onResume();

        Cursor users = mDb.listAllUsers();
        mTextView.append("\n\nUsers in db: " + String.valueOf(users.getCount()));
        while (users.moveToNext())
            mTextView.append("\n[" + users.getString(users.getColumnIndex(RsrDbAdapter.PK_ID_COL)) +
                             "] "  + users.getString(users.getColumnIndex(RsrDbAdapter.FIRST_NAME_COL)) +
                             " "   + users.getString(users.getColumnIndex(RsrDbAdapter.LAST_NAME_COL)));
        users.close();

        List<String>u = mDb.getMissingUsersList();
        mTextView.append("\n\nMissing users in db: " + String.valueOf(u.size()));
        for (String id:u) {
            mTextView.append("\n["+id+"] ");
        }

        Cursor orgs = mDb.listAllOrgs();
        mTextView.append("\n\nOrgs in db: " + String.valueOf(orgs.getCount()));
        while (orgs.moveToNext())
            mTextView.append("\n["+orgs.getString(orgs.getColumnIndex(RsrDbAdapter.PK_ID_COL))+"] "+orgs.getString(orgs.getColumnIndex(RsrDbAdapter.NAME_COL))+" ");
        orgs.close();

        List<String>o = mDb.getMissingOrgsList();
        mTextView.append("\n\nMissing orgs in db: " + String.valueOf(o.size()));
        for (String id:o) {
            mTextView.append("\n["+id+"] ");
        }
        Cursor b = mDb.listAllProjects();
        mTextView.append("\n\nProjects in db: " + String.valueOf(b.getCount()));
//        while (b.moveToNext())
//            mTextView.append("\n["+b.getString(b.getColumnIndex(RsrDbAdapter.PK_ID_COL))+"] " + b.getInt(b.getColumnIndex(RsrDbAdapter.LAST_FETCH_COL))+" ");
        b.close();
        Cursor b2 = mDb.listVisibleProjects();
        mTextView.append("\n\nVisible projects in db: " + String.valueOf(b2.getCount()));
        b2.close();
        Cursor c = mDb.listAllUpdates();
        mTextView.append("\n\nUpdates in db: " + String.valueOf(c.getCount()));
        c.close();
        Cursor a = mDb.listAllCountries();
        mTextView.append("\n\nCountries in db: " + String.valueOf(a.getCount()));
        while (a.moveToNext())
            mTextView.append("\n[" + a.getString(a.getColumnIndex(RsrDbAdapter.PK_ID_COL))+"] " + a.getString(a.getColumnIndex(RsrDbAdapter.NAME_COL)) + " ");
        a.close();

        mTextView.append("\n\nResults in db: " + String.valueOf(mDb.countResults()));

        mTextView.append("\n\nIndicators in db: " + String.valueOf(mDb.countIndicators()));

        mTextView.append("\n\nPeriods in db: " + String.valueOf(mDb.countPeriods()));

//		Cursor d = mDb.listAllUpdatesFor("609");
//		mTextView.append("\nUpdates in db for 609: " + String.valueOf(d.getCount())+"\n");
//		d.close();
		
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdAvailSize = (double)stat.getAvailableBlocks()
		                   * (double)stat.getBlockSize();
		//One binary gigabyte equals 1,073,741,824 bytes.
		double gigaAvailable = sdAvailSize / 1073741824;
        mTextView.append("\n\n" + gigaAvailable + " GiB free on card\n");
	}
		

	@Override
	protected void onDestroy() {
		if (mDb != null) {
			mDb.close();
		}
		super.onDestroy();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.diagnostics, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
/*
        case R.id.action_clear_diagnostics:
            return true;
        case R.id.action_settings:
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
            */
        default:
            return super.onOptionsItemSelected(item);
        }

    }

}
