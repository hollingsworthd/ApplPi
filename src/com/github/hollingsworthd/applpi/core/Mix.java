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
import java.util.Arrays;

public class Mix extends UnitGenerator {
    private ArrayList<UnitGenerator> patches = null;

    public Mix() {
        patches = new ArrayList<UnitGenerator>();
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(offset, inputVal, sampleRate);
        double[] runningVal = new double[inputVal.length];
        Arrays.fill(runningVal, 0.);
        for (int i = 0; i < patches.size(); i++) {

            //don't let the patches being mixed affect each other,
            //so always use original input to start
            double[] workingInputVal = inputVal.clone();

            UnitGenerator curUnit = patches.get(i);
            curUnit.next(offset, workingInputVal, sampleRate);

            for (int j = 0; j < workingInputVal.length; j++) {
                runningVal[j] += workingInputVal[j];
            }
        }
        for (int j = 0; j < runningVal.length; j++) {
            inputVal[j] = runningVal[j] / patches.size();
        }
    }

    public void add(UnitGenerator unit) {
        patches.add(unit);
    }
}
