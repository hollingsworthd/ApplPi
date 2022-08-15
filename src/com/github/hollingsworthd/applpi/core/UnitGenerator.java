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

import java.util.ArrayList;

public abstract class UnitGenerator {
    private ArrayList<Composition> listeners = null;

    public UnitGenerator() {
    }

    public final boolean isValid() {
        return true;//TODO
    }

    /**
     * If you override this (which is generally what you'll want to do),
     * just be sure to call super.
     * @param offset TODO
     * @param inputVal
     * @param conf
     */
    public void next(double offset, double[] inputVal, double sampleRate) {
        if (listeners != null) {
            fireNext(inputVal, sampleRate);
        }
    }

    private void fireNext(double[] inputVal, double sampleRate) {
        for (int i = 0; i < listeners.size(); i++) {
            Composition listener = listeners.get(i);
            listener.aboutToComputeSample(sampleRate);
        }
    }

    public final void addListener(Composition listener) {
        listeners = listeners == null ? new ArrayList<Composition>() : listeners;
        listeners.add(listener);
    }

    public final void removeListener(Composition listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
}
