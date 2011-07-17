package org.none.noise;

public interface MonoSource {
	public static final int SAMPLE_RATE = 44100;
	// generate some new samples
	public void getSamples(short[] samples);
	// higher level was paused
	public void soundBreak();
}
