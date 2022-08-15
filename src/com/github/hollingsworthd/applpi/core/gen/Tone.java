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

package com.github.hollingsworthd.applpi.core.gen;

import com.github.hollingsworthd.applpi.core.UnitGenerator;

public abstract class Tone extends UnitGenerator {
    private double prevPeriod = 1.;
    private double inc = 0.;
    private double targetFreq = 1.;
    private double oldFreq = 1.;
    private final double FREQ_CHANGE_SPEED = 350.;
    private boolean slide = true;
    private double diffLen = 1.;

    public final void setFreq(double p_freq) {

        //TODO set freq by force if it hasnt been set yet?
        if (oldFreq == targetFreq || !slide) {
            targetFreq = p_freq;
            if (!slide) {
                oldFreq = p_freq;
            }
        }
    }

    public final void setFreqByForce(double p_freq) {
        targetFreq = p_freq;
        oldFreq = targetFreq;
    }

    public final double getFreq(double sampleRate) {
        double ret = targetFreq;
        if (oldFreq != targetFreq) {
            if (Math.abs(oldFreq - targetFreq) <= diffLen) {
                oldFreq = targetFreq;
            } else {
                double direction = targetFreq > oldFreq ? 1 : -1;
                double dist = Math.abs(targetFreq - oldFreq);
                diffLen = FREQ_CHANGE_SPEED * direction
                          * (dist < 1 ? 1 : dist)
                          * Math.sqrt(targetFreq * 2)
                          / sampleRate;
                oldFreq += diffLen;
            }

            ret = oldFreq;
        }
        return ret;
    }

    public final void setPrevPeriod(double prevPeriod) {
        this.prevPeriod = prevPeriod;
    }

    public final double getPrevPeriod() {
        return prevPeriod;
    }

    public final void setInc(double inc) {
        this.inc = inc;
    }

    public final double getInc() {
        return inc;
    }
}