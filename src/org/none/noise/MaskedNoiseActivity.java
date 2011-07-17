package org.none.noise;

import android.app.Activity;
import android.os.Bundle;

public class MaskedNoiseActivity extends Activity {
	private BandPassWhiteNoiseSource source_;
	private MonoSourcePlayer player_;
	private Thread thread_;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        source_ = new BandPassWhiteNoiseSource(8000, 100, 100);
        player_ = new MonoSourcePlayer(source_);
    	thread_ = new Thread(player_);
    	thread_.start();

    	player_.play();
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	player_.play();
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	player_.pause();
    }
}