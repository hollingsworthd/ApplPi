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

public class Comb extends UnitGenerator {
    private double feedback;
    private double filterStore;
    private double dampA;
    private double dampB;
    private double[] buf;
    private int curBuf;

    public Comb() {
        filterStore = 0;
        curBuf = 0;
    }

    public void setBuffer(double[] buf) {
        this.buf = buf;
    }

    public double process(double inputVal) {
        double output;

        output = buf[curBuf];

        filterStore = (output * dampB) + (filterStore * dampA);

        buf[curBuf] = inputVal + (filterStore * feedback);

        if (++curBuf >= buf.length)
            curBuf = 0;

        return output;
    }

    public void mute() {
        for (int i = 0; i < buf.length; i++)
            buf[i] = 0;
    }

    public void setDamp(double val) {
        dampA = val;
        dampB = 1 - val;
    }

    public double getDamp() {
        return dampA;
    }

    public void setFeedback(double val) {
        feedback = val;
    }

    public double getFeedback() {
        return feedback;
    }

}
