package geoimbib.Views.JDialogs;

import geoimbib.Controlers.C_ControlDialogSerie;
import geoimbib.Controlers.C_ControlDialogTouch;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ravier on 24/01/2016.
 * Classe &eacute;tendue de JDialog, r&eacute;cup&egrave;re la masse
 */
public class V_jDialogMasse extends JDialog {

    private final JFrame parent;
    private ActionListener aL;
    private C_ControlDialogSerie c_controlDialogSerie;
    private C_ControlDialogTouch c_controlDialogTouch;

    private JLabel jlabelsaisieBalance;

    private JLabel jlabelDoubleValeurBalance;
    private JTextField jtextfieldMasseManu;

    private JPanel jpanelButtons;
    private JButton jbuttonOk;
    private JButton jbuttonRetour;

    private int idCar;

    private V_jlabelDoubleValeurBalanceThread  threadJlabelValeurBalance;

    /**
     * Constructeur de la classe V_jDialogMasse
     * @param parent
     * @param title
     * @param modal
     * @param aL
     * @param idCar
     */
    public V_jDialogMasse(JFrame parent, String title, boolean modal, ActionListener aL, int idCar) {
        super(parent, title, modal);

        this.parent = parent;

        this.setSize(500, 250);
        this.setLocationRelativeTo(null);
        this.setResizable(false);


        this.aL =aL;
        if (aL instanceof C_ControlDialogSerie){
            this.c_controlDialogSerie = (C_ControlDialogSerie)aL;
            this.c_controlDialogSerie.setV_jDialogNouvelleSerie(this);
        }else{
            this.c_controlDialogTouch = (C_ControlDialogTouch) aL;
            this.c_controlDialogTouch.setV_jDialogMasse(this);
        }

        this.idCar = idCar;

        initComposants();


        this.setVisible(true);
    }

    private void initComposants() {
        JPanel jpcomposants = new JPanel(new GridLayout(3,2));
        Border paddingjpcomposants = BorderFactory.createEmptyBorder(10,20,10,100);
        jpcomposants.setBorder(paddingjpcomposants);



        jlabelDoubleValeurBalance = new JLabel("0");
        jlabelsaisieBalance = new JLabel("\"Espace\" pour valider :");
        jpcomposants.add(jlabelsaisieBalance);
        jpcomposants.add(jlabelDoubleValeurBalance);

        jpcomposants.add(new JLabel("\"m\" pour une saisie manuelle"));
        jpcomposants.add(new JLabel(""));


        jtextfieldMasseManu = new JTextField("0");
        jtextfieldMasseManu.setVisible(false);
        jbuttonOk = new JButton("Ok");
        jbuttonOk.setVisible(false);
        jbuttonOk.addActionListener(this.aL);


        jpcomposants.add(jtextfieldMasseManu);
        jpcomposants.add(jbuttonOk);

        jpanelButtons = new JPanel(new BorderLayout());
        jpanelButtons.setVisible(false);
        jbuttonRetour = new JButton("Annuler man");
        jbuttonRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanWindow();
            }
        });
        jpanelButtons.add(jbuttonRetour);
        Border paddingjpanelbutton = BorderFactory.createEmptyBorder(20,100,20,100);
        jpanelButtons.setBorder(paddingjpanelbutton);

        this.getContentPane().add(jpanelButtons, BorderLayout.SOUTH);
        this.getContentPane().add(jpcomposants, BorderLayout.CENTER);

        threadJlabelValeurBalance = new V_jlabelDoubleValeurBalanceThread(jlabelDoubleValeurBalance);
        threadJlabelValeurBalance.start();

        addControler();
    }

    private void cleanWindow() {
        getContentPane().removeAll();
        initComposants();
        validate();
        this.requestFocus();
    }

    private void addControler() {
        if (aL instanceof C_ControlDialogSerie)
            this.addKeyListener(this.c_controlDialogSerie);
        else
            this.addKeyListener(this.c_controlDialogTouch);
    }

    public void setManuel() {
        /*
        * On rend visible les boutons manuels
        * */
        jtextfieldMasseManu.setVisible(true);
        jbuttonOk.setVisible(true);
        jpanelButtons.setVisible(true);
        /*
        * On rend invisible les boutons pour la balance
        * */
        jlabelDoubleValeurBalance.setVisible(false);
        jlabelsaisieBalance.setVisible(false);

        this.removeKeyListener(this.c_controlDialogSerie);
    }

    public JButton getJButtonOkManuel() {
        return jbuttonOk;
    }

    public String getJLabelBalance(){
        return jlabelDoubleValeurBalance.getText();
    }

    public String getJtextfieldValMan() {
        return jtextfieldMasseManu.getText();
    }

    public int getIdCar(){
        return idCar;
    }

    public JLabel getJlabelDoubleValeurBalance() {
        return jlabelDoubleValeurBalance;
    }

    public V_jlabelDoubleValeurBalanceThread getThreadJlabelValeurBalance() {
        return threadJlabelValeurBalance;
    }
}
