package geoimbib.Controlers;

import geoimbib.Models.M_Carotte;
import geoimbib.Models.M_Mesure;
import geoimbib.Models.M_Serie;
import geoimbib.Models.M_armoFile;
import geoimbib.Views.JDialogs.*;
import geoimbib.Views.JPanels.V_JPanelMainRight;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Contr&ocirc;leur pour la modification d'une s&eacute;rie, impl&eacute;mente KeyListener et Action Listener
 */
public class C_ControlDialogTouch implements ActionListener, KeyListener {
    private V_JPanelMainRight v_jPanelMainRight;
    private V_JDialogTouch v_jDialogTouch;
    private V_JDialogInfoFinFillEchant v_JDialogInfoFinFillEchant;
    private V_JDialogHeure v_jDialogHour;
    private V_jDialogMasse v_jDialogMasse;
    private V_JDialogFrange v_jDialogFrange;
    private V_JDialogRecap v_JDialogRecap;

    private ArrayList<String> arrayListName;
    private int nbEchantToUpdate;
    private ArrayList<M_Carotte> arrayListM_carotte;
    private boolean fastMesures;

    private Calendar calendarnewserie = null;
    private String tmpHour;
    private boolean firstMesure = true;


    public C_ControlDialogTouch(V_JPanelMainRight v_jPanelMainRight){
        this.v_jPanelMainRight = v_jPanelMainRight;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        * Modification de s&eacute;rie
        * */

        if (e.getSource() == v_jDialogTouch.getButonNext()){
            try {
                arrayListName = new ArrayList<>();
                for (int i=0; i<v_jDialogTouch.getNameEchant().length; ++i){
                    if (v_jDialogTouch.getJCHeckbokAtIndex(i)){
                        arrayListName.add(v_jDialogTouch.getNameEchant()[i]);
                        System.out.println(v_jDialogTouch.getNameEchant()[i]);
                    }
                }
                nbEchantToUpdate = arrayListName.size();

                arrayListM_carotte = new ArrayList<>();
                for (int i = 0; i<nbEchantToUpdate; ++i)
                    arrayListM_carotte.add(new M_Carotte());

                calendarnewserie = Calendar.getInstance();

                v_jDialogTouch.dispose();

                v_jPanelMainRight.displayInfoFinFillEchant(this);


            }catch (Exception exception) {
                v_jDialogTouch.displayJDialogErrorUpdateSet();
            }
        }


        else if (e.getSource() == v_JDialogInfoFinFillEchant.getJButtonNext()){
            fastMesures = v_JDialogInfoFinFillEchant.getStateJCheckBoxFastMesure();

            v_jPanelMainRight.loopAcquisitionMasse(nbEchantToUpdate, fastMesures, arrayListM_carotte, this);

            v_JDialogInfoFinFillEchant.dispose();
        }

        /**
         * DEBUT DE LOOPACQUISITION
         */
        else if (e.getSource()==v_jDialogHour.getButtonOk()){
            try{
                tmpHour = v_jDialogHour.getJTextfield();
                v_jDialogHour.dispose();
            }catch(Exception e2){
                v_jPanelMainRight.displayJDialogErrorinputNewSerie();
            }
        }

        else if (e.getSource() == v_jDialogMasse.getJButtonOkManuel()){
            try {
                if (firstMesure){
                    for (int i= 0; i<nbEchantToUpdate; ++i)
                        arrayListM_carotte.get(i).setListMesures(new ArrayList<M_Mesure>());
                    firstMesure = false;
                }

                //r&eacute;cup idechantillon
                int id = v_jDialogMasse.getIdCar();

                //R&eacute;cup de la masse
                double valMasse = Double.parseDouble(v_jDialogMasse.getJtextfieldValMan());

                //cr&eacute;ation de la mesure et on ajoute que la masse pour le moment
                M_Mesure m_mesure = new M_Mesure(valMasse);

                //on ajoute la mesure &agrave; arraylist de l'idcar
                arrayListM_carotte.get(id).getListMesures().add(m_mesure);


                v_jDialogMasse.dispose();
            }catch (Exception exc){
                System.out.println(exc);
                v_jPanelMainRight.displayJDialogErrorinputNewSerie();
            }
        }

        else if (e.getSource() == v_jDialogFrange.getbuttonOk()){
            try{
                int id = v_jDialogFrange.getIdEchant();
                arrayListM_carotte.get(id).getListMesures().get(arrayListM_carotte.get(id).getListMesures().size()-1).setHauteurFrangeHumide(Double.parseDouble(v_jDialogFrange.getFrangeHu().replace(",", ".")));
                v_jDialogFrange.dispose();
            }catch (Exception e1){
                System.out.println(e1);
                v_jPanelMainRight.displayJDialogErrorinputNewSerie();
            }
        }

        else if (e.getSource() == v_JDialogRecap.getButtonVal()){
            try{
                v_JDialogRecap.dispose();
            }catch (Exception e1){
                System.out.println(e1);
                v_jPanelMainRight.displayJDialogErrorinputNewSerie();
            }
        }

        else if (e.getSource() == v_JDialogRecap.getButtonGraph()) {
            M_Carotte carotte1 = v_JDialogRecap.getCarotte(); //nouvelles mesures.

            String path = v_JDialogRecap.getMainWindow().getJPanelMainLeft().getPathCurrentSet()+ File.separator + arrayListName.get(v_JDialogRecap.getIdCarotte());
            M_Carotte carotte0 = M_armoFile.getINSTANCE().getCarotte(path);

            for (int i = 0; i<carotte1.getListMesures().size(); ++i){
                carotte0.getListMesures().add(carotte1.getListMesures().get(i));
            }

            new V_JDialogGraph(
                    this.v_jPanelMainRight.getV_mainWindow(),
                    "Graphique",
                    true,
                    carotte0,
                    v_jPanelMainRight
            );
        }
    }

    public void setV_jDialogTouch(V_JDialogTouch v_jDialogTouch){this.v_jDialogTouch=v_jDialogTouch;}

    public void setV_jDialogInfoFillEchant(V_JDialogInfoFinFillEchant v_JDialogInfoFinFillEchant) {
        this.v_JDialogInfoFinFillEchant = v_JDialogInfoFinFillEchant;
    }

    public void setV_jDialogHour(V_JDialogHeure v_jDialogHour) {
        this.v_jDialogHour = v_jDialogHour;
    }

    public Calendar getCalendarSerie() {
        return calendarnewserie;
    }

    public void setV_jDialogMasse(V_jDialogMasse v_jDialogMasse) {
        this.v_jDialogMasse = v_jDialogMasse;
    }

    public void setV_jDialogNouvelleSerie(V_JDialogRecap v_jDialogRecap) {
        this.v_JDialogRecap = v_jDialogRecap;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 32){

                if (firstMesure){
                    for (int i= 0; i<nbEchantToUpdate; ++i)
                        arrayListM_carotte.get(i).setListMesures(new ArrayList<M_Mesure>());
                    firstMesure = false;
                }
            try {
                //r&eacute;cup idechantillon
                int id = v_jDialogMasse.getIdCar();

                //R&eacute;cup de la masse
                double valMasse = Double.parseDouble(v_jDialogMasse.getJlabelDoubleValeurBalance().getText());
                v_jDialogMasse.getThreadJlabelValeurBalance().stopThread();

                //cr&eacute;ation de la mesure et on ajoute que la masse pour le moment
                M_Mesure m_mesure = new M_Mesure(valMasse);

                //on ajoute la mesure &agrave; arraylist de l'idcar
                arrayListM_carotte.get(id).getListMesures().add(m_mesure);


                v_jDialogMasse.dispose();
            }catch (Exception exc){
                System.out.println(exc);
                v_jPanelMainRight.displayJDialogErrorinputNewSerie();
            }
        }
        else if (e.getKeyCode() == 77) {
            v_jDialogMasse.getThreadJlabelValeurBalance().stopThread();
            v_jDialogMasse.setManuel();
        }
    }

    public void setV_jDialogFrange(V_JDialogFrange v_jDialogFrange) {
        this.v_jDialogFrange = v_jDialogFrange;
    }

    public ArrayList<M_Carotte> getArrayCarottes() {
        return arrayListM_carotte;
    }

    public ArrayList<String> getArrayNameUpdate() {
        return arrayListName;
    }

    /**
     * M&eacute;thode d'assignation de l'heure pour chaque mesure
     */
    public void loopAssignHourArrayMesure(int ii) {
        try {
            int index;
            String date = "";
            Date first;
            Date second;
            String path;
            for (int i = 0; i<arrayListM_carotte.size(); ++i){
                index = arrayListM_carotte.get(ii).getListMesures().size()-1;
                date = arrayListM_carotte.get(ii).getListMesures().get(index).getDateMesure();
                arrayListM_carotte.get(ii).getListMesures().get(index).setHeureMesure(date, tmpHour);

                for (int y=0; y<arrayListM_carotte.get(i).getListMesures().size(); ++y){

                    if (y==0) {
                        path = v_jPanelMainRight.getV_mainWindow().getJPanelMainLeft().getPathCurrentSet()+ File.separator + arrayListName.get(ii);

                        first = M_armoFile.getINSTANCE().getLastDateEchant(path);
                        second = arrayListM_carotte.get(ii).getListMesures().get(y).getDateHeure().getTime();
                        double diff = getDiffTimeTwoEchant(second, first);
                        arrayListM_carotte.get(ii).getListMesures().get(y).setTemps(diff+M_armoFile.getINSTANCE().getLastTimeEchant(path));
                    }
                    else{
                        Date h1 = arrayListM_carotte.get(ii).getListMesures().get(y).getDateHeure().getTime();
                        Date h2 = arrayListM_carotte.get(ii).getListMesures().get(y-1).getDateHeure().getTime();
                        double diff = getDiffTimeTwoEchant(h1, h2);
                        arrayListM_carotte.get(ii).getListMesures().get(y).setTemps(diff+arrayListM_carotte.get(ii).getListMesures().get(y-1).getTemps());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne une nouvelle instance de l'object Calendar
     * @return Calendar
     */
    public Calendar getNewCalendarSerie() {
        return Calendar.getInstance();
    }

    /**
     * Retourne la diff&eacute;rence de date (heure) entre deux dates.
     * @param d1
     * @param d2
     * @return
     */
    private double getDiffTimeTwoEchant(Date d1, Date d2){
        return (d1.getTime() - d2.getTime()) /(1000.0*60.0) /60.0;
    }
}
