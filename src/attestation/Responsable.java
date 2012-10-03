package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class Responsable {

    private Database database;
    private ArrayList<String> Donnees;
    private ArrayList<String> Colonne;

    private ArrayList<String> Categorie;
    private ArrayList<String> Champs;
    private ArrayList<String> CompetenceResultat;
    private ArrayList<String> CategorieResultat;
    private boolean categorieSaisie;
    private int cptOui;
    private int cptChamps;
    private int cptOuiTrans;
    private int cptChampsTrans;
    private String nomEnseignant;
    private String prenomEnseignant;
    private String matiereEnseignant;

    public Responsable()
    {
        database = new Database();
        Donnees = new ArrayList<String>();
        Colonne = new ArrayList<String>();

        Categorie = new ArrayList<String>();
        Champs = new ArrayList<String>();
        CompetenceResultat = new ArrayList<String>();
        CategorieResultat = new ArrayList<String>();
        categorieSaisie = false;
        cptOui = 0;
        cptChamps = 0;
        cptOuiTrans = 0;
        cptChampsTrans = 0;
        nomEnseignant = new String();
        prenomEnseignant = new String();
        matiereEnseignant = new String();
    }

    /**
     * methode permettant de recuperer le nom de l'enseignant selectionne
     * @return String
     */
    public String getNomEnseignant() { return nomEnseignant; }

    /**
     * methode permettant de recuperer le prenom de l'enseignant
     * @return String
     */
    public String getPrenomEnseignant() { return prenomEnseignant; }

    /**
     * methode permettant de recuperer la matiere de l'enseignant
     * @return String
     */
    public String getMatiereEnseignant() { return matiereEnseignant; }

    /**
     * methode permettant d'afficher les classes avec leurs professeurs principals
     * @param jTable1
     */
    public void afficherClasses(javax.swing.JTable jTable1)
    {
        jTable1.clearSelection();
        database.open();

        Colonne.clear();
        Donnees.clear();
        Colonne.add("nomClasse");
        Colonne.add("nom");
        Colonne.add("prenom");

        Donnees = database.executeQuery("SELECT classe.nomClasse, enseignant.nom, enseignant.prenom FROM classe, occupe, enseignant WHERE classe.nomClasse=occupe.nomClasse AND occupe.idEnseignant=enseignant.idEnseignant AND occupe.PP='oui' ORDER BY classe.nomClasse DESC;",Colonne);
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
        for(int i = 0; i < Donnees.size(); i = i+3)
        {
            jTable1.setValueAt(Donnees.get(i),row,0);
            jTable1.setValueAt(Donnees.get(i+1)+" "+Donnees.get(i+2),row,1);
            row++;
        }
    }

    /**
     * methode permettant d'afficher les classes mais sans leurs professeurs principals
     * @param jTable1
     */
    public void afficherClassesSansPP(javax.swing.JTable jTable1)
    {
        jTable1.clearSelection();
        database.open();

        Colonne.clear();
        Donnees.clear();
        Colonne.add("nomClasse");

        Donnees = database.executeQuery("SELECT classe.nomClasse FROM classe ORDER BY classe.nomClasse DESC;",Colonne);
        database.close();

        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
            }

        int row = 0;
        for(int i = 0; i < Donnees.size(); i++)
        {
            jTable1.setValueAt(Donnees.get(i),row,0);
            row++;
        }
    }

    /**
     * methode permettant d'afficher les enseignants
     * @param jTable2
     */
    public void afficherEnseignants(javax.swing.JTable jTable2)
    {
        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable2.getRowCount(); j++)
            {
                jTable2.setValueAt(null,j,0);
                jTable2.setValueAt(null,j,1);
            }

        Donnees.clear();
        Colonne.clear();
        database.open();

        Colonne.add("nom");
        Colonne.add("prenom");
        Colonne.add("nomMatiere");

        Donnees = database.executeQuery("SELECT nom,prenom,matiere.nomMatiere FROM matiere,enseignant WHERE matiere.idMatiere=enseignant.idmatiere ORDER BY nom",Colonne);
        database.close();

        int row = 0;
        for(int i = 0; i < Donnees.size(); i = i+3)
        {
            jTable2.setValueAt(Donnees.get(i),row,0);
            jTable2.setValueAt(Donnees.get(i+1),row,1);
            jTable2.setValueAt(Donnees.get(i+2),row,2);
            row ++;
        }
    }

    /**
     * methode permettant de verifier si un champs est valide ou non
     * @param Champs ArrayList<String> contenant tous les champs d'une categorie
     * @param j int correspondant a l'indice de la position du champs
     */
    public void verifierChamps(ArrayList<String> Champs,int j)
    {
        if(!categorieSaisie)
        {
            CompetenceResultat.add(Categorie.get(j-3));
            categorieSaisie = true;
        }
        for(int k = 0; k < Champs.size(); k = k + 2)
        {
            try
            {
                if(!Champs.get(k).equals(Champs.get(k-2)) && !Champs.get(k).equals(Champs.get(k+2)))
                {
                    cptChamps ++;
                    if(!Champs.get(k+1).equals("non")) cptOui ++;
                }
                else if(Champs.get(k).equals(Champs.get(k-2)))
                {
                    cptChampsTrans ++;
                    if(!Champs.get(k+1).equals("non")) cptOuiTrans ++;
                }
                else
                {
                    cptChampsTrans = 0;
                    cptOuiTrans = 0;
                    cptChampsTrans ++;
                    if(!Champs.get(k+1).equals("non")) cptOuiTrans ++;
                }
            }
            catch(java.lang.ArrayIndexOutOfBoundsException e)
            {
                if(!Champs.get(k).equals(Champs.get(k+2)))
                {
                    cptChamps ++;
                    if(!Champs.get(k+1).equals("non")) cptOui ++;
                }
                else
                {
                    cptChampsTrans ++;
                    if(!Champs.get(k+1).equals("non")) cptOuiTrans ++;
                }
            }
            catch(java.lang.IndexOutOfBoundsException e)
            {
                if(!Champs.get(k).equals(Champs.get(k-2)))
                {
                    cptChamps ++;
                    if(!Champs.get(k+1).equals("non")) cptOui ++;
                }
                else
                {
                    cptChampsTrans ++;
                    if(!Champs.get(k+1).equals("non")) cptOuiTrans ++;
                }
            }
        }

        if((cptChampsTrans*2)/3 <= cptOuiTrans) cptOui ++;
        if((cptChamps*2)/3 <= cptOui)
        {
            CompetenceResultat.add("Validé");
            CategorieResultat.add("Validé");
        }
        else
        {
            CompetenceResultat.add("Refusé");
            CategorieResultat.add("Refusé");
        }

        cptOui = 0;
        cptChamps = 0;
        cptOuiTrans = 0;
        cptChampsTrans = 0;
    }

    /**
     * methode permettant de verifier les champs de la competence 2 concernant les langues vivantes
     * @param Champs ArrayList<String> contenant tous les champs d'une categorie
     * @param j int correspondant a l'indice de la position du champs
     */
    public void verifierChampsLangue(ArrayList<String> Champs, int j)
    {
        int cptChamps1 = 0;
        int cptChamps2 = 0;
        int cptOui1 = 0;
        int cptOui2 = 0;
        
        if(!categorieSaisie)
        {
            CompetenceResultat.add(Categorie.get(j-3));
            categorieSaisie = true;
        }
        for(int k = 0; k < Champs.size(); k = k + 2)
        {
            try
            {
                if(Champs.get(k).equals(Champs.get(k-2)))
                {
                    cptChamps2 ++;
                    if(!Champs.get(k+1).equals("non")) cptOui2 ++;
                }
                else
                {
                    cptChamps1 ++;
                    if(!Champs.get(k+1).equals("non")) cptOui1 ++;
                }
            }
            catch(java.lang.ArrayIndexOutOfBoundsException e)
            {
                cptChamps1 ++;
                if(!Champs.get(k+1).equals("non")) cptOui1 ++;
            }    
        }

        if((cptChamps1*2)/3 <= cptOui1)
        {
            CompetenceResultat.add("Validé");
            CategorieResultat.add("Validé");
        }
        else
        {
            CompetenceResultat.add("Refusé");
            CategorieResultat.add("Refusé");
        }

        if((cptChamps2*2)/3 <= cptOui2)
        {
            CompetenceResultat.add("Validé");
            CategorieResultat.add("Validé");
        }
        else
        {
            CompetenceResultat.add("Refusé");
            CategorieResultat.add("Refusé");
        }
    }

    /**
     * methode permettant de consulter le resultat de toutes les competences d'un eleve
     * @param nomEleve
     * @param prenomEleve
     * @return ArrayList<String>
     */
    public ArrayList<String> consulterCompetences(String nomEleve, String prenomEleve)
    {
        database.open();
        Colonne.clear();
        Donnees.clear();
        Colonne.add("competence");
        Colonne.add("idCategorie");
        Colonne.add("idChamps");
        Colonne.add("valideChamps");

        Donnees = database.executeQuery("SELECT categorie.competence,categorie.idCategorie,correspond.idChamps,correspond.valideChamps FROM eleve,correspond,champs,categorie WHERE eleve.idEleve=correspond.idEleve AND correspond.idChamps=champs.idChamps AND champs.idCategorie=categorie.idCategorie AND correspond.idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nomEleve+"' AND prenom='"+prenomEleve+"');", Colonne);
        database.close();
        
        for(int i = 0; i < 7; i++)
        {
            CompetenceResultat.add("Compétence "+String.valueOf(i+1));
            categorieSaisie = false;
            Categorie.clear();
            CategorieResultat.clear();
            
            for(int j = 0; j < Donnees.size(); j = j+4)
            {
                if(Integer.parseInt(Donnees.get(j)) == (i+1))
                {
                    Categorie.add(Donnees.get(j+1));
                    Categorie.add(Donnees.get(j+2));
                    Categorie.add(Donnees.get(j+3));
                }          
            }

            for(int j = 0; j < Categorie.size(); j = j + 3)
            {
                try
                {
                    if(Categorie.get(j).equals(Categorie.get(j-3)))
                    {
                        Champs.add(Categorie.get(j+1));
                        Champs.add(Categorie.get(j+2));

                        if(!Categorie.get(j).equals(Categorie.get(j+3)))
                        {
                            if(!Categorie.get(j).equals("5") && !Categorie.get(j).equals("6") && !Categorie.get(j).equals("7") && !Categorie.get(j).equals("8") && !Categorie.get(j).equals("9"))
                            verifierChamps(Champs,j);
                            else verifierChampsLangue(Champs,j);
                        }
                    }
                   else
                   {
                    Champs.clear();
                    CompetenceResultat.add(Categorie.get(j));
                    Champs.add(Categorie.get(j+1));
                    Champs.add(Categorie.get(j+2));

                    if(j == Categorie.size()-3 && (!Categorie.get(j).equals("4") || !Categorie.get(j).equals("13")))
                        {
                            cptChamps ++;
                            if(!Champs.get(1).equals("non")) cptOui ++;
                            if((cptChamps*2)/3 <= cptOui)
                            {
                                CompetenceResultat.add("Validé");
                                CategorieResultat.add("Validé");
                            }
                            else
                            {
                                CompetenceResultat.add("Refusé");
                                CategorieResultat.add("Refusé");
                            }

                            cptOui = 0;
                            cptChamps = 0;
                            cptOuiTrans = 0;
                            cptChampsTrans = 0;
                        }
                   }  
                }
                catch(java.lang.ArrayIndexOutOfBoundsException e)
                {
                    Champs.add(Categorie.get(j+1));
                    Champs.add(Categorie.get(j+2));
                }
                catch(java.lang.IndexOutOfBoundsException e)
                {
                    if(!Categorie.get(j).equals("5") && !Categorie.get(j).equals("6") && !Categorie.get(j).equals("7") && !Categorie.get(j).equals("8") && !Categorie.get(j).equals("9"))
                    verifierChamps(Champs,j);
                    else verifierChampsLangue(Champs,j);
                }
            }

            int cptCategorieOk = 0;
            int cptCategorieOkLangue1 = 0;
            int cptCategorieOkLangue2 = 0;

            if(i != 1)
            {
                for(int l = 0; l < CategorieResultat.size(); l ++)
                {
                    if(CategorieResultat.get(l).equals("Validé")) cptCategorieOk ++;
                }
                if(cptCategorieOk > (CategorieResultat.size()*2)/3) CompetenceResultat.add("Validé");
                else CompetenceResultat.add("Refusé");
            }
            else
            {
                for(int l = 0; l < CategorieResultat.size(); l = l+2)
                {
                    if(CategorieResultat.get(l).equals("Validé")) cptCategorieOkLangue1 ++;
                    if(CategorieResultat.get(l+1).equals("Validé")) cptCategorieOkLangue2 ++;
                }
                if(cptCategorieOkLangue1 > (5*2)/3) CompetenceResultat.add("Validé");
                else CompetenceResultat.add("Refusé");
                if(cptCategorieOkLangue2 > (5*2)/3) CompetenceResultat.add("Validé");
                else CompetenceResultat.add("Refusé");
            }
        }
        cptOui = 0;
        cptChamps = 0;
        cptOuiTrans = 0;
        cptChampsTrans = 0;
        
        return CompetenceResultat;
    }

    /**
     * methode permettant d'extraire les donnees pointees,avec la souris, par l'enseignant
     * @param jTable1 tableau ou sont affiches les donnees visibles par l'enseignant
     * @param evt  evenement correspondant au clic de la souris
     */
    public void getMouseClicked(javax.swing.JTable jTable1, java.awt.event.MouseEvent evt)
    {
        try
        {
            nomEnseignant = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),0).toString();
            prenomEnseignant = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),1).toString();
            matiereEnseignant = jTable1.getValueAt(jTable1.rowAtPoint(evt.getPoint()),2).toString();
        }
        catch(java.lang.NullPointerException e){ JOptionPane.showMessageDialog(null,"Veuillez selectionner une ligne renseignée","Information", JOptionPane.INFORMATION_MESSAGE);}
    }
}
