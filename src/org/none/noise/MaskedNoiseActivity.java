package org.none.noise;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MaskedNoiseActivity extends Activity {
	public static Context context;
	private BandPassWhiteNoiseSource source_;
	private MonoSourcePlayer player_;
	private Thread thread_;
	public MaskedNoiseActivity() {
		super();
		context = this;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        source_ = new BandPassWhiteNoiseSource(4000, 300, 100);
        player_ = new MonoSourcePlayer(source_);
    	thread_ = new Thread(player_);
    	thread_.start();

    	player_.play();
    	
    	final SeekBar seekVolume = (SeekBar)findViewById(R.id.seekVolume);
    	final SeekBar seekAttenuation = (SeekBar)findViewById(R.id.seekAttenuation);
    	final SeekBar seekCenterFrequency = (SeekBar)findViewById(R.id.seekCenterFrequency);
    	final SeekBar seekWidth = (SeekBar)findViewById(R.id.seekWidth);
    	final EditText editWidth = (EditText)findViewById(R.id.width);
    	final EditText editVolume = (EditText)findViewById(R.id.volume);
    	final EditText editCenterFrequency = (EditText)findViewById(R.id.centerFrequency);
    	final EditText editAttenuation = (EditText)findViewById(R.id.attenuation);
    	
    	seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    		@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				editVolume.setText(Integer.toString(seekBar.getProgress()));
			}
    		@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
    		@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
		});

    	editVolume.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				seekVolume.setProgress(Integer.parseInt(v.getText().toString()));
				return false;
			}
		});

    	seekAttenuation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    		@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
    			source_.setAttentuation(seekBar.getProgress());
    			editAttenuation.setText(Integer.toString(seekBar.getProgress()));
			}
    		@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
    		@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
		});

    	editAttenuation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int attenuation = Integer.parseInt(v.getText().toString());
    			source_.setAttentuation(attenuation);
				seekAttenuation.setProgress(attenuation);
				return false;
			}
		});
    	
    	seekWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    		@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
    			editWidth.setText(Integer.toString(seekBar.getProgress()));
    			source_.setBandWidth(seekBar.getProgress());
			}
    		@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
    		@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
		});

    	editWidth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int width = Integer.parseInt(v.getText().toString());
    			source_.setBandWidth(width);
				seekWidth.setProgress(width);
				return false;
			}
		});
    	seekCenterFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    		@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
    			editCenterFrequency.setText(Integer.toString(seekBar.getProgress()));
    			source_.setCenterFrequency(seekBar.getProgress());
			}
    		@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
    		@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
		});

    	editCenterFrequency.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				int center = Integer.parseInt(v.getText().toString());
    			source_.setCenterFrequency(center);
    			seekCenterFrequency.setProgress(center);
				return false;
			}
		});
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