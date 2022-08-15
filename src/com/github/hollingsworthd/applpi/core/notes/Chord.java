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

package com.github.hollingsworthd.applpi.core.notes;

import com.github.hollingsworthd.applpi.core.UnitGenerator;
import com.github.hollingsworthd.applpi.core.gen.Tone;

public class Chord extends UnitGenerator {

    private Tone[] tones = null;
    private String chordName = null;

    public Chord(Class<? extends Tone> tone, String chordName, double rootFreq) {
        setChord(tone, chordName, rootFreq);
    }

    public synchronized void setChord(double rootFreq) {
        setChord(null, null, rootFreq);
    }

    public synchronized void setChord(String chordName, double rootFreq) {
        setChord(null, chordName, rootFreq);
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(offset, inputVal, sampleRate);
        double val = 0;

        for (int i = 0; i < tones.length; i++) {
            tones[i].next(offset, inputVal, sampleRate);
            double d = 0;
            for (int j = 0; j < inputVal.length; j++) {
                d += inputVal[j];
            }
            val += d / inputVal.length;
        }
        val /= tones.length;

        for (int i = 0; i < inputVal.length; i++) {
            inputVal[i] = val;
        }
    }

    private void setChord(Class<? extends Tone> tone, String chordName, double rootFreq) {
        if (chordName != null) {
            this.chordName = chordName;
        }
        double[] freqs = Note.TwelveTET.instance().getFreqs(this.chordName, rootFreq);
        if (tone != null) {
            tones = new Tone[freqs.length];
        }
        for (int i = 0; i < freqs.length; i++) {
            try {
                if (tone != null) {
                    tones[i] = tone.newInstance();
                }
                tones[i].setFreq(freqs[i]); //TODO set freq by force?
            } catch (InstantiationException e) {
                //TODO
            } catch (IllegalAccessException e) {
                //TODO
            }
        }
    }
}
