package gui;

import main.Zeljeznica;
import model.lokomotive.Lokomotiva;
import model.put.Putevi;
import model.vagoni.Namjenski;
import model.vagoni.Tertni;
import model.vagoni.Vagon;
import model.vozila.Auto;
import model.vozila.Kamion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.logging.Logger;

/**
 * @author Alex
 */
public class MainWindow extends JFrame{
    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    private static final int primaryColor = 0x7984EE /*0x5370E8*/;
    private static final int secondaryColor = 0xA9D2FF /*0xE7EFFC*/;
    private static final int ssecondaryColor = 0xd2f6fc;
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
    private final static String[] colName = new String[30];

    public MainWindow(){
        super("ŽELJEZNICA");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(backPanel);
        this.setLocationRelativeTo(null);

        ImageIcon icona = new ImageIcon("src/gui/icon/noun_Train_743171_ss.png");
        this.setIconImage(icona.getImage());

        matrix.setModel(new DefaultTableModel(30,30));
        matrix.setDefaultRenderer(Object.class, new ColorRenderer());
        matrix.setRowHeight(20);


       /* runButton.setBorder(BorderFactory.createLineBorder(new Color(secondaryColor),2,true));
        pauzirajButton.setBorder(BorderFactory.createLineBorder(new Color(ssecondaryColor),7, true));
        kretanjaButton.setBorder(BorderFactory.createLineBorder(new Color(ssecondaryColor), 7, true));
        zaustaviButton.setBorder(BorderFactory.createLineBorder(new Color(ssecondaryColor),7,true));*/

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("help");
        menu.add(about);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        pauzirajButton.setEnabled(false);

        // Coming soon
        zaustaviButton.setVisible(false);
        zaustaviButton.setEnabled(false);


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
        JOptionPane.showMessageDialog(about,"<html><center>Programski jezici 2<br/>Željeznice<br/>by Aleksandar Bešlić</center></html>");
    }

    private void prikazKretanja() {
        new KretanjeWindow();
    }

    private void endSimulation() { Zeljeznica.zaustavi(); runButton.setEnabled(true);}

    private void pauzirajSimulation() { Zeljeznica.setPauza();}

    private void runSimulation() {
        Zeljeznica.runSimulacija(true);
        runButton.setEnabled(false);
        pauzirajButton.setEnabled(true);
    }

    public void refresh(){
        // RTX ON
        matrixPanel.revalidate();
        matrixPanel.repaint();
    }

    static class ColorRenderer extends DefaultTableCellRenderer {
        private final ImageIcon auto = new ImageIcon("src/gui/icon/PinClipart.com_minimalist-clipart_1520103_car.png");
        private final ImageIcon kamion = new ImageIcon("src/gui/icon/PinClipart.com_minimalist-clipart_1520103_s.png");
        private final ImageIcon lokomotiva = new ImageIcon("src/gui/icon/noun_Train_367197_s.png");
        private final ImageIcon vagonTeretni = new ImageIcon("src/gui/icon/noun_Train_900347_s.png");
        private final ImageIcon vagonNamjenski = new ImageIcon("src/gui/icon/noun_wagon_1009518_s.png");
        private final ImageIcon vagon = new ImageIcon("src/gui/icon/noun_Train_900373_s.png");
        private final ImageIcon cekaonica = new ImageIcon("src/gui/icon/197-1976118_train-station-train-station-icon-line_s.png");
        public ColorRenderer() {}

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Postavljanje pozadine
            if (Zeljeznica.background[row][column] instanceof Putevi) {
                String vrsta = ((Putevi) Zeljeznica.background[row][column]).getVrsta();
                if (Putevi.KOLOVOZ.equals(vrsta))
                    setBackground(new Color(primaryColor));
                else if (Putevi.PRUGA.equals(vrsta))
                    setBackground(new Color(secondaryColor));
                else if (Putevi.STANICA.equals(vrsta))
                    setBackground(Color.lightGray);
                else if (Putevi.PRELAZ.equals(vrsta))
                    setBackground(Color.black);
                else if (Putevi.CEKAONICA.equals(vrsta))
                    setBackground(Color.lightGray);
                else
                    setBackground(table.getBackground());
            }


            if (Zeljeznica.background[row][column] == null)
                setBackground(table.getBackground());

            // Postavljanje Ikona
            if (Zeljeznica.map[row][column] != null) {
                if (Zeljeznica.map[row][column] instanceof Auto) {
                    setIcon(auto);
                    setBackground(new Color(ssecondaryColor));
                }
                else if(Zeljeznica.map[row][column] instanceof Kamion){
                    setIcon(kamion);
                    setBackground(new Color(ssecondaryColor));
                }
                else if(Zeljeznica.map[row][column] instanceof Lokomotiva){
                    setIcon(lokomotiva);
                    setBackground(new Color(ssecondaryColor));
                }
                else if(Zeljeznica.map[row][column] instanceof Vagon){
                    if(Zeljeznica.map[row][column] instanceof Namjenski)
                        setIcon(vagonNamjenski);
                    else if(Zeljeznica.map[row][column] instanceof Tertni)
                        setIcon(vagonTeretni);
                    else
                     setIcon(vagon);

                    setBackground(new Color(ssecondaryColor));
                }
                else
                    setIcon(null);
            }
            else{
            /*if (Zeljeznica.background[row][column] instanceof Putevi) {
                String vrsta = ((Putevi) Zeljeznica.background[row][column]).getVrsta();
                if (Putevi.CEKAONICA.equals(vrsta)) {
                    setBackground(Color.red);
                    setIcon(cekaonica);
                }
                else
                    setIcon(null);

            }
            else
                setIcon(null);*/
                setIcon(null);
            }


            return this;
        }
    }
}
