//This file is part of ApplPi.
//
//ApplPi is a stochastic noise machine.
//Personal website: http://danielhollingsworth.com
//Project page: https://github.com/hollingsworthd/ApplPi
//Copyright (C) 2009-2013 Daniel Hollingsworth
//
//ApplPi is free software: you can redistribute it and/or modify
//it under the terms of the GNU Affero General Public License as
//published by the Free Software Foundation, either version 3 of the
//License, or (at your option) any later version.
//
//ApplPi is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU Affero General Public License
//along with ApplPi.  If not, see <http://www.gnu.org/licenses/>.

package com.github.hollingsworthd.applpi.core;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Play extends UnitGenerator implements Runnable {
    protected final Segment seg;
    private static final double SAMP_SIZE = 2.;
    private static final double BITS_PER_BYTE = 8.;
    private static final double GAIN = 2;
    private static final double MAX = ((Math.rint(Math.pow(2., SAMP_SIZE * BITS_PER_BYTE - 1))) - 1.) * GAIN;
    private static final double MIN = ((Math.rint(Math.pow(2., SAMP_SIZE * BITS_PER_BYTE - 1))) * (-1.)) * GAIN;
    private static final byte[] convertedDouble = new byte[(int) SAMP_SIZE];

    public Play(Segment seg) {
        this.seg = seg;
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(offset, inputVal, sampleRate);
        seg.next(offset, inputVal, sampleRate); //let them get the prev. sample
    }

    public final void run() {
        final double SAMP_RATE = 44100.;
        final long BUF_MS = 150;
        final double BUF_LEN = BUF_MS / 1000. * SAMP_RATE;
        final double CHANNELS = 2.;
        final AudioFormat af = new AudioFormat((float) SAMP_RATE,
                                               (int) (SAMP_SIZE * BITS_PER_BYTE),
                                               (int) CHANNELS,
                                               true,
                                               true);
        final DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        final byte[] buf = new byte[(int) (BUF_LEN * SAMP_SIZE * CHANNELS)];
        SourceDataLine source = null;
        try {
            source = (SourceDataLine) AudioSystem.getLine(info);
            source.open(af, (int) (BUF_LEN * CHANNELS * SAMP_SIZE));
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        source.start();
        double[] input = new double[(int) CHANNELS];
        double[] output = new double[(int) CHANNELS];

        while (!seg.isEnded(SAMP_RATE)) {
            for (int cur = 0; cur < buf.length;) {
                this.next(1, input, SAMP_RATE);
                for (int i = 0; i < input.length; i++) {
                    output[i] = input[i] * MAX;
                }
                for (int chan = 0; chan < CHANNELS; chan++) {
                    byte[] bytes = getBytes(output[chan]);
                    for (int i = 0; i < SAMP_SIZE; i++) {
                        buf[cur++] = bytes[i];
                    }
                }
            }

            int pos = 0;
            while (pos < buf.length) {
                pos = source.write(buf, pos, buf.length - pos);
            }
        }

        source.drain();
        source.stop();
        source.close();
    }

    private static byte[] getBytes(double p_dSample) {

        double dSample = Math.min(MAX, Math.max(MIN, p_dSample));
        int nSample = (int) Math.rint(dSample);
        for (int i = 0; i < SAMP_SIZE; i++) {
            convertedDouble[i] = (byte) ((nSample >> ((int) BITS_PER_BYTE * ((int) SAMP_SIZE - 1 - i))) & 0xFFFFFFFF);
        }
        return convertedDouble;
    }
}
