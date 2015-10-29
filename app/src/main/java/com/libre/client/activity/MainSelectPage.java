package com.libre.client.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlna.dmc.processor.impl.UpnpProcessorImpl;
import com.app.dlna.dmc.processor.interfaces.DMRProcessor;
import com.app.dlna.dmc.processor.interfaces.UpnpProcessor;
import com.libre.client.WifiConnect;
import com.libre.client.activity.listview.MainSelectListSeg;
import com.libre.client.util.DMRControlHelper;
import com.libre.client.util.PlaybackHelper;
import com.libre.client.util.UpnpDeviceManager;

import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;

import java.util.ArrayList;

public class MainSelectPage extends BaseActivity implements DMRProcessor.DMRProcessorListener {

	private static final int MSG_REG_BROADCAST = 0x01;
	private static final int MSG_DMR_UPDATEPOSITION = 0xA0;
	private static final int MSG_DMR_PLAYCOMPLETED = 0xA1;
	private static final int MSG_DMR_PLAYING = 0xA2;
	private static final int MSG_DMR_PAUSED = 0xA3;
	private static final int MSG_DMR_STOPPED = 0xA4;
	private static final int MSG_DMR_SETURI = 0xA5;
	private static final int MSG_ONACTIONFAIL=0xA6;
	private static final int MSG_ONACTIONVOLUME=0xA7;


	private static final String TAG = "PLAY VIEW";
	private LibreApplication m_myApp;
	private long m_waitTime = 2000;
	private long m_lastBackTime = 0;
	private ImageButton m_speaker = null;
	private ImageButton m_music = null;
	private ImageButton m_next = null;
	private ImageButton m_previous = null;
	private TextView m_nowTime, m_totalTime;
	String URL,META;
	private NetworkStateReceiver m_networkReceiver;
	private UpnpProcessor m_upnpProcessor = null;
	private PlaybackHelper m_playbackHelper = null;
	private DMRControlHelper m_dmrControlHelper = null;
	private DMRProcessor m_dmrProcessor = null;
	private boolean m_isPlaybackSeeking = false;
	private TextView m_singer;
	private TextView m_song;
	private ImageView m_songimg;

	private ImageView m_source;
	private TextView m_speakername;
	private AudioManager m_audioManager;

	private SeekBar m_sb_playingProgress;
	private MySlider m_sb_volume;
	private ImageButton m_btn_PlayPause;
	private ImageButton m_volumePlus;

	private boolean restartneeded=false;

	private PLAY_STATE m_currentPlayState = PLAY_STATE.STOP;


	// yhcha, block
	/*Handler wifihandler = new Handler() {
		@SuppressLint("InflateParams")
		@Override
		public void handleMessage(Message msg) {

			Log.v(TAG, "wifihandler : " + msg.what);

			if (msg.what == WIFICONST.NETWORK_CHANGED)
			{

				*//*Log.v(TAG,"Net changed in MainActivity");
				for (Iterator<String> i = LibreApplication.PLAYBACK_HELPER_MAP.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					LibreApplication.PLAYBACK_HELPER_MAP.get(key).getDmrHelper().getDmrProcessor().dispose();
				}
				m_playbackHelper=null;
				m_myApp.setCurrentDmrDeviceUdn(LibreApplication.LOCAL_UDN);*//*

				restartneeded=true;

				*//*if (m_upnpProcessor != null) {
					m_upnpProcessor.removeListener(UpnpDeviceManager.getInstance());
					m_upnpProcessor.unbindUpnpService();
					//m_upnpProcessor.stopMusicServer();
				}
				//m_upnpProcessor = new UpnpProcessorImpl(MainActivity.this);
				m_upnpProcessor.bindUpnpService();
				m_upnpProcessor.addListener(UpnpDeviceManager.getInstance());*//*


			}
			else if (msg.what == WIFICONST.SSID_NOT_FOUND)
			{
				if(attempts<5)
				{
					connect.SearchMore();
					attempts++;
				}
				else
				{

					connect.close();
					wifihandler.sendEmptyMessageDelayed(OOH_CONNECT_FAILED, 1000);

				}
			}
			else if (msg.what == WIFICONST.SSID_FOUND)
			{
				String data = (String)msg.obj;
				Log.v(TAG,"Found a Speaker Message-----");
				connect.close();
				connect.Connect(data,"hello123");

			}
			else if (msg.what ==WIFICONST.SSID_CONNECTED)
			{
				connect.close();
				wifihandler.sendEmptyMessage(OOH_CONNECT_SUCCESS);

			}
			else if (msg.what == WIFICONST.SSID_CONNECT_FAILED)
			{
				connect.close();
				wifihandler.sendEmptyMessageDelayed(OOH_CONNECT_FAILED, 1000);
				//DisplayAlert("");

			}
			else if (msg.what == OOH_CONNECT_FAILED)
			{
				m_progressDlg.dismiss();

				DisplayAlert("Failed to Connect to DDMS Network");
				//finish();

			}
			else if (msg.what == OOH_CONNECT_SUCCESS)
			{
				m_progressDlg.dismiss();

				DisplayAlert("Connected to DDMS Network");
				//m_myApp.Restart();
				//System.exit(1);

			}
		}

	};*/
	private void DisplayAlert(String msg){

		final AlertDialog alertDialog1 = new AlertDialog.Builder(
				MainSelectPage.this).create();

		// Setting Dialog Title
		alertDialog1.setTitle("Alert");

		// Setting Dialog Message
		alertDialog1.setMessage(msg);

		// Setting Icon to Dialog
		alertDialog1.setIcon(R.drawable.ic_launcher);
		alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog
				// closed
				// Intent i = getBaseContext().getPackageManager()
				//       .getLaunchIntentForPackage( getBaseContext().getPackageName() );
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(i);

				Intent mainIntent = new Intent(MainSelectPage.this, LodingActivity.class);
				mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
				startActivity(mainIntent);
				finish();
				Toast.makeText(getApplicationContext(),
						"You clicked on OK", Toast.LENGTH_SHORT).show();
				alertDialog1.dismiss();
			}
		});




		// Showing
		//
		//
		// Alert Message
		alertDialog1.show();
	}
	@Override
	public void onUpdatePosition(long position, long duration) {

	}

	@Override
	public void onUpdateVolume(int currentVolume) {

	}

	@Override
	public void onPaused() {

	}

	@Override
	public void onStoped() {

	}

	@Override
	public void onSetURI() {

	}

	@Override
	public void onPlayCompleted() {

	}

	@Override
	public void onPlaying() {

	}

	@Override
	public void onActionFail(Action actionCallback, UpnpResponse response, String cause) {


	}

	private enum PLAY_STATE {
		PAUSE,
		PLAYING,
		STOP
	}


	WifiConnect connect;
	int attempts=0;
	private ProgressDialog m_progressDlg;
	protected int OOH_CONNECT_FAILED=0x30;
	protected int OOH_CONNECT_SUCCESS=0x31;

	MainSelectListSeg adapter;
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
			"",
			"",
			"There are your songs, photos and videos",
	} ;

	Integer[] imageId = {
			R.drawable.itunesicon300x300,
			R.drawable.music_player,
			R.drawable.book_list,
	};

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		getSupportActionBar().hide();
		Log.d(TAG, "onCreate");
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_select_page);

		/*Intent getValue = getIntent();
		Source[0] = getValue.getStringExtra("SongListName");*/

		adapter = new MainSelectListSeg(MainSelectPage.this, Number, Title, Source, imageId);

		listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "listView pressed " + position, Toast.LENGTH_LONG).show();

				//finish();

				Intent getValue = getIntent();

				switch (position)
				{
					case 0:	// Song List
						//startActivity(new Intent(MainSelectPage.this, DMSListActivity.class));
						Intent intent0 = new Intent(MainSelectPage.this, DMSListActivity.class);
						intent0.putExtra("SongListName", getValue.getStringExtra("SongListName"));

						intent0.putExtra("PlayerName", getValue.getStringExtra("PlayerName"));	// save current selected device name
						intent0.putExtra("PlayerUuid", getValue.getStringExtra("PlayerUuid"));	// save current selected uuid number

						startActivity(intent0);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						break;

					case 1:	// Select Media Player
                        //startActivity(new Intent(MainSelectPage.this, DMRActivity.class));
						Intent intent1 = new Intent(MainSelectPage.this, DMRActivity.class);
						intent1.putExtra("SongListName", getValue.getStringExtra("SongListName"));
						startActivity(intent1);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						break;

					case 2:	// Browse and play media
                        //RemoteDevice device = m_adapter.getItem(position);
                        //m_myApp.setDmsBrowseHelperTemp(new DMSBrowseHelper(false, device.getIdentity().getUdn().toString()));
                        Constant.isUpNPbroswer=true;

						//Intent getValue = getIntent();
                        Intent intent2 = new Intent(MainSelectPage.this, DMSBrowserActivity.class);
						intent2.putExtra("SongListName", getValue.getStringExtra("SongListName"));
                        //MainSelectPage.this.startActivity(intent);
                        startActivity(intent2);

						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						//startActivity(new Intent(MainSelectPage.this, DMRActivity.class));

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
		Log.d(TAG,"OnResume");

		Intent getValue = getIntent();
		Source[0] = getValue.getStringExtra("SongListName");
		Source[1] = getValue.getStringExtra("PlayerName");

		MainSelectListSeg newAdapter = new MainSelectListSeg(MainSelectPage.this, Number, Title, Source, imageId);
		listView.setAdapter(newAdapter);
		listView.refreshDrawableState();

		// yhcha, block
		/*if(Source[0] != null || Source[1] != null )
		{
			//adapter.getTextSourceNameAtindex().setTextColor(0xFF0000FF);
		}

		Log.d(TAG, "UpdateUI" + "Title[0] = " + Title[0] + getValue.getStringExtra("SongListName") + getValue.getIntExtra("SongListNumber",100));

		if(restartneeded)
		{
			restartneeded=false;
			//finish();
			//System.exit(1);

			//	finish();
			//	startActivity(new Intent(this, LodingActivity.class));
		}
		m_upnpProcessor.addListener(MainSelectPage.this);
		String udn = m_myApp.getCurrentDmrDeviceUdn();
		if (udn == null || udn == "")
		{
			Log.d("Main Activity", "Null Error!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return;
		}*/
		/*m_playbackHelper = m_myApp.getCurrentPlaybackHelper();
		if (m_playbackHelper == null) {
			if (udn.equals(LibreApplication.LOCAL_UDN)) return;
			RemoteDevice deviceMeta = UpnpDeviceManager.getInstance().getRemoteDmrMap().get(udn);
			if (deviceMeta == null) return;
			RemoteService service = deviceMeta.findService(new ServiceType(DMRControlHelper.SERVICE_NAMESPACE,
					DMRControlHelper.SERVICE_AVTRANSPORT_TYPE));
			if (service == null) return;
			DMRControlHelper dmrControl = new DMRControlHelper(udn,
					m_upnpProcessor.getControlPoint(), deviceMeta, service);
			m_playbackHelper = new PlaybackHelper(dmrControl);
			LibreApplication.PLAYBACK_HELPER_MAP.put(udn, m_playbackHelper);
		}
		m_dmrControlHelper = m_playbackHelper.getDmrHelper();
		m_dmrProcessor = m_dmrControlHelper.getDmrProcessor();

		m_dmrProcessor.addListener(MainSelectPage.this);
		m_myApp.setDmsBrowseHelperSaved(m_playbackHelper.getDmsHelper());

		handleUI();

		if (m_myApp.isPlayNewSong()) {
			try {
				m_playbackHelper.playSong();
				m_myApp.setPlayNewSong(false);
			} catch (Throwable t) {}
		} else {
			//TODO set playback screen: now, total, image...
			//setupPlaybackScreen();
			if (m_playbackHelper.isPlaying()) {
				//setPlayStartStatus();
			} else {
				//setPlayStopStatus();
			}
		}
*/
	}

	private void handleUI() {

		//resetPlayBackScreen();

		// yhcha, block
		/*if (m_dmrControlHelper.isLocalDevice()) {
			*//*m_sb_volume.setMax(m_dmrProcessor.getMaxVolume());
			m_sb_volume.setProgress(m_dmrProcessor.getVolume());*//*
			//	m_speakername.setText(m_dmrControlHelper.getDmrDisplayName());
		}
		else
		{
			if (LibreApplication.PLAYBACK_HELPER_MAP.containsKey(LibreApplication.LOCAL_UDN)) {
				LibreApplication.PLAYBACK_HELPER_MAP.get(LibreApplication.LOCAL_UDN)
						.getDmrHelper().getDmrProcessor().reset();
			}
			*//*m_sb_volume.setMax(m_dmrProcessor.getMaxVolume());
			m_sb_volume.setProgress(m_dmrProcessor.getVolume());
			m_speakername.setText(m_myApp.getSpeakerName());*//*
		}
		//handler.sendEmptyMessage(0x01);

		// Refresh DMS Name	by yhcha*/
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
		// yhcha, block
		//m_myApp = (LibreApplication)getApplication();

		/*ViewGroup.LayoutParams imgParams = m_songimg.getLayoutParams();
		imgParams.width = m_myApp.getImageViewSize();
		imgParams.height = m_myApp.getImageViewSize();
		m_songimg.setLayoutParams(imgParams);*/

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		m_upnpProcessor = new UpnpProcessorImpl(MainSelectPage.this);
		m_upnpProcessor.bindUpnpService();
		m_upnpProcessor.addListener(UpnpDeviceManager.getInstance());

		//AppPreference.PREF = PreferenceManager.getDefaultSharedPreferences(MainSelectPage.this);

		//NetworkStateReceiver.registerforNetchange(wifihandler);

	}

	@Override
	public void onClick(View v) {

	}
}
