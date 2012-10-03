package attestation;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.security.*;

/**
 *
 * @author Nathan
 */
public class Connexion {
    
    private Database database;
    private ArrayList<String> Personnel;
    private ArrayList<String> Colonne;

    public Connexion() {

        database = new Database();
        Personnel = new ArrayList<String>();
        Colonne = new ArrayList<String>();
    }

    /**
     * cette methode permet de hasher une chaine de caractere grace a l'algorithme MD5
     * @param password
     * @return String
     */
    public static String MD5(String password)
    {
        byte[] uniqueKey = password.getBytes();
        byte[] hash = null;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new Error("No MD5 support in this VM.");
        }

        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++)
        {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1)
            {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }
            else
                hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }

    /**
     * methode qui se connecte a la base de donnees, elle recupere les informations necessaires et vérifie si la ConnexionFrame au logiciel est possible
     * cette methode verifie aussi la nature de la personne qui se connecte, autrement dit un enseignant ou un responsable
     */
    public void authentification(String Lognom ,String Logprenom, String Mdp)
    {
        try
        {
        database.open();

        Colonne.add("nom");
        Colonne.add("prenom");
        Colonne.add("mdp");
        Colonne.add("nomMatiere");

        Personnel = database.executeQuery("select nom,prenom,mdp,matiere.nomMatiere FROM enseignant,matiere WHERE matiere.idMatiere=enseignant.idmatiere AND nom='"+Lognom+"' AND prenom='"+Logprenom+"' AND mdp = '"+MD5(Mdp)+"'",Colonne);
        database.close();

                if(Lognom.length() != 0 && Logprenom.length() != 0 && Mdp.length() != 0)
                {
                    int i = 0;

                    if(Personnel.size() != 0)
                    {
                        if(Lognom.equals(Personnel.get(i)) && Logprenom.equals(Personnel.get(i+1)) && MD5(Mdp).equals(Personnel.get(i+2)))
                        {
                            if(Personnel.get(i+3).equals("RESPONSABLE"))
                            {
                                new MenuResponsableFrame(Lognom,Logprenom);
                            }
                            else
                            {
                                new MenuEnseignantFrame(Lognom,Logprenom,Personnel.get(i+3));
                            }
                        }
                        i = i + 3;
                    }

                    else
                    {
                        JOptionPane.showMessageDialog(null,"Désolé mais la connexion a échoué, veuillez vérifier votre identifiant et votre mot de passe", "Echec de connexion", JOptionPane.ERROR_MESSAGE);
                        new ConnexionFrame();
                    }

                }

                else
                {
                    JOptionPane.showMessageDialog(null,"Désolé mais la connexion a échoué, veuillez vérifier votre identifiant et votre mot de passe", "Echec de connexion", JOptionPane.ERROR_MESSAGE);
                    new ConnexionFrame();
                }
        }
        catch(java.lang.NullPointerException e) {}
    }
}
