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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.hollingsworthd.applpi.gui.Main;

public class ApplPi extends JFrame {

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new ApplPi();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ApplPi() {
		Main view = new Main();
		getContentPane().add(view);
		setTitle("ApplPi#3.1 http://tiny.cc/applpi (c) 2009-2013 Daniel Hollingsworth");
		this.setSize(600, 80);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
