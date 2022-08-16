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

public abstract class Tone extends UnitGenerator {

  private final double FREQ_CHANGE_SPEED = 350.;
  private double prevPeriod = 1.;
  private double inc = 0.;
  private double targetFreq = 1.;
  private double oldFreq = 1.;
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

  public final double getPrevPeriod() {
    return prevPeriod;
  }

  public final void setPrevPeriod(double prevPeriod) {
    this.prevPeriod = prevPeriod;
  }

  public final double getInc() {
    return inc;
  }

  public final void setInc(double inc) {
    this.inc = inc;
  }
}