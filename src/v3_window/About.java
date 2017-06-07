package v3_window;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

/**
 * Classe qui permet d'afficher la fenêtre "A propos"
 * 
 * @author AirDur
 * @version Build III -  v0.6
 * @since Build III -  v0.6
 */
public class About extends JDialog{

	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur de la fenetre
	 * 
	 */
	public About(Frame parent, boolean modal){
        super(parent, modal);
        // the aboutBox is located in the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double ScreenHeight = screenSize.getHeight();
        int width = 350;
        int height = 190;
        int x = ((int)screenWidth-width)/2;
        int y = ((int)ScreenHeight-height)/2;
        setSize(width,height);
        setLocation(x, y);
 
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Maze", JLabel.CENTER);
        title.setFont(new Font("Helvetica",Font.PLAIN,24));
        title.setForeground(new java.awt.Color(255, 153, 102));

        JLabel version = new JLabel("Version: 5.0", JLabel.CENTER);
        version.setFont(new Font("Helvetica",Font.BOLD,14));

        JLabel programmer = new JLabel("Designer: Nikos Kanargias", JLabel.CENTER);
        programmer.setFont(new Font("Helvetica",Font.PLAIN,16));

        JLabel email = new JLabel("E-mail: nkana@tee.gr", JLabel.CENTER);
        email.setFont(new Font("Helvetica",Font.PLAIN,14));

        JLabel sourceCode = new JLabel("Code and documentation:", JLabel.CENTER);
        sourceCode.setFont(new Font("Helvetica",Font.PLAIN,14));

        JLabel link = new JLabel("<html><a href=\\\"\\\">Code and documentation</a></html>", JLabel.CENTER);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.setFont(new Font("Helvetica",Font.PLAIN,16));
        link.setToolTipText
            ("Click this link to retrieve code and documentation from DropBox");

        JLabel video = new JLabel("<html><a href=\\\"\\\">Watch demo video on YouTube</a></html>", JLabel.CENTER);
        video.setCursor(new Cursor(Cursor.HAND_CURSOR));
        video.setFont(new Font("Helvetica",Font.PLAIN,16));
        video.setToolTipText
            ("Click this link to watch demo video on YouTube");

        JLabel dummy = new JLabel("");

        add(title);
        add(version);
        add(programmer);
        add(email);
        add(link);
        add(video);
        add(dummy);
 
        goDropBox(link);
        goYouTube(video);

        title.     setBounds(5,  0, 330, 30);
        version.   setBounds(5, 30, 330, 20);
        programmer.setBounds(5, 55, 330, 20);
        email.     setBounds(5, 80, 330, 20);
        link.      setBounds(5,105, 330, 20);
        video.     setBounds(5,130, 330, 20);
        dummy.     setBounds(5,155, 330, 20);
    }

    private void goDropBox(JLabel website) {
        website.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/AirDur/CarSharingLP2017"));
                } catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
                }
            }
        });
    }
    private void goYouTube(JLabel video) {
        video.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("http://youtu.be/0ol_PptA7rM"));
                } catch (URISyntaxException | IOException ex) {
                    //It looks like there's a problem
                }
            }
        });
    }
}