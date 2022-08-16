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

package com.github.hollingsworthd.applpi.core.gen;

import com.github.hollingsworthd.applpi.core.UnitGenerator;

public class Gain extends UnitGenerator {

  private double gain;

  public Gain(double p_gain) {
    this.gain = p_gain;
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    for (int i = 0; i < inputVal.length; i++) {
      inputVal[i] *= gain;
    }
  }

  public void setGain(double p_gain) {
    gain = p_gain;
  }
}