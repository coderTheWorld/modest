package FileManage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class FileManagerGUI extends JFrame {
    JMenuBar menuBar = new JMenuBar();
    JMenu menus[] = {new JMenu("FILE Utility")};
    JMenuItem items[] = {new JMenuItem("Rerange Files"), new JMenuItem("Rename Files")};
    JLabel label;
    JTextField dir;
    JButton chooseButton;
    JButton startButton;
    JProgressBar pb;
    Logger logger;

    FileFilter fileFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return false;
            }
            return true;
        }
    };

    MyFile f;

    FileManagerGUI() {
        super("FILE MANAGER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for (JMenu jm : menus)
            menuBar.add(jm);
        menus[0].add(items[0]);
        menus[0].add(items[1]);
        items[0].addActionListener(new RerangeGUI());
        items[1].addActionListener(new RenameGUI());
        JTextField text = new JTextField("Welcome!");

        setJMenuBar(menuBar);
        getContentPane().add(text);
        setSize(333, 120);
        setVisible(true);

        logger = Logger.getLogger("FileManage");
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("file.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        SimpleFormatter sf = new SimpleFormatter();
        fileHandler.setFormatter(sf);
        logger.severe("TEST");

    }

    public static void main(String[] args) {
        System.out.println("122fsf.23432.jpg".replaceAll(".+\\.","\\."));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileManagerGUI();
            }
        });
    }

    public class RerangeGUI implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            label = new JLabel("Choose the diretory you want to rerange:");
            dir = new JTextField(16);
            chooseButton = new JButton("Choose");
            startButton = new JButton("Start");
            pb = new JProgressBar();
            pb.setBackground(Color.white);
            pb.setForeground(Color.red);
            pb.setStringPainted(true);
            chooseButton.addActionListener(new FileChoosee());
            startButton.addActionListener(new FileRerange());

            getContentPane().removeAll();
            getContentPane().add(BorderLayout.NORTH, label);
            getContentPane().add(BorderLayout.WEST, dir);
            getContentPane().add(BorderLayout.CENTER, chooseButton);
            getContentPane().add(BorderLayout.EAST, startButton);
            getContentPane().add(BorderLayout.SOUTH, pb);
            getContentPane().validate();

        }

    }

    public class RenameGUI implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            label = new JLabel("Choose the diretory you want to rename files:");
            dir = new JTextField(16);
            chooseButton = new JButton("Choose");
            startButton = new JButton("Start");
            pb = new JProgressBar();
            pb.setBackground(Color.white);
            pb.setForeground(Color.red);
            pb.setStringPainted(true);
            chooseButton.addActionListener(new FileChoosee());
            startButton.addActionListener(new FileRename());


            getContentPane().removeAll();
            getContentPane().add(BorderLayout.NORTH, label);
            getContentPane().add(BorderLayout.WEST, dir);
            getContentPane().add(BorderLayout.CENTER, chooseButton);
            getContentPane().add(BorderLayout.EAST, startButton);
            //getContentPane().add(BorderLayout.SOUTH, pb);
            getContentPane().validate();

        }

    }

    public class FileChoosee implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = fc.showOpenDialog(FileManagerGUI.this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                String filePath = fc.getSelectedFile().getAbsolutePath();
                dir.setText(filePath);
            }
        }
    }

    public class FileRerange implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            f = new MyFile(dir.getText());


            new Thread(new Runnable() {
                @Override
                public void run() {
                    f.rerange(fileFilter);
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    pb.setIndeterminate(true);
                    startButton.setEnabled(false);
                    try {
                        Thread.sleep(33);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int max = f.listFiles(fileFilter).length;
                    pb.setIndeterminate(false);
                    pb.setMaximum(max);
                    pb.setMinimum(0);
                    pb.setValue(0);
                    while (f.listFiles(fileFilter).length > 0) {
                        int process=max - f.listFiles(fileFilter).length;
                        int percentage=process*100/max;
                        pb.setValue(process);
                        pb.setString(String.valueOf((percentage))+'%');
                        try {
                            Thread.sleep(33);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    pb.setValue(max);
                    pb.setString("100% Complete!");
                    startButton.setEnabled(true);
                }
            }).start();
        }

    }

    public class FileRename implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    startButton.setEnabled(false);
                    f = new MyFile(dir.getText());
                    f.rename(fileFilter);
                    JOptionPane.showMessageDialog(null,"Task Complete!","Info",JOptionPane.INFORMATION_MESSAGE);
                    startButton.setEnabled(true);
                }
            });
        }

    }
}
