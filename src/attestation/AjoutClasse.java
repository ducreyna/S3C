package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class AjoutClasse {

    private Database database;

    public AjoutClasse()
    {
       database = new Database();
    }

    /**
     * methode permettant d'ajouter une classe dans la base
     * @param nomClasse
     * @return true si l'ajout a reussi
     */
    public boolean ajout(String nomClasse)
    {
        boolean resultat;

        database.open();
        resultat = database.executeUpdate("INSERT INTO classe VALUES ('"+nomClasse+"');");
        database.close();
        if(resultat)
        {
            JOptionPane.showMessageDialog(null, "La classe a bien été ajoutée dans la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'ajout du professeur principal de la classe dans la base", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * methode permettant de supprimer une classe
     * @param nomClasse
     */
    public void suppression(String nomClasse)
    {
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();
        boolean resultat1 = false;
        boolean resultat2 = false;

        database.open();
        Colonne.add("idEleve");
        Donnees = database.executeQuery("SELECT idEleve FROM eleve WHERE nomClasse='"+nomClasse+"'", Colonne);
        database.close();

        if(Donnees.size() == 0)
        {
            database.open();
            resultat1 = database.executeUpdate("DELETE FROM classe WHERE nomClasse='"+nomClasse+"'");
            database.close();
        }
        else
        {
            for(int i = 0; i < Donnees.size(); i++)
            {
                database.open();
                database.executeUpdate("UPDATE eleve SET nomClasse='LIBRE' WHERE idEleve='"+Donnees.get(i)+"'");
                database.close();
            }

            database.open();
            database.executeUpdate("DELETE FROM occupe WHERE nomClasse='"+nomClasse+"'");
            resultat2 = database.executeUpdate("DELETE FROM classe WHERE nomClasse='"+nomClasse+"'");
            database.close();
        }

        if(resultat1 || resultat2)
        JOptionPane.showMessageDialog(null, "La classe a bien été supprimée de la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        else JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la suppression de la classe", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
