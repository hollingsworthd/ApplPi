/*
 *  Copyright 2009-2022 Daniel Hollingsworth
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

  private void add(UnitGenerator newUnit,
      UnitGenerator existingUnit, boolean addBefore) {
    LinkedList<UnitGenerator> tempUnits = new LinkedList<UnitGenerator>();
    tempUnits.addAll(units);
    Iterator<UnitGenerator> iter = tempUnits.iterator();

    int i = 1;
    if (addBefore) {
      i = 0;
    }

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

  public void addAfter(UnitGenerator newUnit,
      UnitGenerator existingUnit) {
    add(newUnit, existingUnit, false);
  }

  public void addBefore(UnitGenerator newUnit,
      UnitGenerator existingUnit) {
    add(newUnit, existingUnit, true);
  }

  public void remove(UnitGenerator unit) {
    units.remove(unit);
  }

}