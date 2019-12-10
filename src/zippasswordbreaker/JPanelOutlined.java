/*
   Copyright [2019] [Connor Jack and Jevon Erickson]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package zippasswordbreaker;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * A JPanel that can be defined with a border of any specified size and color
 * @author Connor
 */
public class JPanelOutlined extends JPanel {
    private int outlinePX = 2;
    private Color color = Color.BLUE;
    
    /**
     * 
     * @param outline defines how thick in pixels the border should be
     * @param color defines what color the border should be
     */
    public JPanelOutlined(int outline, Color color){
        outlinePX = outline;
        this.color = color;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Color def = UIManager.getColor("Panel.background");
        int rectwide = super.getWidth() - (outlinePX*2);
        int recttall = super.getHeight() - (outlinePX*2);
        g.setColor(color);
        g.fillRect(0, 0, super.getWidth(), super.getHeight());
        g.setColor(def);
        g.fillRect(outlinePX, outlinePX, rectwide, recttall);
    }
}
