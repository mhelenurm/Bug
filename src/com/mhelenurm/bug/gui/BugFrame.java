/*
 * Copyright © 2013 Mark Helenurm
 * 
 * This code is copyrighted by Mark Helenurm.
 * Do not steal this code under the threat of legal
 * prosecution.
 * 
 * If you have suggestions, comments, or requests to
 * borrow code, email me at <mhelenurm@gmail.com>
 */
package com.mhelenurm.bug.gui;

import com.mhelenurm.bug.global.GlobalGFXResources;
import com.mhelenurm.bug.global.GlobalGenInf;
import com.mhelenurm.bug.global.GlobalMan;
import com.mhelenurm.bug.model.Bug;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.Timer;

/**
 * This class is the main frame for the bug application.
 *
 * @author Mark Helenurm
 * @version 1.0
 * @since Jun 25, 2013
 */
public class BugFrame extends JFrame {

	private static final Logger LOG = Logger.getLogger(BugFrame.class.getName());
	private static final long serialVersionUID = 1L;

	private BugDisplay display;
	private final int MARGINSIZE = 30;
	private int JARICNSIZE;

	/**
	 * Initializes BugFrame.
	 */
	public BugFrame() {
		super("Bug");
		GlobalMan.alloc();

		Preferences p = Preferences.userRoot();

		while (p.get("Username", "").isEmpty()) {
			String name = JOptionPane.showInputDialog(null, "Enter your last name: ");
			int opt = JOptionPane.showConfirmDialog(null, "Are you sure your name is \"" + name + "\"?", "Please Confirm", JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.OK_OPTION) {
				p.put("Username", name);
			}
		}
		setLayout(null);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int margin = MARGINSIZE;
		int nrows = 2;
		int ncols = 3;
		int height = (int) Math.min(500, tk.getScreenSize().height * .8);
		int width = (height - 3 * margin) / 2 * 3 + 4 * margin;
		JARICNSIZE = (height - 3 * margin) / 2;
		setSize(width, height);

		setLocation(tk.getScreenSize().width / 2 - width / 2, tk.getScreenSize().height / 2 - (height) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);

		JLabel label = new JLabel("ID: " + p.get("Username", ""));
		label.setForeground(Color.white);
		label.setSize(100, 25);
		label.setLocation(width / 2 - 50, height - 20);
		getContentPane().add(label);

		display = new BugDisplay(p.get("Username", "").hashCode(), width, height);
		display.setSize(width, height);
		display.setLocation(0, 0);
		getContentPane().add(display);
		setBackground(new Color(255, 0, 0, 0));
		setUndecorated(true);
	}

    private class BugDisplay extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
		private static final long serialVersionUID = 1L;

        private Jar[] jars;
        private BufferedImage background;
        boolean zoomedout;
        int zoomedtoward;
        private Timer t;
        private JButton backbutton;
        private JButton breedbutton;
        private JButton killbutton;
        private JButton freezebutton;
        private Image[] jarthumbs;
        private int mx, my;
        private JPopupMenu popup;
        private JMenuItem[] menuitems;

		@SuppressWarnings("LeakingThisInConstructor")
		BugDisplay(int hash, int width, int height) {
			super();
            setLayout(null);
            jars = new Jar[6];
            for (int i = 0; i < 6; i++) {
                jars[i] = new Jar();
            }
            zoomedout = true;
            //this block
            Random generator = new Random(hash);
            int[] map = {0, 1, 2, 3, 4, 5};
            for (int i = 0; i < 100; i++) {
                int p1 = generator.nextInt(map.length);
                int p2 = generator.nextInt(map.length);
				int tmp = map[p1];
                map[p1] = map[p2];
				map[p2] = tmp;
            }
            GlobalGenInf.setMap(map);

            GlobalGenInf.mapdists = new int[2];
            GlobalGenInf.mapdists[0] = generator.nextInt(50);
            GlobalGenInf.mapdists[1] = generator.nextInt(50);


            GlobalGFXResources.init();



            //generator = new Random();
            boolean[] charset1 = {generator.nextBoolean(), generator.nextBoolean(), generator.nextBoolean(), generator.nextBoolean(), generator.nextBoolean(), generator.nextBoolean()};
            boolean[] charset2 = {!charset1[0], !charset1[1], !charset1[2], !charset1[3], !charset1[4], !charset1[5]};
            if (charset1[0] && charset1[5]) {
                charset1[5] = false;
                if (charset2[5] == false) {
                    charset2[5] = true;
                }
            }


            boolean[] charset3 = {false, false, false, false, false, false};
            boolean[] charset4 = {false, false, false, false, false, false};
            jars[0].addBug(new Bug(charset1, charset2));
            jars[0].addBug(new Bug(charset3, charset4));
            jars[0].breed();
            jars[0].render();

            BufferedImage background2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gfx = background2.createGraphics();
            gfx.drawImage(GlobalGFXResources.background.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
            AffineTransform at = new AffineTransform();
            at.translate((double) height / 14.0, (double) height / 14.0);
            at.rotate(Math.PI * 3.0 / 2.0);
            double scale = (double) height / 7.0 / (double) GlobalGFXResources.goldcorner.getHeight();
            at.scale(scale, scale);
            at.translate(-GlobalGFXResources.goldcorner.getWidth() / 2, -GlobalGFXResources.goldcorner.getHeight() / 2);
            gfx.drawImage(GlobalGFXResources.goldcorner, at, null);

            at = new AffineTransform();
            at.translate((double) width - (double) height / 14.0, (double) height / 14.0);
            at.rotate(Math.PI * 0.0 / 2.0);
            at.scale(scale, scale);
            at.translate(-GlobalGFXResources.goldcorner.getWidth() / 2, -GlobalGFXResources.goldcorner.getHeight() / 2);
            gfx.drawImage(GlobalGFXResources.goldcorner, at, null);

            at = new AffineTransform();
            at.translate((double) height / 14.0, (double) height - (double) height / 14.0);
            at.rotate(Math.PI * 2.0 / 2.0);
            at.scale(scale, scale);
            at.translate(-GlobalGFXResources.goldcorner.getWidth() / 2, -GlobalGFXResources.goldcorner.getHeight() / 2);
            gfx.drawImage(GlobalGFXResources.goldcorner, at, null);

            at = new AffineTransform();
            at.translate((double) width - (double) height / 14.0, (double) height - (double) height / 14.0);
            at.rotate(Math.PI * 1.0 / 2.0);
            at.scale(scale, scale);
            at.translate(-GlobalGFXResources.goldcorner.getWidth() / 2, -GlobalGFXResources.goldcorner.getHeight() / 2);
            gfx.drawImage(GlobalGFXResources.goldcorner, at, null);

            background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D ggfx = background.createGraphics();
            ggfx.drawImage(GlobalGFXResources.wood.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
            at = new AffineTransform();
            at.translate((double) width / 2, (double) height / 2);
            at.scale(.95, .95);
            at.translate(-background2.getWidth() / 2, -background2.getHeight() / 2);
            ggfx.drawImage(background2, at, null);

            setBackground(new Color(255, 0, 0, 0));
            t = new Timer(1000 / 30, this);

            jarthumbs = new Image[6];
            for (int i = 0; i < 6; i++) {
                jars[i].render();
                jarthumbs[i] = jars[i].getBuffer().getScaledInstance(JARICNSIZE, JARICNSIZE, BufferedImage.SCALE_AREA_AVERAGING);
            }

            int compwidth = (width - jars[zoomedtoward].getBuffer().getWidth()) / 2;

            int buttonheight = 50;
            int buttonwidth = 50;
            
            backbutton = new JButton();
            backbutton.setIcon(new ImageIcon((new ImageIcon(GlobalMan.getResourcePath("backbutton.png"))).getImage().getScaledInstance(buttonwidth, buttonheight, Image.SCALE_SMOOTH)));
            backbutton.setSize(buttonwidth, buttonheight);
            backbutton.setOpaque(false);
            backbutton.setContentAreaFilled(false);
            backbutton.setBorderPainted(false);
            backbutton.setLocation((compwidth - buttonwidth) / 2, 20);

            backbutton.addActionListener(new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent e) {
                    if (!zoomedout) {
                        zoom(false, -1);
                    }
                }
            });
            backbutton.setVisible(false);
            add(backbutton);

            breedbutton = new JButton();
            breedbutton.setIcon(new ImageIcon((new ImageIcon(GlobalMan.getResourcePath("breedbutton.png"))).getImage().getScaledInstance(buttonwidth * 2, buttonheight, Image.SCALE_SMOOTH)));
            breedbutton.setSize(buttonwidth * 2, buttonheight);
            breedbutton.setOpaque(false);
            breedbutton.setContentAreaFilled(false);
            breedbutton.setBorderPainted(false);
            breedbutton.setLocation((compwidth - buttonwidth * 2) / 2, 20 + (buttonheight + 20));

            breedbutton.addActionListener(new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent e) {
                    if (!zoomedout) {
                        jars[zoomedtoward].breed();
                    }
                }
            });
            breedbutton.setVisible(false);
            add(breedbutton);

            killbutton = new JButton();
            killbutton.setIcon(new ImageIcon((new ImageIcon(GlobalMan.getResourcePath("killbutton.png"))).getImage().getScaledInstance(buttonwidth * 2, buttonheight, Image.SCALE_SMOOTH)));
            killbutton.setSize(buttonwidth * 2, buttonheight);
            killbutton.setOpaque(false);
            killbutton.setContentAreaFilled(false);
            killbutton.setBorderPainted(false);
            killbutton.setLocation((compwidth - buttonwidth * 2) / 2, 20 + (buttonheight + 20) * 2);

            killbutton.addActionListener(new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent e) {
                    if (!zoomedout) {
                        jars[zoomedtoward].kill(!(zoomedtoward == 0));
                    }
                }
            });
            killbutton.setVisible(false);
            add(killbutton);

            //System.out.println(new File(GlobalMan.resourcePath + "freezebutton.png").exists());
            //System.out.println(new File(GlobalMan.resourcePath + "freezebutton.png").getAbsolutePath());
            freezebutton = new JButton();
            freezebutton.setIcon(new ImageIcon((new ImageIcon(GlobalMan.getResourcePath("freezebutton.png"))).getImage().getScaledInstance(buttonwidth * 2, buttonheight, Image.SCALE_SMOOTH)));
            freezebutton.setSize(buttonwidth * 2, buttonheight);
            freezebutton.setOpaque(false);
            freezebutton.setContentAreaFilled(false);
            freezebutton.setBorderPainted(false);
            freezebutton.setLocation((compwidth - buttonwidth * 2) / 2, 20 + (buttonheight + 20) * 3);

            freezebutton.addActionListener(new ActionListener() {
				@Override
                public void actionPerformed(ActionEvent e) {
                    if (!zoomedout) {
                        jars[zoomedtoward].toggleFreeze();
                    }
                }
            });
            freezebutton.setVisible(false);
            add(freezebutton);

            popup = new JPopupMenu();
            JLabel m = new JLabel("Move Bug To:");
            m.setFont(new Font(this.getFont().getName(), Font.BOLD, this.getFont().getSize()));

            popup.add(m);
            popup.add(new JSeparator());
            menuitems = new JMenuItem[6];
            for (int i = 1; i <= 6; i++) {
                menuitems[i - 1] = new JMenuItem("Container #" + i);
                menuitems[i - 1].setName("" + (i - 1));
                popup.add(menuitems[i - 1]);
                menuitems[i - 1].addActionListener(new ActionListener() {
					@Override
                    public void actionPerformed(ActionEvent e) {
                        Bug b = jars[zoomedtoward].getSelected();
                        if (b != null) {
                            int newind = Integer.parseInt(((JMenuItem) e.getSource()).getName());
                            jars[newind].addBug(b);
                            jars[newind].render();
                            jarthumbs[newind] = jars[newind].getBuffer().getScaledInstance(JARICNSIZE, JARICNSIZE, BufferedImage.SCALE_AREA_AVERAGING);
                            jars[zoomedtoward].removeSelected();
                        }
                    }
                });

            }
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void zoom(boolean in, int jarnum) {
            if (!in) {
                //zooming back out
                zoomedout = true;
                jarthumbs[zoomedtoward] = jars[zoomedtoward].getBuffer().getScaledInstance(JARICNSIZE, JARICNSIZE, BufferedImage.SCALE_AREA_AVERAGING);

                backbutton.setVisible(false);
                killbutton.setVisible(false);
                breedbutton.setVisible(false);
                freezebutton.setVisible(false);
                t.stop();
                repaint();
            } else {
                //zooming in
                zoomedout = false;
                zoomedtoward = jarnum;
                backbutton.setVisible(true);
                breedbutton.setVisible(true);
                killbutton.setVisible(true);
                freezebutton.setVisible(true);
                t.start();
            }
        }

		@Override
        public void paintComponent(Graphics gg) {
            Graphics2D g = (Graphics2D) gg;
            g.drawImage(background, 0, 0, null);
            if (zoomedout) {
                g.setColor(Color.white);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        g.drawImage(jarthumbs[i * 3 + j], MARGINSIZE * (j + 1) + j * JARICNSIZE, MARGINSIZE * (i + 1) + i * JARICNSIZE, null);
                        g.drawString("Petri Dish #" + (i * 3 + j + 1), MARGINSIZE * (j + 1) + j * JARICNSIZE + jarthumbs[i * 3 + j].getWidth(null) / 2 - g.getFontMetrics().stringWidth("Petri Dish #" + (i * 3 + j + 1)) / 2, MARGINSIZE * (i + 1) + i * JARICNSIZE + JARICNSIZE);
                    }
                }

            } else {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, (getWidth() - jars[zoomedtoward].getBuffer().getWidth()) / 2, getHeight());
                g.drawImage(jars[zoomedtoward].getBuffer(), getWidth() / 2 - jars[zoomedtoward].getBuffer().getWidth() / 2, getHeight() / 2 - jars[zoomedtoward].getBuffer().getHeight() / 2, null);

            }
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            jars[zoomedtoward].timestep(1.0 / 30.0);
            jars[zoomedtoward].render();
            repaint();
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            if (jars[zoomedtoward].getSelected() != null) {
                jars[zoomedtoward].deselect();
            }

            if (!zoomedout & (me.getButton() == MouseEvent.BUTTON3 || me.isAltDown())) {

                if (jars[zoomedtoward].select(me.getX() - (getWidth() / 2 - jars[zoomedtoward].getBuffer().getWidth() / 2), me.getY() - (getHeight() / 2 - jars[zoomedtoward].getBuffer().getHeight() / 2))) {
                    popup.show(this, me.getX(), me.getY());
                    for (int i = 0; i < 6; i++) {
                        menuitems[i].setEnabled(true);
                    }
                    menuitems[zoomedtoward].setEnabled(false);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            if (zoomedout) {
                boolean found = false;
                int num = -1;
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        int jnum = i * 3 + j;
                        double size = JARICNSIZE / 2;
                        double xcoord = MARGINSIZE * (j + 1) + j * JARICNSIZE + JARICNSIZE / 2;
                        double ycoord = MARGINSIZE * (i + 1) + i * JARICNSIZE + JARICNSIZE / 2;
						double mousex = me.getX();
						double mousey = me.getY();
						if (Math.pow(xcoord - mousex, 2) + Math.pow(ycoord - mousey, 2) < size * size) {
                            found = true;
                            num = jnum;
                        }
                    }
                }
                if (found) {
                    zoom(true, num);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            mx = me.getX();
            my = me.getY();
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            mx = me.getX();
            my = me.getY();
        }
    }
}
