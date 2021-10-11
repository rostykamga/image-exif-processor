package org.rostykamga.imageexifprocessor.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Enumeration;

/**
 * Picture tile will be used to display a selectable picture into a grid.
 */
public class PictureTile extends JToggleButton implements MouseListener {

    //the actual file of the picture
    File pictureFile;

    //Button group, used to ensure exclusive selection of image
    private ButtonGroup group;

    public PictureTile(File picture, ButtonGroup group){

        super();
        pictureFile = picture;
        this.group = group;
        this.setIcon(new ImageIcon(picture.getAbsolutePath()));
        this.addMouseListener(this);
    }

    public File getPictureFile() {
        return pictureFile;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * When mouse is released on a tile, then, set a highlighted border on it if it is selected
     * otherwise, clear the border.
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if(!this.isSelected()) {
            this.setBorder(null);
        }
        else{
            //Clears all the previously selected items.
            Enumeration<AbstractButton> enumeration = group.getElements();
            while(enumeration.hasMoreElements()) {
                AbstractButton btn = enumeration.nextElement();
                btn.setBorder(null);
                btn.revalidate();
            }

            this.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.GREEN));
        }
        this.revalidate();
    }

    /**
     * Hover the pictures when the mouse enters it.
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if(!this.isSelected()) {
            this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));
            this.revalidate();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(!this.isSelected()) {
            this.setBorder(null);
        }
        this.revalidate();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(150,150);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100,100);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(130,130);
    }
}
