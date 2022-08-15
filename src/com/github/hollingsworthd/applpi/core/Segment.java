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

public abstract class Segment extends UnitGenerator {
    private double start = 0;
    private double dur = 10;
    private double curSamp = -1;
    private UnitGenerator unit = null;
    double[] lastSample = null;
    final static double FRAC_POS = .001;
    final static double FRAC_NEG = -1. * FRAC_POS;
    private boolean silent = false;

    public Segment(final UnitGenerator unit, double startInMs, double durInMs) {
        this.unit = unit;
        setStart(startInMs / 1000.);
        setDur(durInMs / 1000.);
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        curSamp += offset;
        super.next(offset, inputVal, sampleRate);

        if (isPlaying(sampleRate)) {
            unit.next(offset, inputVal, sampleRate);
        } else if (isEnded(sampleRate) && !silent) {//do a slight fade out to avoid popping
            silent = true;
            for (int i = 0; i < inputVal.length; i++) {
                double change = inputVal[i] > 0. ? FRAC_NEG : inputVal[i] < 0. ? FRAC_POS : 0.;
                inputVal[i] += change;
                inputVal[i] = Math.abs(inputVal[i]) < 2 * FRAC_POS ? 0. : inputVal[i];
                if (inputVal[i] != 0.) {
                    silent = false;
                }
            }
        } else {
            for (int i = 0; i < inputVal.length; i++) {
                inputVal[i] = 0.;
            }
        }

    }

    private synchronized double getStart() {
        return start;
    }

    private synchronized double getDur() {
        return dur;
    }

    private synchronized void setStart(double d) {
        start = d;
    }

    private synchronized void setDur(double d) {
        dur = d;
    }

    public void startPlaying() {
        setStart(0);
        setDur(-1);
    }

    public void stopPlaying() {
        setStart(0);
        setDur(.5);
    }

    public boolean isPlaying(double sampleRate) {
        return dur < 0 || (curSamp / sampleRate >= getStart() && curSamp / sampleRate - getStart() < getDur());
    }

    public boolean isEnded(double sampleRate) {
        return dur >= 0 && curSamp / sampleRate - getStart() >= getDur();
    }

    /**
     * Might be fractional.  But will be in milliseconds.
     * @param audioConf
     * @return MS
     */
    public double getCurTimeInMs(double sampleRate) {
        return curSamp / sampleRate * 1000d;
    }

    public double getCurSample() {
        return curSamp;
    }
}
