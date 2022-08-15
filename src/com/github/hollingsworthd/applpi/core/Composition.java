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

import java.util.Random;

public abstract class Composition extends Segment {
    private double root = 440 / 16;

    public Composition(UnitGenerator master) {
        super(master, 0, -1);
        master.addListener(this);
    }

    public static Random rand = new Random(System.currentTimeMillis());

    public abstract void aboutToComputeSample(double sampleRate);

    public synchronized final double getRoot() {
        return root;
    }

    public synchronized final void setRoot(double freq) {
        root = freq;
    }
}
