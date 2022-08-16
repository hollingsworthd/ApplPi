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

package com.github.hollingsworthd.applpi.composition;

import com.github.hollingsworthd.applpi.core.Composition;
import com.github.hollingsworthd.applpi.core.Mix;
import com.github.hollingsworthd.applpi.core.Patch;
import com.github.hollingsworthd.applpi.core.effect.Reverb;
import com.github.hollingsworthd.applpi.core.gen.Gain;
import com.github.hollingsworthd.applpi.core.gen.Saw;
import com.github.hollingsworthd.applpi.core.gen.Sine;
import com.github.hollingsworthd.applpi.core.gen.Tone;
import com.github.hollingsworthd.applpi.core.notes.Note;

public class RollingRock extends Composition {

  private static Patch master = new Patch();
  private final Tone sine1;
  private final Tone sine2;
  private final Tone sine3;
  double[] loFreqs = new double[]{Note.TwelveTET.instance().getFreq(3, 16),
      Note.TwelveTET.instance().getFreq(5, 16),
      Note.TwelveTET.instance().getFreq(7, 16),
      Note.TwelveTET.instance().getFreq(8, 16),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(5, 4),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(8, 4),
      Note.TwelveTET.instance().getFreq(0, 4),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(8, 8)};
  double[] hiFreqs = new double[]{
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(8, 8),
      Note.TwelveTET.instance().getFreq(0, 4),
      Note.TwelveTET.instance().getFreq(5, 4),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(8, 4),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(5, 16),
      Note.TwelveTET.instance().getFreq(7, 16),
      Note.TwelveTET.instance().getFreq(8, 16)};
  double[] hiFreqs2 = new double[]{Note.TwelveTET.instance().getFreq(3, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(8, 8),
      Note.TwelveTET.instance().getFreq(0, 2),
      Note.TwelveTET.instance().getFreq(5, 16),
      Note.TwelveTET.instance().getFreq(7, 16),
      Note.TwelveTET.instance().getFreq(8, 16),
      Note.TwelveTET.instance().getFreq(0, 4),
      Note.TwelveTET.instance().getFreq(3, 4),
      Note.TwelveTET.instance().getFreq(5, 4),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(8, 4)};
  private double start = 0;
  private double cur = 0;
  private double len = 0;
  private double scale = 10.;
  private double grain = 1;
  private boolean isWild = false;

  public RollingRock() {
    super(master);
    Patch patcher1 = new Patch();
    sine1 = new Sine(); //TODO add freq to ctor
    patcher1.add(sine1);
    patcher1.add(new Gain(.03));

    Patch patcher2 = new Patch();
    sine2 = new Saw();
    patcher2.add(sine2);
    patcher2.add(new Gain(.03));

    Patch patcher3 = new Patch();
    sine3 = new Sine();
    patcher3.add(sine3);
    patcher3.add(new Gain(.03));

    Mix mixer = new Mix();
    mixer.add(patcher1);
    mixer.add(patcher2);
    mixer.add(patcher3);

    Reverb reverb = new Reverb();
    master.add(mixer);
    master.add(reverb);
  }

  @Override
  public void aboutToComputeSample(double sampleRate) {
    cur = getCurTimeInMs(sampleRate);
    if (cur >= 0 && (Math.abs(cur - start) >= len) && (cur != start || cur == 0)) {

      start = cur;
      double base = 0;
      double deviation = rand.nextBoolean() ? .001 : 0;
      if (System.currentTimeMillis() % 5 < rand.nextInt(5) || rand.nextBoolean()) {
        double noteChange = rand.nextDouble();
        if (noteChange < .75) {
          base = loFreqs[(int) Math.rint(rand.nextDouble() * ((double) loFreqs.length - 1))];
          sine1.setFreq((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .5) {
          base = hiFreqs[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs.length - 1))];
          sine2.setFreq((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .98) {
          base = hiFreqs2[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs2.length - 1))];
          sine3.setFreq((deviation + 1.) * base * getRoot());
        }
      }
      double factor = 1000 * rand.nextDouble();
      isWild = rand.nextDouble() < .2;
      len = (((rand.nextDouble() * factor)) * factor);

      if (!isWild) {
        len %= 32452843;
        len = len == 0 ? 7001 : len;
      } else {
        len %= 15485863;
        len = len == 0 ? 4001 : len;
      }
      len %= scale;
      len -= len % grain;
      len -= cur % grain; //error correction... try to stay on beat
      len += grain;
    }
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(1, inputVal, sampleRate);
  }

}
