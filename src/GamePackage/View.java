package GamePackage;

import utilities.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;

//le ce218 sample code has arrived (Provided by Dr Dimitri Ognibene) (enhanced a bit by me)

public class View extends JComponent {

    private Model model;

    private static Image loading = ImageManager.getImage("loading");

    private boolean displayingModel;


    View(){

        displayingModel = false;


    }

    void showModel(Model m){
        this.model = m;
        displayingModel = true;
    }



    @Override
    public void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        AffineTransform initialTransform = g.getTransform();



        if (displayingModel) {
            model.draw(g);
        } else{
            //AffineTransform backup = g.getTransform();
            g.drawImage(loading,g.getTransform(),null);
        }
        g.setTransform(initialTransform);
        revalidate();
    }

    @Override
    public Dimension getPreferredSize() { return Constants.DEFAULT_DIMENSION; }

}