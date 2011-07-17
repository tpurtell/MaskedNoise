package org.none.noise;

import java.util.Random;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class BandPassWhiteNoiseSource implements MonoSource {
	public static final int FILTER_SIZE = 256;
	private Random random_;
	private float centerFrequency_;
	private float bandWidth_;
	private float attentuation_;
	private int[] partials_;
	private int partialBase_;
	private int[] kernel_;
	private double[] kernelFrequency_;
	FastFourierTransformer fft_;
	
	BandPassWhiteNoiseSource(float centerFrequency, float bandWidth, float attenuation) {
		fft_ = new FastFourierTransformer();
		kernel_ = new int[FILTER_SIZE]; 
		kernelFrequency_ = new double[FILTER_SIZE];
		partials_ = new int[65536];
		partialBase_ = 0;
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
		updateKernel();
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
		double low = Math.pow(10, -attentuation_ / 10);
		double high = 1.0;
		double min_freq = Double.MAX_VALUE;
		double max_freq = Double.MIN_VALUE;
		for(int i = 0; i < kernel_.length; ++i) {
			double freq = 2 * i * SAMPLE_RATE / FILTER_SIZE;
			if(freq < centerFrequency_ - bandWidth_ / 2 || freq > centerFrequency_ + bandWidth_ / 2)
				kernelFrequency_[i] = low;
			else {
				if(freq < min_freq)
					min_freq = freq;
				if(freq > max_freq)
					max_freq = freq;
				kernelFrequency_[i] = high;
			}
		}
		Complex[] transformed = fft_.inversetransform(kernelFrequency_);
		for(int i = 0; i < kernel_.length; ++i) {
			kernel_[i] = (int)(transformed[i].getReal() * 65536);
		}
		//Toast.makeText(MaskedNoiseActivity.context, "kernel range: " + min_freq + " to " + max_freq, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void getSamples(short[] samples) {
		if(partials_.length * 2 < samples.length) {
			//for now assume that the requested samples get processed into partials
			//and then extracted from partials
			throw new RuntimeException("samples must be shorter than partials");
		}
		for (int i = 0; i < samples.length; i += 2) {
			int r = random_.nextInt();
			samples[i] = (short)r;
			samples[i + 1] = (short)(r >> 16);
		}
		for(int i = 0; i < samples.length; ++i) {
			int partial = partialBase_ + i;
			for(int j = 0; j < kernel_.length; ++j) {
				partials_[partial] += ((int)samples[i] << 8) * (kernel_[j] >> 8);
				if(++partial >= partials_.length)
					partial = 0;
			}
		}
		for(int i = 0; i < samples.length; ++i) {
			samples[i] = (short)(partials_[partialBase_] >> 16);
			partials_[partialBase_] = 0;
			if(++partialBase_ >= partials_.length)
				partialBase_ = 0;
		}
	}
	@Override
	public void soundBreak() {
		for(int i = 0; i < partials_.length; ++i) {
			partials_[i] = 0;
		}
		
	}

}
