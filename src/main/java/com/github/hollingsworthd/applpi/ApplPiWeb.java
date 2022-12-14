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
package main.java.com.github.hollingsworthd.applpi;

import com.github.hollingsworthd.applpi.gui.Main;
import javax.swing.JApplet;
import javax.swing.SwingUtilities;

public class ApplPiWeb extends JApplet {

  private Main view = null;

  public void init() {
    //Execute a job on the event-dispatching thread:
    //creating this applet's GUI.
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        public void run() {
          createGUI();
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createGUI() {
    view = new Main();
    getContentPane().add(view);
  }

  @Override
  public void destroy() {
    super.destroy();
    view.stop();
  }

}
