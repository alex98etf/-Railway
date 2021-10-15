package gui;

import main.Zeljeznica;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;



public class KretanjeWindow extends JFrame{
    private static final Logger LOGGER = Logger.getLogger(KretanjeWindow.class.getName());

    private JPanel backPanel;
    private JList<String> kretanjeList;
    private JTextArea kretanjeTextArea;

    public KretanjeWindow(){
        super("ŽELJEZNICA - Kretanje Svih Kompozicija");
        this.setContentPane(backPanel);
        this.setLocationRelativeTo(null);
        this.setSize(750,450);
        ImageIcon icona = new ImageIcon("src/gui/icon/noun_Train_743171_ss.png");
        this.setIconImage(icona.getImage());

        DefaultListModel<String> listModel = new DefaultListModel<>();

        File folder = new File(Zeljeznica.dirKretanja);
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        for (File file : listOfFiles)
            if (file.isFile())
                listModel.addElement(file.getName());

        kretanjeList.setModel(listModel);
        kretanjeList.addListSelectionListener(e -> prikazLinije());

        this.setResizable(true);
        this.setVisible(true);
    }

    private void prikazLinije() {

        if(kretanjeList.getSelectedIndex() != -1)
            kretanjeTextArea.setText("");

        try {
            FileInputStream stream = new FileInputStream(Zeljeznica.dirKretanja + File.separator + kretanjeList.getSelectedValue() );
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder text = new StringBuilder();

            while(reader.ready())
                text.append(reader.readLine()).append("\n");

            kretanjeTextArea.setText(text.toString());

            stream.close();
            reader.close();

        } catch (Exception exception) {
            LOGGER.warning(exception.fillInStackTrace().toString());
        }
    }
}

