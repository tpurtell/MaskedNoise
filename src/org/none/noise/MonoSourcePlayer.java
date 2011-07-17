package org.none.noise;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class MonoSourcePlayer implements Runnable {
	private AudioTrack track_;
	private boolean quit_;
	private boolean paused_;
	private MonoSource source_;
	private short[] samples_;

	MonoSourcePlayer(MonoSource source) {
		source_ = source;
		int buffer_size = AudioTrack.getMinBufferSize(MonoSource.SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		samples_ = new short[buffer_size];
        track_ = new AudioTrack(
        		AudioManager.STREAM_MUSIC, 
        		MonoSource.SAMPLE_RATE, 
        		AudioFormat.CHANNEL_OUT_MONO, 
        		AudioFormat.ENCODING_PCM_16BIT, 
        		samples_.length, 
        		AudioTrack.MODE_STREAM);
        //this activates the stream, it shouldn't do anything until we give it data
        //i think (hope) it just pauses when it runs out of data
		track_.play();
		quit_ = false;
		paused_ = true;
	}
	@Override
	public void run() {
		for(;;) {
			synchronized (this) {
				if(quit_)
					break;
				if(paused_) {
					source_.soundBreak();
					try {
						wait();
					} catch(InterruptedException e) {
						//cool, re-check our condition
					}
					continue;
				}
			}
			//generate and write some samples
			source_.getSamples(samples_);
			track_.write(samples_, 0, samples_.length);
		}
	}
	public synchronized void quit() {
		//it'll do it eventually
		quit_ = true;
	}
	public synchronized void pause() {
		//it'll do it eventually
		paused_ = true;
	}
	public synchronized void play() {
		//notify to wake up
		paused_ = false;
		notify();
	}	
}
