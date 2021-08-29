package gui;

import main.Zeljeznica;
import model.put.Putevi;
import model.vozila.Vozilo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.logging.Logger;

public class MainWindow extends JFrame{
    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    private static final int primaryColor = 0x5370E8;
    private static final int secondaryColor = 0xE7EFFC;
    private JPanel backPanel;
    private JPanel buttonPanel;
    private JPanel matrixPanel;
    private JButton runButton;
    private JButton zaustaviButton;
    private JButton kretanjaButton;
    private JTable matrix;
    private JButton pauzirajButton;
    private JPanel mainPanel;
    private final JMenuItem about = new JMenuItem("About");
    private static final String mapFile = "src/map.txt";

    public MainWindow(){
        super("ŽELJEZNICA");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(backPanel);
        this.setLocationRelativeTo(null);

        matrix.setModel(new DefaultTableModel(30,30));
        matrix.setDefaultRenderer(Object.class, new ColorRenderer());

        runButton.setBorder(BorderFactory.createLineBorder(new Color(primaryColor),2, true));
        pauzirajButton.setBorder(BorderFactory.createLineBorder(new Color(primaryColor),2, true));
        kretanjaButton.setBorder(BorderFactory.createLineBorder(new Color(primaryColor), 2, true));
        zaustaviButton.setBorder(BorderFactory.createLineBorder(new Color(primaryColor),2,true));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(about);
        this.setJMenuBar(menuBar);

        runButton.addActionListener(e -> runSimulation());
        pauzirajButton.addActionListener(e -> pauzirajSimulation());
        zaustaviButton.addActionListener(e -> endSimulation());
        kretanjaButton.addActionListener(e -> prikazKretanja());
        about.addActionListener(e -> aboutMeni());

        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setResizable(true);
        this.setVisible(true);
    }

    private void aboutMeni() {
        JOptionPane.showMessageDialog(about,"<html>Programski jezici 2<br/>Zeljeznica Željeznice<br/>by Aleksandar Bešlić</html>");
    }

    private void prikazKretanja() {
        new KretanjeWindow();
    }

    private void endSimulation() {
        Zeljeznica.start = false;
    }

    private void pauzirajSimulation() {
        Zeljeznica.setPauza();
    }

    private void runSimulation() {
        Zeljeznica.start = true;
    }

    static class ColorRenderer extends DefaultTableCellRenderer {
        private String line;

        public ColorRenderer() {
            try {
                FileInputStream stream = new FileInputStream(mapFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                line = reader.readLine();

                stream.close();
                reader.close();

            } catch (Exception exception) {
                LOGGER.warning(exception.fillInStackTrace().toString());
            }
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            /*char cell = line.charAt(Zeljeznica.MAP_SIZE * row + column);
            if(cell == 'a')
                setBackground(new Color(primaryColor));
            else if (cell == 'v')
                setBackground(new Color(secondaryColor));
            else if (cell == 's')
                setBackground(Color.darkGray);
            else if (cell == 'p')
                setBackground(Color.black);
            else
                setBackground(table.getBackground());*/

            if (Zeljeznica.background[row][column] instanceof Putevi) {
                String vrsta = ((Putevi) Zeljeznica.background[row][column]).getVrsta();
                if (Putevi.KOLOVOZ.equals(vrsta))
                    setBackground(new Color(primaryColor));
                else if (Putevi.PRUGA.equals(vrsta))
                    setBackground(new Color(secondaryColor));
                else if (Putevi.STANICA.equals(vrsta))
                    setBackground(Color.darkGray);
                else if (Putevi.PRELAZ.equals(vrsta))
                    setBackground(Color.black);
                else
                    setBackground(table.getBackground());
            }

            if(Zeljeznica.background[row][column] instanceof Vozilo)
                setBackground(new Color(primaryColor));

            if(Zeljeznica.background[row][column] == null)
                setBackground(table.getBackground());

                return this;
        }
    }
}
