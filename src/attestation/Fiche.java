package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.io.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author nathan
 */
public class Fiche {

    private Database database;
    private ArrayList<String> Colonne;
    private ArrayList<String> Donnees;
    private ArrayList<Integer> idCategorie;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel jLabel6;
    private String consigneSelectionnee;
    private ArrayList<String> consigneSelectionneeMult;
    private String nomEleve;
    private String prenomEleve;
    private int idNextCategorie;
    private int nbreClic;
    private int keyCount;

    public Fiche(String nomEleve, String prenomEleve,javax.swing.JLabel jLabel6, javax.swing.JTable jTable1)
    {
        database = new Database();
        Colonne = new ArrayList<String>();
        Donnees = new ArrayList<String>();
        consigneSelectionneeMult = new ArrayList<String>();
        consigneSelectionnee = new String();
        idCategorie = new ArrayList<Integer>();
        this.nomEleve = nomEleve;
        this.prenomEleve = prenomEleve;
        idNextCategorie = 0;
        nbreClic = 0;
        keyCount = 0;
        this.jLabel6 = jLabel6;
        this.jTable1 = jTable1;
    }

    public Fiche(String nomEleve,String prenomEleve)
    {
        database = new Database();
        Colonne = new ArrayList<String>();
        Donnees = new ArrayList<String>();
    }

    /**
     * methode permettant de renvoyer l'identifiant de la prochaine categorie
     * @return int
     */
    public int getIdNextCategorie() { return idNextCategorie; }

    /**
     * methode permettant de renvoyer l'arraylist contenant les donnees de la fiche
     * @return ArrayList<String>
     */
    public ArrayList<String> getDonnees() { return Donnees; }

    /**
     * methode permettant de renvoyer l'arraylist contenant les identifiants des differentes categories
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getIdCategorie() { return idCategorie; }

    /**
     * methode permettant de modifier la valeur de idCategorie
     * @param idCategorie
     */
    public void setIdCategorie(ArrayList<Integer> idCategorie) { this.idCategorie = idCategorie; }

    /**
     * methode permettant de modifier la valeur de consigneSelectionneeMult
     * @param consigneSelectionneeMult
     */
    public void setConsigneSelectionneeMult(ArrayList<String> consigneSelectionneeMult) { this.consigneSelectionneeMult = consigneSelectionneeMult; }

    /**
     * methode permettant de renvoyer le nombre de clic effectue afin de connaitre l'emplacement de la categorie
     * @return int
     */
    public int getNbreClic() { return nbreClic; }

    /**
     * methode permettant de modifier la valeur de nbreClic
     * @param nbreClic
     */
    public void setNbreClic(int nbreClic) { this.nbreClic = nbreClic; }

    /**
     * methode permettant de renvoyer le nombre de fois que l'utilisateur a presse les touches Haut/Bas du clavier
     * @return int
     */
    public int getKeyCount() { return keyCount; }

    /**
     * methode permettant de modifier la valeur de keyCount
     * @param keyCount
     */
    public void setKeyCount(int keyCount) { this.keyCount = keyCount; }

    /**
     * methode renvoyant toutes les matieres enseignees
     * @return
     */
    public ArrayList<String> recupererMatiere()
    {
        database.open();

        Colonne.clear();
        Donnees.clear();
        Colonne.add("nomMatiere");

        Donnees = database.executeQuery("SELECT DISTINCT nomMatiere FROM matiere,correspond WHERE matiere.idMatiere=correspond.idMatiere AND idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"')",Colonne);
        database.close();
        return Donnees;
    }

    /**
     * methode permettant de recuperer tous les champs d'une fiche en utilisant la matiere de l'enseignant
     * @param Matiere
     */
    public void recupererFiche(String Matiere)
    {
        Colonne.clear();
        Donnees.clear();
        Colonne.add("nom");
        Colonne.add("denomination");
        Colonne.add("valideChamps");
        Colonne.add("nbOui");

        database.open();
        Donnees = database.executeQuery("SELECT categorie.nom, champs.denomination,correspond.valideChamps,correspond.nbOui FROM champs,correspond,matiere,categorie WHERE champs.idChamps=correspond.idChamps AND correspond.idMatiere=matiere.idMatiere AND champs.idCategorie=categorie.idCategorie AND correspond.idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"');",Colonne);
        database.close();
    }

    public ArrayList<String> recupereEleves(String classe)
    {
        Colonne.clear();
        Donnees.clear();
        Colonne.add("nom");
        Colonne.add("prenom");

        database.open();
        Donnees = database.executeQuery("SELECT nom,prenom FROM eleve WHERE nomClasse='"+classe+"'",Colonne);
        database.close();

        return Donnees;
    }

    /**
     * methode permettant d'afficher les champs d'une categorie dans un jTable
     * @param idC identifiant de la categorie
     */
    public void afficherChamps(int idC)
    {
        // reinitialiser les cellules du tableau
        jTable1.clearSelection();
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
                jTable1.setValueAt(null,j,1);
            }

        int row = 0;
        int i;
        jLabel6.setText(Donnees.get(idC));

        // modifie la taille de la premiere colonne du tableau
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(450);

        for(i = idC; i < Donnees.size(); i = i+4)
        {
            if(Donnees.get(i).equals(jLabel6.getText()))
            {
                for(int j = 1; j < 3; j++)
                {
                    jTable1.setValueAt(Donnees.get(i+j),row,j-1);
                }
                row++;
            }
            else
            {
               idNextCategorie = i;
               break;
            }
        }

        // centrer le contenu des cellules des colonnes 1 et 2
        DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
        custom.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (i = 0 ; i < jTable1.getRowCount() ; i++)
        {
            jTable1.getColumnModel().getColumn(1).setCellRenderer(custom);
        }
    }

    /**
     * methode permettant de valider un champs selectionne par l'enseignant
     * @param nomEleve
     * @param prenomEleve
     * @param Matiere
     * @param Classe
     */
    public void validerChamps(String nomEleve, String prenomEleve, String Matiere, String Classe)
    {
        int nbOui;
        boolean dejaValide = false;
        try
        {
            database.open();
            Colonne.clear();
            Colonne.add("nbOui");


            if(consigneSelectionnee.length() != 0)
            {
                nbOui = Integer.parseInt(database.executeQuery("SELECT nbOui FROM correspond WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = '"+Matiere+"' AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionnee+"');",Colonne).get(0));
                database.close();

                if((Integer.parseInt(Classe.substring(0,1)) == 6 && nbOui == 1) || (Integer.parseInt(Classe.substring(0,1)) == 5 && nbOui == 2) || (Integer.parseInt(Classe.substring(0,1)) == 4 && nbOui == 3) || (Integer.parseInt(Classe.substring(0,1)) == 3 && nbOui == 4))
                {
                    JOptionPane.showMessageDialog(null,"Cette consigne a déjà été validée cette année","Information",JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    database.open();
                    database.executeUpdate("UPDATE correspond SET valideChamps = 'oui',nbOui = nbOui + 1 WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = (SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionnee+"');");
                    database.close();
                    recupererFiche(Matiere);
                    afficherChamps(idCategorie.get(nbreClic));
                    JOptionPane.showMessageDialog(null,"La consigne selectionnée a bien été validée.","Confirmation",JOptionPane.INFORMATION_MESSAGE);  
                }
                consigneSelectionnee = new String();
            }
            else
            {
                database.open();
                for(int i = 0; i < consigneSelectionneeMult.size(); i++)
                {
                    nbOui = Integer.parseInt(database.executeQuery("SELECT nbOui FROM correspond WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = (SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionneeMult.get(i)+"');",Colonne).get(0));

                    if((Integer.parseInt(Classe.substring(0,1)) == 6 && nbOui == 1) || (Integer.parseInt(Classe.substring(0,1)) == 5 && nbOui == 2) || (Integer.parseInt(Classe.substring(0,1)) == 4 && nbOui == 3) || (Integer.parseInt(Classe.substring(0,1)) == 3 && nbOui == 4))
                    {
                        dejaValide = true;
                    }
                    else
                    {
                        database.executeUpdate("UPDATE correspond SET valideChamps = 'oui',nbOui = nbOui + 1 WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = (SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionneeMult.get(i)+"');");
                    }
                }
                database.close();
                if(dejaValide) JOptionPane.showMessageDialog(null,"Les consignes selectionnées ont bien été validées sauf celles qui avaient déjà été validées cette année","Information",JOptionPane.INFORMATION_MESSAGE);
                else if(consigneSelectionneeMult.size() > 1)
                {
                    JOptionPane.showMessageDialog(null,"Les consignes selectionnées ont bien été validées.","Confirmation",JOptionPane.INFORMATION_MESSAGE);
                }
                else JOptionPane.showMessageDialog(null,"La consigne selectionnée a bien été validée.","Confirmation",JOptionPane.INFORMATION_MESSAGE);
                recupererFiche(Matiere);
                afficherChamps(idCategorie.get(nbreClic));
                consigneSelectionneeMult.clear();
            }
        }
        catch(java.lang.IndexOutOfBoundsException e){ JOptionPane.showMessageDialog(null,"Veuillez selectionner une consigne.","Information", JOptionPane.INFORMATION_MESSAGE); }
    }

    /**
     * methode permettant de valider un champs selectionne par l'enseignant
     * @param nomEleve
     * @param prenomEleve
     * @param Matiere
     * @param Classe
     */
    public void devaliderChamps(String nomEleve, String prenomEleve, String Matiere, String Classe)
    {
        try
        {
            if(consigneSelectionnee.length() != 0)
            {
                database.open();
                database.executeUpdate("UPDATE correspond SET valideChamps = 'non',nbOui = nbOui - 1 WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = (SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionnee+"');");
                database.close();
                recupererFiche(Matiere);
                afficherChamps(idCategorie.get(nbreClic));
                JOptionPane.showMessageDialog(null,"La consigne selectionnée a bien été dévalidée.","Confirmation",JOptionPane.INFORMATION_MESSAGE);
                consigneSelectionnee = new String();
            }
            else
            {
                database.open();
                for(int i = 0; i < consigneSelectionneeMult.size(); i++)
                {
                    database.executeUpdate("UPDATE correspond SET valideChamps = 'non',nbOui = nbOui - 1 WHERE idEleve = (SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"') AND idMatiere = (SELECT idMatiere FROM matiere WHERE nomMatiere='"+Matiere+"') AND idChamps = (SELECT idChamps FROM champs WHERE denomination='"+consigneSelectionneeMult.get(i)+"');");
                }
                database.close();
                if(consigneSelectionneeMult.size() > 1)
                {
                    JOptionPane.showMessageDialog(null,"Les consignes selectionnées ont bien été dévalidées.","Confirmation",JOptionPane.INFORMATION_MESSAGE);
                }
                else JOptionPane.showMessageDialog(null,"La consigne selectionnée a bien été dévalidée.","Confirmation",JOptionPane.INFORMATION_MESSAGE);
                recupererFiche(Matiere);
                afficherChamps(idCategorie.get(nbreClic));
                consigneSelectionneeMult.clear();
            }
        }
        catch(java.lang.IndexOutOfBoundsException e){ JOptionPane.showMessageDialog(null,"Veuillez selectionner une consigne.","Information", JOptionPane.INFORMATION_MESSAGE); }
    }

    /**
     * methode retournant le nombre de ligne renseignee dans le jTable
     * @return int
     */
    public int Count()
    {
        int cpt = 0;
        try
        {
            do
                {
                    cpt ++;
                }
            while(!jTable1.getValueAt(cpt,0).toString().equals(""));
        }
        catch(java.lang.NullPointerException e) {}

        return cpt;
    }

    /**
     * methode permettant d'extraire les donnees pointees,avec la souris, par l'enseignant
     * @param evt  evenement correspondant au clic de la souris
     */
    public void getMouseClicked(java.awt.event.MouseEvent evt)
    {
        try
        {
           consigneSelectionneeMult.add(jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),0).toString());
        }
        catch(java.lang.NullPointerException e){ JOptionPane.showMessageDialog(null,"Veuillez selectionner une ligne renseignée","Information", JOptionPane.INFORMATION_MESSAGE);}
    }

    /**
     * methode permettant d'extraire les donnees selectionnees a l'aide des fleches
     * @param evt  evenement correspondant au clic de la souris, cela sert de point de depart pour la manipulation des fleches Haut/Bas
     * @param keyCode int le code de la touche pressee
     */
    public void getKeyPressed(java.awt.event.MouseEvent evt, int keyCode)
    {
        try
        {
            if(keyCount < 0)
            {
                if(keyCode == 38)
                {
                    consigneSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                }
                else if(keyCode == 40)
                {
                    consigneSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                }
            }
            else
            {
                if(keyCode == 38)
                {
                    consigneSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
                }
                else if(keyCode == 40)
                {
                    consigneSelectionnee = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()) + keyCount,0).toString();
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

    /**
     * methode permettant d'enregistrer la fiche d'un eleve dans une matiere
     * @param nom
     * @param prenom
     * @param matiere
     * @param classe
     */
    public void enregistrerEleve(String nom, String prenom, String matiere, String classe)
    {
        Donnees.clear();
        Colonne.clear();
        Colonne.add("nom");
        Colonne.add("denomination");
        Colonne.add("valideChamps");
        database.open();
        Donnees = database.executeQuery("SELECT categorie.nom,champs.denomination,correspond.valideChamps FROM champs,correspond,matiere,categorie WHERE champs.idChamps=correspond.idChamps AND correspond.idMatiere=matiere.idMatiere AND champs.idCategorie=categorie.idCategorie AND correspond.idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"') AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nom+"' AND prenom='"+prenom+"');",Colonne);
        database.close();
        try
	{
                new File("Fiches").mkdir();
                new File("Fiches/"+classe).mkdir();
                new File("Fiches/"+classe+"/"+matiere).mkdir();
		BufferedWriter fichier = new BufferedWriter(new FileWriter("Fiches/"+classe+"/"+matiere+"/"+nom+" "+prenom+".txt",false));
                fichier.write("\r\n\t\t\t\t"+matiere+"\r\n\r\n");
                fichier.write("Classe:\t"+classe+"\r\n\r\n\r\n");

                for(int i = 0; i < 23; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("COMPETENCES");
                for(int i = 0; i < 39; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("VALIDE\r\n");

                for(int i = 0; i < Donnees.size(); i = i+3)
                {
                    try
                    {
                        if(Donnees.get(i).equals(Donnees.get(i-3)))
                        {
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                        else
                        {
                            fichier.write("\r\n\r\n");
                            fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                    }
                    catch(java.lang.IndexOutOfBoundsException e)
                    {
                        fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                        fichier.write("\r\n"+Donnees.get(i+1));
                        for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                        {
                            fichier.write(" ");
                        }
                        fichier.write(Donnees.get(i+2));
                    }
                }
		fichier.close();
	}

	catch (FileNotFoundException e) {System.out.println(e.getMessage());}

	catch (IOException e) {System.out.println(e.getMessage());}
    }

    /**
     * methode permettant d'enregistrer la fiche d'un eleve dans une matiere
     * @param nom
     * @param prenom
     * @param matiere
     * @param classe
     */
    public void enregistrerToutEleve(String nom, String prenom, ArrayList<String> matiere, String classe)
    {
        ArrayList<String> copieMatiere = new ArrayList<String>(matiere);

        Donnees.clear();
        Colonne.clear();
        Colonne.add("nom");
        Colonne.add("denomination");
        Colonne.add("valideChamps");
        for(int k = 0; k < copieMatiere.size(); k++)
        {
        Donnees.clear();
        database.open();
        Donnees = database.executeQuery("SELECT categorie.nom,champs.denomination,correspond.valideChamps FROM champs,correspond,matiere,categorie WHERE champs.idChamps=correspond.idChamps AND correspond.idMatiere=matiere.idMatiere AND champs.idCategorie=categorie.idCategorie AND correspond.idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+copieMatiere.get(k)+"') AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nom+"' AND prenom='"+prenom+"');",Colonne);
        database.close();
        try
	{
                new File("Fiches").mkdir();
                new File("Fiches/"+classe).mkdir();
                new File("Fiches/"+classe+"/"+copieMatiere.get(k)).mkdir();
		BufferedWriter fichier = new BufferedWriter(new FileWriter("Fiches/"+classe+"/"+copieMatiere.get(k)+"/"+nom+" "+prenom+".txt",false));
                fichier.write("\r\n\t\t\t\t"+copieMatiere.get(k)+"\r\n\r\n");
                fichier.write("Classe:\t"+classe+"\r\n\r\n\r\n");

                for(int i = 0; i < 23; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("COMPETENCES");
                for(int i = 0; i < 39; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("VALIDE\r\n");

                for(int i = 0; i < Donnees.size(); i = i+3)
                {
                    try
                    {
                        if(Donnees.get(i).equals(Donnees.get(i-3)))
                        {
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                        else
                        {
                            fichier.write("\r\n\r\n");
                            fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                    }
                    catch(java.lang.IndexOutOfBoundsException e)
                    {
                        fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                        fichier.write("\r\n"+Donnees.get(i+1));
                        for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                        {
                            fichier.write(" ");
                        }
                        fichier.write(Donnees.get(i+2));
                    }
                }
		fichier.close();
	}


	catch (FileNotFoundException e) {}

	catch (IOException e) {}

        }
    }

    /**
     * methode permettant d'enregistrer la fiche d'une matière pour toute la classe
     * @param eleve
     * @param matiere
     * @param classe
     */
    public void enregistrerClasse(ArrayList<String> eleve, String matiere, String classe)
    {
        ArrayList<String> copieEleve = new ArrayList<String>(eleve);
        
        Colonne.clear();
        Colonne.add("nom");
        Colonne.add("denomination");
        Colonne.add("valideChamps");
        for(int k = 0; k < copieEleve.size(); k = k+2)
        {
        Donnees.clear();
        database.open();
        Donnees = database.executeQuery("SELECT categorie.nom,champs.denomination,correspond.valideChamps FROM champs,correspond,matiere,categorie WHERE champs.idChamps=correspond.idChamps AND correspond.idMatiere=matiere.idMatiere AND champs.idCategorie=categorie.idCategorie AND correspond.idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"') AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+copieEleve.get(k)+"' AND prenom='"+copieEleve.get(k+1)+"');",Colonne);
        database.close();
        try
	{
                new File("Fiches").mkdir();
                new File("Fiches/"+classe).mkdir();
                new File("Fiches/"+classe+"/"+matiere).mkdir();
		BufferedWriter fichier = new BufferedWriter(new FileWriter("Fiches/"+classe+"/"+matiere+"/"+copieEleve.get(k)+" "+copieEleve.get(k+1)+".txt",false));
                fichier.write("\r\n\t\t\t\t"+matiere+"\r\n\r\n");
                fichier.write("Classe:\t"+classe+"\r\n\r\n\r\n");

                for(int i = 0; i < 23; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("COMPETENCES");
                for(int i = 0; i < 39; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("VALIDE\r\n");

                for(int i = 0; i < Donnees.size(); i = i+3)
                {
                    try
                    {
                        if(Donnees.get(i).equals(Donnees.get(i-3)))
                        {
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                        else
                        {
                            fichier.write("\r\n\r\n");
                            fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                    }
                    catch(java.lang.IndexOutOfBoundsException e)
                    {
                        fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                        fichier.write("\r\n"+Donnees.get(i+1));
                        for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                        {
                            fichier.write(" ");
                        }
                        fichier.write(Donnees.get(i+2));
                    }
                }
		fichier.close();
	}

	catch (FileNotFoundException e) {}

	catch (IOException e) {}

        }
    }

    /**
     * methode permettant d'enregistrer les fiches de toutes les matières pour toute la classe
     * @param eleve
     * @param matiere
     * @param classe
     */
    public void enregistrerToutesClasse(ArrayList<String> eleve, ArrayList<String> matiere, String classe)
    {
        ArrayList<String> copieEleve = new ArrayList<String>(eleve);
        ArrayList<String> copieMatiere = new ArrayList<String>(matiere);
        Colonne.clear();
        Colonne.add("nom");
        Colonne.add("denomination");
        Colonne.add("valideChamps");
        for(int l = 0; l < copieMatiere.size(); l++)
        {
        for(int k = 0; k < copieEleve.size(); k = k+2)
        {
        Donnees.clear();
        database.open();
        Donnees = database.executeQuery("SELECT categorie.nom,champs.denomination,correspond.valideChamps FROM champs,correspond,matiere,categorie WHERE champs.idChamps=correspond.idChamps AND correspond.idMatiere=matiere.idMatiere AND champs.idCategorie=categorie.idCategorie AND correspond.idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+copieMatiere.get(l)+"') AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+copieEleve.get(k)+"' AND prenom='"+copieEleve.get(k+1)+"');",Colonne);
        database.close();
        try
	{
                new File("Fiches").mkdir();
                new File("Fiches/"+classe).mkdir();
                new File("Fiches/"+classe+"/"+copieMatiere.get(l)).mkdir();
		BufferedWriter fichier = new BufferedWriter(new FileWriter("Fiches/"+classe+"/"+copieMatiere.get(l)+"/"+copieEleve.get(k)+" "+copieEleve.get(k+1)+".txt",false));
                fichier.write("\r\n\t\t\t\t"+copieMatiere.get(l)+"\r\n\r\n");
                fichier.write("Classe:\t"+classe+"\r\n\r\n\r\n");

                for(int i = 0; i < 23; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("COMPETENCES");
                for(int i = 0; i < 39; i++)
                {
                    fichier.write(" ");
                }
                fichier.write("VALIDE\r\n");

                for(int i = 0; i < Donnees.size(); i = i+3)
                {
                    try
                    {
                        if(Donnees.get(i).equals(Donnees.get(i-3)))
                        {
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                        else
                        {
                            fichier.write("\r\n\r\n");
                            fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                            fichier.write("\r\n"+Donnees.get(i+1));
                            for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                            {
                                fichier.write(" ");
                            }
                            fichier.write(Donnees.get(i+2));
                        }
                    }
                    catch(java.lang.IndexOutOfBoundsException e)
                    {
                        fichier.write("\r\n\r\n"+Donnees.get(i)+"\r\n");
                        fichier.write("\r\n"+Donnees.get(i+1));
                        for(int j = 0; j < (74-Donnees.get(i+1).length()); j++)
                        {
                            fichier.write(" ");
                        }
                        fichier.write(Donnees.get(i+2));
                    }
                }
		fichier.close();
	}

	catch (FileNotFoundException e) {}

	catch (IOException e) {}

        }
        }
    }
}
