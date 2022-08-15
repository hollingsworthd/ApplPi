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

package com.github.hollingsworthd.applpi.composition;

import com.github.hollingsworthd.applpi.core.Composition;
import com.github.hollingsworthd.applpi.core.Patch;
import com.github.hollingsworthd.applpi.core.Mix;
import com.github.hollingsworthd.applpi.core.effect.Reverb;
import com.github.hollingsworthd.applpi.core.gen.Gain;
import com.github.hollingsworthd.applpi.core.gen.Sine;
import com.github.hollingsworthd.applpi.core.gen.Tone;
import com.github.hollingsworthd.applpi.core.notes.Note;

public class Guinness extends Composition {
    private double start = 0;
    private double cur = 0;
    private double len = 0;
    private double scale = 12.;
    private double grain = 3.;
    private boolean isWild = false;
    private static Patch master = new Patch();
    private final Tone sine1;
    private final Tone sine2;
    private final Tone sine3;
    double[] loFreqs = new double[] { Note.TwelveTET.instance().getFreq(0, 16),
                                     Note.TwelveTET.instance().getFreq(3, 16),
                                     Note.TwelveTET.instance().getFreq(5, 16),
                                     Note.TwelveTET.instance().getFreq(7, 16),
                                     Note.TwelveTET.instance().getFreq(0, 4) };
    double[] hiFreqs = new double[] { Note.TwelveTET.instance().getFreq(0, 4),
                                     Note.TwelveTET.instance().getFreq(3, 4),
                                     Note.TwelveTET.instance().getFreq(5, 4),
                                     Note.TwelveTET.instance().getFreq(7, 4),
                                     Note.TwelveTET.instance().getFreq(0, 8) };
    double[] hiFreqs2 = new double[] { Note.TwelveTET.instance().getFreq(3, 8),
                                      Note.TwelveTET.instance().getFreq(5, 8),
                                      Note.TwelveTET.instance().getFreq(7, 8),
                                      Note.TwelveTET.instance().getFreq(7, 16),
                                      Note.TwelveTET.instance().getFreq(8, 16) };

    public Guinness() {
        super(master);
        Patch patcher1 = new Patch();
        sine1 = new Sine(); //TODO add freq to ctor
        patcher1.add(sine1);
        patcher1.add(new Gain(.035));

        Patch patcher2 = new Patch();
        sine2 = new Sine();
        patcher2.add(sine2);
        patcher2.add(new Gain(.035));

        Patch patcher3 = new Patch();
        sine3 = new Sine();
        patcher3.add(sine3);
        patcher3.add(new Gain(.035));

        Mix mixer = new Mix();
        mixer.add(patcher1);
        mixer.add(patcher2);
        mixer.add(patcher3);

        Reverb reverb = new Reverb();
        master.add(mixer);
        master.add(reverb);
    }

    @Override
    public void aboutToComputeSample(double sampleRate) {
        cur = getCurTimeInMs(sampleRate);
        if (cur >= 0 && (Math.abs(cur - start) >= len) && (cur != start || cur == 0)) {

            start = cur;
            double base = 0;
            double deviation = rand.nextDouble() * .0001;
            if (true) {
                double noteChange = rand.nextDouble();
                if (noteChange < .12) {
                    base = loFreqs[(int) Math.rint(rand.nextDouble() * ((double) loFreqs.length - 1))];
                    sine1.setFreq((deviation + 1.) * base * getRoot());
                }
                if (noteChange < .06) {
                    base = hiFreqs[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs.length - 1))];
                    sine2.setFreq((deviation + 1.) * base * getRoot());
                }
                if (noteChange < .9) {
                    base = hiFreqs2[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs2.length - 1))];
                    sine3.setFreq((deviation + 1.) * base * getRoot());
                }
            }
            double factor = 1000 * rand.nextDouble();
            isWild = rand.nextDouble() < .2;
            len = (((rand.nextDouble() * factor)) * factor);

            if (!isWild) {
                len %= 32452843;
                len = len == 0 ? 7001 : len;
            } else {
                len %= 15485863;
                len = len == 0 ? 4001 : len;
            }
            len %= scale;
            len -= len % grain;
            len -= cur % grain; //error correction... try to stay on beat
            len += grain;
        }
    }

    private boolean prevOdd = false;

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(Math.random() < .7 ? 1 : (Math.random() < .5 ? 0 : 2), inputVal, sampleRate);
    }
}
