package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Nathan
 */
public class AffectationEnseignants {

    private Database database;
    private ArrayList<String> Donnees;
    private ArrayList<String> Colonne;

    public AffectationEnseignants()
    {
        database = new Database();
        Donnees = new ArrayList<String>();
        Colonne = new ArrayList<String>();
    }

    /**
     * methode permettant d'afficher les enseignants deja affectés a la classe
     * @param jTable1
     * @param nomClasse
     * @return ArrayList<String> contenant les donnees concernant les enseignants
     */
    public ArrayList<String> afficherEnseignantsAssignes(javax.swing.JTable jTable1, String nomClasse)
    {
        Donnees.clear();
        Colonne.clear();
        database.open();
        Colonne.add("nomMatiere");
        Colonne.add("nom");
        Colonne.add("prenom");
        Colonne.add("PP");
        Donnees = database.executeQuery("SELECT matiere.nomMatiere,enseignant.nom, enseignant.prenom,occupe.PP FROM matiere,enseignant,occupe WHERE enseignant.idEnseignant=occupe.idEnseignant AND matiere.idMatiere=enseignant.idmatiere AND occupe.nomClasse='"+nomClasse+"' ORDER BY matiere.nomMatiere",Colonne);
        database.close();

        if(jTable1 != null)
        {
        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
                jTable1.setValueAt(null,j,1);
                jTable1.setValueAt(null,j,2);
                jTable1.setValueAt(null,j,3);
            }

        int row = 0;
        for(int i = 0; i < Donnees.size(); i = i+4)
        {
            jTable1.setValueAt(Donnees.get(i),row,0);
            jTable1.setValueAt(Donnees.get(i+1),row,1);
            jTable1.setValueAt(Donnees.get(i+2),row,2);
            jTable1.setValueAt(Donnees.get(i+3),row,3);
            row ++;
        }

        // centrer le contenu des cellules des colonnes 1 et 2
        DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
        custom.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0 ; i < jTable1.getRowCount() ; i++)
        {
            jTable1.getColumnModel().getColumn(3).setCellRenderer(custom);
        }
        }
        return Donnees;
    }

    /**
     * methode permettant de retirer un enseignant de la classe
     * @param nomClasse
     * @param nom
     * @param prenom
     * @param matiere
     */
    public void retirerEnseignant(String nomClasse, String nom, String prenom, String matiere)
    {
        boolean resultat;
        
        database.open();
        resultat = database.executeUpdate("DELETE FROM occupe WHERE idEnseignant=(SELECT idenseignant FROM enseignant WHERE nom='"+nom+"' AND prenom='"+prenom+"' AND idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"')) AND nomClasse='"+nomClasse+"'");
        database.close();

        if(resultat)
        {
            JOptionPane.showMessageDialog(null, nom+" "+prenom+" "+" ne s'occupe plus de cette classe désormais", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * methode permettant de recuperer tous les enseignants ainsi que la matiere enseignee
     * @return
     */
     public ArrayList<String> recupererEnseignants()
    {
        Donnees.clear();
        Colonne.clear();
        Colonne.add("nomMatiere");
        Colonne.add("nom");
        Colonne.add("prenom");
        database.open();
        Donnees = database.executeQuery("SELECT matiere.nomMatiere,nom,prenom FROM enseignant,matiere WHERE matiere.idMatiere=enseignant.idmatiere", Colonne);
        database.close();

        return Donnees;
    }

     /**
      * methode permettant d'affecter des enseignants a une classe
      * @param nomClasse
      * @param enseignants
      */
     public void affecterEnseignant(String nomClasse,ArrayList<String> enseignants)
     {
         String nomComplet;
         String nom;
         String prenom;
         int Espace = 0;
         
         for(int i = 0; i < enseignants.size(); i++)
         {
             if(enseignants.get(i).equals("PP"))
             {
                 nomComplet = enseignants.get(i+1);
                 for(int j = 0; j < nomComplet.length(); j++)
                 {
                     if(nomComplet.charAt(j) == ' ') Espace = j;
                 }
                 nom = nomComplet.substring(0, Espace);
                 prenom = nomComplet.substring(Espace+1,nomComplet.length());

                 database.open();
                 database.executeUpdate("UPDATE occupe SET PP='non' WHERE nomClasse='"+nomClasse+"'");
                 database.executeUpdate("UPDATE occupe SET PP='oui' WHERE nomClasse='"+nomClasse+"' AND idEnseignant=(SELECT idEnseignant FROM enseignant WHERE nom='"+nom+"' AND prenom='"+prenom+"')");
                 database.close();
             }
             else
             {
                 nomComplet = enseignants.get(i);
                 for(int j = 0; j < nomComplet.length(); j++)
                 {
                     if(nomComplet.charAt(j) == ' ') Espace = j;
                 }
                 nom = nomComplet.substring(0, Espace);
                 prenom = nomComplet.substring(Espace+1,nomComplet.length());

                 database.open();
                 database.executeUpdate("INSERT INTO occupe VALUES ((SELECT idEnseignant FROM enseignant WHERE nom='"+nom+"' AND prenom='"+prenom+"'),'"+nomClasse+"','non');");
                 database.close();
             }
         }
     }
}
