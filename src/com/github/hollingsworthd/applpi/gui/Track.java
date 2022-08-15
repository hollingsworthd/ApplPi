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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Track extends JPanel {
    public Track() {
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setSize(new Dimension(Integer.MAX_VALUE, 30));
        setPreferredSize(new Dimension(30, 30));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setMinimumSize(new Dimension(30, 30));
        setBackground(Color.LIGHT_GRAY);
        addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void mousePressed(MouseEvent e) {
                TrackSegment ts = new TrackSegment();
                ts.setLocation(0, e.getX());
                ts.setSize(10,30);
                add(ts);
                revalidate();
            }

            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
                
            }});
    }
}
