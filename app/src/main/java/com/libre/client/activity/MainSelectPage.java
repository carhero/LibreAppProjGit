package com.libre.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.libre.client.activity.listview.MainSelectListSeg;

import java.util.ArrayList;

public class MainSelectPage extends BaseActivity {

	private ListView listView;
	private ArrayList<MainSelectListSeg> arrayList;
	//ListView list;
	String[] Number = {
			" 1 ",
			" 2 ",
			" 3 ",
	} ;

	String[] Title = {
			"Choose a mieda source",
			"Choose a player",
			"Browse and play media",
	} ;

	String[] Source = {
			"Waiting for ",
			"Waiting for ",
			"There are your songs, photos and videos",
	} ;

	Integer[] imageId = {
			R.drawable.ic_pause_black_24dp,
			R.drawable.ic_pause_circle_fill_black_36dp,
			R.drawable.ic_play_orange_36dp,
	};

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		getSupportActionBar().hide();
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_select_page);

		MainSelectListSeg adapter = new MainSelectListSeg(MainSelectPage.this, Number, Title, Source, imageId);

		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "listView pressed " + position, Toast.LENGTH_LONG).show();

				//finish();

				switch (position)
				{
					case 0:	// Media source
						startActivity(new Intent(MainSelectPage.this, DMRActivity.class));
						overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
						break;

					case 1:	// Media Player
						Intent mediaPlayer  = new Intent(MainSelectPage.this, MainActivity.class);
						startActivity(mediaPlayer);
						break;
					case 2:	// Song List
						startActivity(new Intent(MainSelectPage.this, DMSListActivity.class));
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						break;

					default:
						break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public MainSelectPage() {
		super();
	}

	@Override
	protected void findViewById() {

	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void loadViewLayout() {

	}

	@Override
	protected void processLogic() {

	}

	@Override
	public void onClick(View v) {

	}
}
