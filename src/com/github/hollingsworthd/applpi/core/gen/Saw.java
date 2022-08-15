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

public class Saw extends Tone {
    public Saw() {
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(offset, inputVal, sampleRate);
        double period = sampleRate / getFreq(sampleRate);
        setInc(period / getPrevPeriod() * getInc() + offset);
        setPrevPeriod(period);
        if (getInc() >= period) {
            setInc(0);
        } else if (getInc() < 0) {
            setInc(period - 1);
        }
        double val = 2 * (getInc() / period - Math.floor(getInc() / period + .5));
        for (int i = 0; i < inputVal.length; i++) {
            inputVal[i] = val;
        }
    }
}