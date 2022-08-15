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
import java.util.Iterator;
import java.util.LinkedList;

public class Patch extends UnitGenerator {
    private ArrayList<UnitGenerator> units = null;

    public Patch() {
        units = new ArrayList<UnitGenerator>();
    }

    @Override
    public void next(double offset, double[] inputVal, double sampleRate) {
        super.next(offset, inputVal, sampleRate);
        for (int i = 0; i < units.size(); i++) {
            UnitGenerator curUnit = units.get(i);
            curUnit.next(offset, inputVal, sampleRate);
        }
    }

    public void add(UnitGenerator unit) {
        units.add(unit);
    }

    private void add(UnitGenerator newUnit, UnitGenerator existingUnit, boolean addBefore) {
        LinkedList<UnitGenerator> tempUnits = new LinkedList<UnitGenerator>();
        tempUnits.addAll(units);
        Iterator<UnitGenerator> iter = tempUnits.iterator();

        int i = 1;
        if (addBefore)
            i = 0;

        Object cur = null;
        while (iter.hasNext()) {
            cur = iter.next();
            if (cur == existingUnit) {
                units.add(i, newUnit);
                i++;
            }
            i++;
        }
    }

    public void addAfter(UnitGenerator newUnit, UnitGenerator existingUnit) {
        add(newUnit, existingUnit, false);
    }

    public void addBefore(UnitGenerator newUnit, UnitGenerator existingUnit) {
        add(newUnit, existingUnit, true);
    }

    public void remove(UnitGenerator unit) {
        units.remove(unit);
    }

}