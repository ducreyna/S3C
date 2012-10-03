package attestation;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;

/**
 *
 * @author nathan
 */
public class Enseignant {

    private Database database;
    private ArrayList<String> Colonne;
    private ArrayList<String> Donnees;
    private String classeSelectionnee;
    private String PP;
    private String nomEleve;
    private String prenomEleve;
    private int keyCount;

    public Enseignant() {

        database = new Database();
        Colonne = new ArrayList<String>();
        Donnees = new ArrayList<String>();
        classeSelectionnee = new String();
        nomEleve = new String();
        prenomEleve = new String();
        keyCount = 0;
    }

    /**
     * methode permettant de renvoyer la classe selectionnee pour l'elaboration de la fiche
     * @return String
     */
    public String getClasseSelectionnee() { return classeSelectionnee; }

    /**
     * methode permettant de modifier la classe selectionnee par l'enseignant
     * @param classeSelectionnee
     */
    public void setClasseSelectionnee(String classeSelectionnee) { this.classeSelectionnee = classeSelectionnee; }

    /**
     * methode permettant de savoir si l'enseignant est professeur principal de la classe
     * @return String
     */
    public String getPP() { return PP; }
    /**
     * methode permettant de renvoyer le nom de l'eleve selectionne
     * @return String
     */
    public String getNomEleve() { return nomEleve; }
    
    /**
     * methode permettant de modifier le nom de l'eleve
     * @param nomEleve
     */
    public void setNomEleve(String nomEleve) { this.nomEleve = nomEleve; }

    /**
     * methode permettant de renvoyer le prenom de l'eleve selectionnee
     * @return String
     */
    public String getPrenomEleve() { return prenomEleve; }

    /**
     * methode permettant de renvoyer le nombre de fois que les touches Haut/Bas du clavier ont ete pressees
     * @return int
     */
    public int getKeyCount() { return keyCount ; }

    /**
     * methode permettant de modifier la valeur de keyCount, autrement dit le nombre de fois que les touches Haut/Bas du clavier ont ete pressees
     * @param KeyCount
     */
    public void setKeyCount(int KeyCount) { this.keyCount = KeyCount; }
    
    /**
    * methode se connectant a la base de donnees, recupere les classes dont s'occupe l'enseignant connecté et les affiche dans une jTable
    * @param jTable1 tableau ou sont affichees les donnees demandees
    * @param Lognom nom de l'enseignant connecte
    */
    public void afficherClasses(javax.swing.JTable jTable1, String Lognom, String Logprenom)
    {
        jTable1.clearSelection();
        database.open();

        Colonne.clear();
        Donnees.clear();
        Colonne.add("nomClasse");
        Colonne.add("PP");

        Donnees = database.executeQuery("SELECT  classe.nomClasse, occupe.PP FROM classe, occupe, enseignant WHERE classe.nomClasse=occupe.nomClasse AND occupe.idEnseignant=enseignant.idEnseignant AND enseignant.idEnseignant= (SELECT idEnseignant FROM enseignant WHERE nom='"+Lognom+"' AND prenom='"+Logprenom+"') ORDER BY classe.nomClasse DESC;",Colonne);
        database.close();

        // ces 2 lignes servent à modifier le nom des colonnes de ma jTable
        jTable1.getColumnModel().getColumn(0).setHeaderValue("Classe");
        jTable1.getColumnModel().getColumn(1).setHeaderValue("P P");
        jTable1.getTableHeader().resizeAndRepaint();

        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
                jTable1.setValueAt(null,j,1);
            }

        int row = 0;
        for(int i = 0; i < Donnees.size(); i = i+2)
        {
            jTable1.setValueAt(Donnees.get(i),row,0);
            jTable1.setValueAt(Donnees.get(i+1),row,1);
            row++;
        }

        // centrer le contenu des cellules des colonnes 1 et 2
        DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
        custom.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0 ; i < jTable1.getRowCount() ; i++)
        {
            jTable1.getColumnModel().getColumn(1).setCellRenderer(custom);
        }
    }

    /**
    * methode se connectant a la base de donnees, recupere les eleves de la classe selectionnee par l'enseignant connecté et les affiche dans une jTable
    * @param jTable1 tableau où sont affichees les donnees demandees
    */
    public void afficherEleves(javax.swing.JTable jTable1)
    {
        jTable1.clearSelection();
        database.open();

        Colonne.clear();
        Donnees.clear();
        Colonne.add("nom");
        Colonne.add("prenom");

        Donnees = database.executeQuery("SELECT eleve.nom,eleve.prenom FROM eleve WHERE nomClasse='"+classeSelectionnee+"' ORDER BY eleve.nom;",Colonne);
        database.close();

        // ces 2 lignes servent à modifier le nom des colonnes de ma jTable
        jTable1.getColumnModel().getColumn(0).setHeaderValue("Nom");
        jTable1.getColumnModel().getColumn(1).setHeaderValue("Prénom");
        jTable1.getTableHeader().resizeAndRepaint();

        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
                jTable1.setValueAt(null,j,1);
            }

        int row = 0;
        for(int i = 0; i < Donnees.size(); i = i+2)
        {
            jTable1.setValueAt(Donnees.get(i),row,0);
            jTable1.setValueAt(Donnees.get(i+1),row,1);
            row++;
        }
    }

    /**
     * methode permettant d'extraire les donnees pointees,avec la souris, par l'enseignant
     * @param jTable1 tableau ou sont affiches les donnees visibles par l'enseignant
     * @param evt  evenement correspondant au clic de la souris
     * @param nbreClic int le nombre de clic fait par l'enseignant sur le bouton "Suivant"
     */
    public void getMouseClicked(javax.swing.JTable jTable1, java.awt.event.MouseEvent evt, int nbreClic)
    {
        try
        {
            if(nbreClic == 0)
            {
                classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),0).toString();
                PP = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),1).toString();
            }
            else if(nbreClic == 5)
            {
                classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),0).toString();
            }
            else
            {
                nomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),0).toString();
                prenomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),1).toString();
            }
        }
        catch(java.lang.NullPointerException e){ JOptionPane.showMessageDialog(null,"Veuillez selectionner une ligne renseignée","Information", JOptionPane.INFORMATION_MESSAGE);}
    }

    /**
     * methode permettant d'extraire les donnees selectionnees a l'aide des fleches
     * @param jTable1 tableau ou sont affiches les donnees visibles par l'enseignant
     * @param evt  evenement correspondant au clic de la souris, cela sert de point de depart pour la manipulation des fleches Haut/Bas
     * @param nbreClic  int le nombre de clic fait par l'enseignant
     * @param keyCode int le code de la touche pressee
     * @param keyCount int le nombre de fois qu'une touche a ete pressee
     */
    public void getKeyPressed(javax.swing.JTable jTable1, java.awt.event.MouseEvent evt, int nbreClic, int keyCode)
    {
        try
        {
            if(nbreClic == 0)
            {
                    if(keyCount < 0)
                    {
                        if(keyCode == 38)
                        {
                            classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                            PP = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                        }
                        else if(keyCode == 40)
                        {
                            classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                            PP = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                        }
                    }
                    else
                    {
                        if(keyCode == 38)
                        {
                            classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                            PP = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                        }
                        else if(keyCode == 40)
                        {
                            classeSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                            PP = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                        }
                    }
            }
            else
            {
                if(keyCount < 0)
                 {
                    keyCount = keyCount * -1;
                    if(keyCode == 38)
                     {
                        nomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) - keyCount,0).toString();
                        prenomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) - keyCount,1).toString();
                     }
                     else if(keyCode == 40)
                     {
                        nomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) - keyCount,0).toString();
                        prenomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) - keyCount,1).toString();
                     }
                 }
                 else
                 {
                    if(keyCode == 38)
                     {
                        nomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                        prenomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                     }
                    else if(keyCode == 40)
                     {
                        nomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                        prenomEleve = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,1).toString();
                     }
                 }
            }
        }
        catch(java.lang.NullPointerException e)
        {
            JOptionPane.showMessageDialog(null,"Veuillez selectionner une ligne renseignée","Information", JOptionPane.INFORMATION_MESSAGE);           
        }

        catch(java.lang.ArrayIndexOutOfBoundsException e)
        {
            setKeyCount(getKeyCount() + 1);
            JOptionPane.showMessageDialog(null,"Veuillez selectionner une ligne renseignée","Information", JOptionPane.INFORMATION_MESSAGE);           
        }
    }
}