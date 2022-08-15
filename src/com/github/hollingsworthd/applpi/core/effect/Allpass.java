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

package com.github.hollingsworthd.applpi.core.effect;

import com.github.hollingsworthd.applpi.core.UnitGenerator;

public class Allpass extends UnitGenerator {
    private double feedback;
    private double[] buf;
    private int curBuf = 0;

    public Allpass() {
    }

    public void setBuffer(double[] buf) {
        this.buf = buf;
    }

    public double process(double intputVal) {
        double ret;
        double bufVal;

        bufVal = buf[curBuf];

        ret = bufVal - intputVal;
        buf[curBuf] = intputVal + (bufVal * feedback);

        if (++curBuf >= buf.length) {
            curBuf = 0;
        }

        return ret;
    }

    public void mute() {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = 0;
        }
    }

    public void setFeedback(double feedback) {
        this.feedback = feedback;
    }

    public double getFeedback() {
        return 0.;
    }
}
