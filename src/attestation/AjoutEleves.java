package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class AjoutEleves {

    private Database database;

    public AjoutEleves()
    {
        database = new Database();
    }

    /**
     * methode permettant de recuperer toutes les informations concernant l'eleve
     * @param nom
     * @param prenom
     * @return ArrayList<String> contenant toutes les donnees
     */
    public ArrayList<String> recupereEleveInformations(String nom, String prenom)
    {
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();

        Colonne.add("idEleve");
        Colonne.add("LV1");
        Colonne.add("LV2");
        Colonne.add("nomClasse");
        database.open();
        Donnees = database.executeQuery("SELECT idEleve, LV1, LV2, nomClasse FROM eleve WHERE nom='"+nom+"' AND prenom='"+prenom+"'",Colonne);
        database.close();

        return Donnees;
    }

    /**
     * methode permettant de recuperer toutes les classes enregistrees
     * @return
     */
    public ArrayList<String> recupererClasses()
    {
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();

        Colonne.add("nomClasse");
        database.open();
        Donnees = database.executeQuery("SELECT nomClasse FROM classe",Colonne);
        database.close();

        return Donnees;
    }

    /**
     * methode permettant d'ajouter un eleve dans la base tout en gerant les doublons
     * @param INE
     * @param nom
     * @param prenom
     * @param LV1
     * @param LV2
     * @param nomClasse
     * @param Import
     */
    public void ajout(String INE, String nom, String prenom, String LV1, String LV2, String nomClasse, boolean Import)
    {
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();
        ArrayList<String> Eleves = new ArrayList<String>();
        boolean doublon = false;

        database.open();
        Colonne.add("idEleve");
        Eleves = database.executeQuery("SELECT idEleve FROM eleve", Colonne);
        for(int i = 0; i < Eleves.size(); i++)
        {
            if(Eleves.get(i).equals(INE)) doublon = true;
        }

        if(!doublon)
        {
            if(nomClasse.length() != 0)
            database.executeUpdate("INSERT INTO eleve VALUES('"+INE+"','"+nom+"','"+prenom+"','"+LV1+"','"+LV2+"','"+nomClasse+"')");
            else database.executeUpdate("INSERT INTO eleve VALUES('"+INE+"','"+nom+"','"+prenom+"','"+LV1+"','"+LV2+"','LIBRE')");

            Colonne.clear();
            Colonne.add("idChamps");
            Colonne.add("nbOui");
            Colonne.add("valideChamps");
            Colonne.add("idMatiere");
        
            Donnees = database.executeQuery("SELECT idChamps,nbOui,valideChamps,idMatiere FROM correspond WHERE idEleve='0'",Colonne);
        
            for(int i = 0; i < Donnees.size(); i = i+4)
            {
                database.executeUpdate("INSERT INTO correspond VALUES ('"+INE+"','"+Donnees.get(i)+"','"+Donnees.get(i+1)+"','"+Donnees.get(i+2)+"','"+Donnees.get(i+3)+"')");
            }
        
            database.executeUpdate("UPDATE correspond SET idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+LV1+"') WHERE idEleve='"+INE+"' AND idMatiere='1'");
            if(LV2.length() != 0)
            {
                database.executeUpdate("UPDATE correspond SET idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+LV2+"') WHERE idEleve='"+INE+"' AND idMatiere='2'");
                database.close();
            }
            else database.close();

            if(!Import) JOptionPane.showMessageDialog(null, "L'élève a bien été ajouté dans la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            database.executeUpdate("UPDATE eleve SET nomClasse='"+nomClasse+"' WHERE idEleve='"+INE+"'");
            database.executeUpdate("UPDATE correspond SET idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+LV1+"') WHERE idEleve='"+INE+"' AND idMatiere='1'");
            if(LV2.length() != 0)
            {
                database.executeUpdate("UPDATE correspond SET idMatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+LV2+"') WHERE idEleve='"+INE+"' AND idMatiere='2'");
                database.close();
            }
            else database.close();
        }
    }

    /**
     * methode permettant de modifier les renseignements concernant l'eleve
     * @param INE
     * @param nom
     * @param prenom
     * @param LV1
     * @param LV2
     * @param nomClasse
     */
    public void modification( String INE, String nom, String prenom, String LV1, String LV2, String nomClasse)
    {
        database.open();
        database.executeUpdate("UPDATE eleve SET nom='"+nom+"', prenom='"+prenom+"', LV1='"+LV1+"', LV2='"+LV2+"', nomClasse='"+nomClasse+"' WHERE idEleve='"+INE+"'");
        database.close();

        JOptionPane.showMessageDialog(null, "L'élève a bien été modifié dans la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * methode permettant de supprimer un eleve de la base
     * @param nom
     * @param prenom
     */
    public void suppression(String nom, String prenom)
    {
        database.open();
        database.executeUpdate("DELETE FROM correspond WHERE idEleve=(SELECT idEleve FROM eleve WHERE nom='"+nom+"' AND prenom='"+prenom+"')");
        database.executeUpdate("DELETE FROM eleve WHERE nom='"+nom+"' AND prenom='"+prenom+"'");
        database.close();

        JOptionPane.showMessageDialog(null, "L'élève a bien été supprimé de la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    public void suppression(ArrayList<String> Eleves)
    {
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();
        boolean present;

        database.open();
        Colonne.add("idEleve");
        Donnees = database.executeQuery("SELECT idEleve FROM eleve WHERE idEleve != '0'", Colonne);
        database.close();

        for(int i = 0; i < Donnees.size(); i ++)
        {
            present = false;
            for(int j = 0; j < Eleves.size(); j = j+6)
            {
                if(Eleves.get(j+2).equals(Donnees.get(i))) present = true;
            }
            if(!present)
            {
                database.open();
                database.executeUpdate("DELETE FROM correspond WHERE idEleve='"+Donnees.get(i)+"'");
                database.executeUpdate("DELETE FROM eleve WHERE idEleve='"+Donnees.get(i)+"'");
                database.close();
            }
        }
    }
}
