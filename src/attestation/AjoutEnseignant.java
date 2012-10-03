package attestation;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class AjoutEnseignant {

    private Database database;

    public AjoutEnseignant()
    {
        database = new Database();
    }

    /**
     * methode permettant d'ajouter un enseignant dans la base
     * @param nom
     * @param prenom
     * @param mdp
     * @param matiere
     * @return true si l'ajout a reussi et false si ca n'a pas fonctionne
     */
    public boolean ajout(String nom, String prenom, String mdp, String matiere)
    {
        boolean resultat;
        
        database.open();
        resultat = database.executeUpdate("INSERT INTO enseignant VALUES (default,'"+nom+"','"+prenom+"',MD5('"+mdp+"'),(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"'));");
        database.close();
        if(resultat) 
        {
            JOptionPane.showMessageDialog(null, "L'enseignant a bien été ajouté dans la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la modification de la base", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * methode permettant de modifier les champs renseignants un enseignant
     * @param ancienNom
     * @param ancienPrenom
     * @param ancienneMatiere
     * @param nom
     * @param prenom
     * @param mdp
     * @param matiere
     * @return true si la modification a reussi et false si ca n'a pas fonctionne
     */
    public boolean modification(String ancienNom, String ancienPrenom, String ancienneMatiere, String nom, String prenom, String mdp, String matiere)
    {
        boolean resultat;

        if(mdp.length() != 0)
        {
            database.open();
            resultat = database.executeUpdate("UPDATE enseignant SET nom='"+nom+"',prenom='"+prenom+"',mdp=MD5('"+mdp+"'),idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"') WHERE nom='"+ancienNom+"' AND prenom='"+ancienPrenom+"' AND idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+ancienneMatiere+"')");
            database.close();
        }
        else
        {
            database.open();
            resultat = database.executeUpdate("UPDATE enseignant SET nom='"+nom+"',prenom='"+prenom+"',idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"') WHERE nom='"+ancienNom+"' AND prenom='"+ancienPrenom+"' AND idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+ancienneMatiere+"')");
            database.close();
        }
        
        if(resultat)
        {
            JOptionPane.showMessageDialog(null, "L'enseignant a bien été modifié dans la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la modification de la base", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * methode permattant de supprimer un enseignant de la base de donnees
     * @param nom
     * @param prenom
     * @param matiere
     */
    public void suppression(String nom, String prenom, String matiere)
    {
        boolean resultat;

        database.open();
        database.executeUpdate("DELETE FROM occupe WHERE idEnseignant=(SELECT idEnseignant FROM enseignant WHERE nom='"+nom+"' AND prenom='"+prenom+"' AND idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"'))");
        database.close();

        database.open();
        resultat = database.executeUpdate("DELETE FROM enseignant WHERE nom='"+nom+"' AND prenom='"+prenom+"' AND idmatiere=(SELECT idMatiere FROM matiere WHERE nomMatiere='"+matiere+"')");
        database.close();
        
        if(resultat)
        {
            JOptionPane.showMessageDialog(null, "L'enseignant a bien été supprimé de la base", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de la suppression de l'enseignant de la base", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
