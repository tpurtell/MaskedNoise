package org.none.noise;

import java.util.Random;

public class BandPassWhiteNoiseSource implements MonoSource {
	private Random random_;
	private float centerFrequency_;
	private float bandWidth_;
	private float attentuation_;
	private int[] partials_;
	private int[] kernel_;
	
	BandPassWhiteNoiseSource(float centerFrequency, float bandWidth, float attenuation) {
		kernel_ = new int[MonoSource.SAMPLE_RATE];
		partials_ = new int[kernel_.length];
		random_ = new Random();
		setParameters(centerFrequency, bandWidth, attenuation);
	}

	public void setParameters(float centerFrequency, float bandWidth, float attenuation) {
		centerFrequency_ = centerFrequency;
		bandWidth_ = bandWidth;
		attentuation_ = attenuation;
		updateKernel();
	}
	public void setCenterFrequency(float centerFrequency) {
		centerFrequency_ = centerFrequency;
	}
	public float getCenterFrequency() {
		return centerFrequency_;
	}
	public void setBandWidth(float bandWidth) {
		bandWidth_ = bandWidth;
		updateKernel();
	}
	public float getBandWidth() {
		return bandWidth_;
	}
	public void setAttentuation(float attentuation) {
		attentuation_ = attentuation;
		updateKernel();
	}
	public float getAttentuation() {
		return attentuation_;
	}
	
	private void updateKernel() {
		for(int i = 0; i < kernel_.length; ++i) {
			//for now, random value between -32k and +32k
			int r = random_.nextInt();
			if(r < 0)
				kernel_[i] = random_.nextInt() >> 8;
			else
				kernel_[i] = -(random_.nextInt() >> 8);			
		}
	}
	
	@Override
	public void getSamples(short[] samples) {
		for (int i = 0; i < samples.length; i += 2) {
			int r = random_.nextInt();
			samples[i] = (short)r;
			samples[i + 1] = (short)(r >> 16);
		}
	}
	@Override
	public void soundBreak() {
		for(int i = 0; i < partials_.length; ++i) {
			partials_[i] = 0;
		}
		
	}

}
