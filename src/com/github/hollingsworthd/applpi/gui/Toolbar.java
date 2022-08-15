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

package com.github.hollingsworthd.applpi.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.hollingsworthd.applpi.composition.BlueMoon;
import com.github.hollingsworthd.applpi.composition.ChocolateStout;
import com.github.hollingsworthd.applpi.composition.CookieCrisp;
import com.github.hollingsworthd.applpi.composition.FrostedFlakes;
import com.github.hollingsworthd.applpi.composition.FruitLoops;
import com.github.hollingsworthd.applpi.composition.Guinness;
import com.github.hollingsworthd.applpi.composition.LuckyCharms;
import com.github.hollingsworthd.applpi.composition.Newcastle;
import com.github.hollingsworthd.applpi.composition.RollingRock;
import com.github.hollingsworthd.applpi.composition.Smithwicks;
import com.github.hollingsworthd.applpi.composition.SummerAle;
import com.github.hollingsworthd.applpi.composition.ToastCrunch;
import com.github.hollingsworthd.applpi.core.Composition;
import com.github.hollingsworthd.applpi.core.Play;

public class Toolbar extends JToolBar {
	private static final double DEFAULT_ROOT = 440;
	private static double root = DEFAULT_ROOT / 16;
	private Composition[] compositions = new Composition[] { new LuckyCharms(),
			new FruitLoops(), new FrostedFlakes(), new CookieCrisp(),
			new ToastCrunch(), new SummerAle(), new Newcastle(),
			new Guinness(), new ChocolateStout(), new BlueMoon(),
			new RollingRock(), new Smithwicks(), };
	private int curComp = 0;
	private int maxIndex = compositions.length - 1;
	private Thread thread = null;
	private boolean isPlaying = false;
	private JButton prev;
	private JButton play;
	private JButton next;
	private JSpinner rootSpinner;

	public synchronized boolean isPlaying() {
		return isPlaying;
	}

	JFrame mainFrame;
	JLabel label;

	public synchronized void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	public synchronized int getCurComp() {
		return curComp;
	}

	public synchronized boolean incCurComp() {
		curComp++;
		if (curComp > maxIndex) {
			curComp = maxIndex;
			return false;
		}
		return true;
	}

	public synchronized boolean decCurComp() {
		curComp--;
		if (curComp < 0) {
			curComp = 0;
			return false;

		}
		return true;
	}

	public void close() {
		if (isPlaying()) {
			togglePlay();
		}
	}

	private void togglePlay() {
		if (isPlaying()) {
			compositions[getCurComp()].stopPlaying();
			setPlaying(false);
		} else {
			try {
				if (thread != null) {
					thread.join();
				}
			} catch (InterruptedException e1) {
				// do nothing
			}
			compositions[getCurComp()].startPlaying();
			thread = new Thread(new Play(compositions[getCurComp()]));
			setPlaying(true);
			thread.start();
		}
		play.setSelected(isPlaying());
	}

	public Toolbar() {
		JPanel controls = new JPanel();
		prev = new JButton(new AbstractAction("Ç") {
			public void actionPerformed(ActionEvent e) {
				if (isPlaying()) {
					togglePlay();
				}
				if (decCurComp()) {
					compositions[getCurComp()].startPlaying();
					compositions[getCurComp()].setRoot((Double) rootSpinner
							.getValue() / 16d);
					togglePlay();
				}

				if (getCurComp() == 0) {
					prev.setEnabled(false);
				}
				if (getCurComp() < maxIndex) {
					next.setEnabled(true);
				}
			}
		});
		prev.setEnabled(false);

		play = new JButton(new AbstractAction(">") {
			public void actionPerformed(ActionEvent e) {
				togglePlay();
			}
		});

		next = new JButton(new AbstractAction("È") {
			public void actionPerformed(ActionEvent e) {
				if (isPlaying()) {
					togglePlay();
				}
				if (incCurComp()) {
					compositions[getCurComp()].startPlaying();
					compositions[getCurComp()].setRoot((Double) rootSpinner
							.getValue() / 16d);
					togglePlay();
				}

				if (getCurComp() == maxIndex) {
					next.setEnabled(false);
				}
				if (getCurComp() > 0) {
					prev.setEnabled(true);
				}
			}
		});

		rootSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_ROOT, 1,
				20000000, 1));
		rootSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				compositions[getCurComp()].setRoot((Double) rootSpinner
						.getValue() / 16d);
			}
		});

		JButton addTrack = new JButton(new AbstractAction("+") {

			public void actionPerformed(ActionEvent e) {
				Tracks.instance().addTrack();
			}
		});

		setFloatable(false);
		JPanel content = new JPanel();
		content.add(prev);
		content.add(play);
		content.add(next);
		content.add(rootSpinner);
		add(content);
	}
}
