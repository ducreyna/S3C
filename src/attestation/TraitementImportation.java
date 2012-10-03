package attestation;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Nathan
 */
public class TraitementImportation {

    private AjoutEleves ajoutEleves;
    private ArrayList<String> Eleves;
    
    public TraitementImportation(ArrayList<String> Eleves)
    {
        ajoutEleves = new AjoutEleves();
        this.Eleves = Eleves;
    }

    /**
     * methode permettant de filtrer les informations concernant un eleve
     */
    public void filtreEleves()
    {
        ArrayList<String> ElevesDetails = new ArrayList<String>();
        ArrayList<String> Classes = ajoutEleves.recupererClasses();
        String eleve = new String();

        for(int i = 0; i < Eleves.size(); i++)
        {
            eleve = Eleves.get(i);

            int indexDebut = 0;
            for(int j = 0; j < eleve.length(); j++)
            {
                if(eleve.charAt(j) == ';')
                {
                    ElevesDetails.add(eleve.substring(indexDebut, j).trim());
                    indexDebut = j+1;
                }
                if(j == eleve.length()-1 && eleve.charAt(j) != ';')
                {
                    ElevesDetails.add(eleve.substring(indexDebut, j).trim());
                }
                else if(j == eleve.length()-1 && eleve.charAt(j) == ';') ElevesDetails.add("");
            }
        }

        String langue;
        for(int k = 4; k < ElevesDetails.size(); k = k+6)
        {
            langue = ElevesDetails.get(k);
            ElevesDetails.remove(ElevesDetails.get(k));
            for(int j = 0; j < langue.length(); j++)
            {
                if(langue.charAt(j) == ' ')
                {
                    langue = langue.substring(0, j);
                }
            }
            ElevesDetails.add(k, langue);

            if(ElevesDetails.get(k+1).length() != 0)
            {
                langue = ElevesDetails.get(k+1);
                ElevesDetails.remove(ElevesDetails.get(k+1));
                for(int j = 0; j < langue.length(); j++)
                {
                    if(langue.charAt(j) == ' ')
                    {
                        langue = langue.substring(0, j);
                    }
                }
                ElevesDetails.add(k+1, langue);
            }
        }

        for(int k = 3; k < ElevesDetails.size(); k = k+6)
        {
            for(int i = 0; i < Classes.size(); i++)
            {
                if(ElevesDetails.get(k).equals(Classes.get(i).substring(2)))
                {
                    ElevesDetails.remove(ElevesDetails.get(k));
                    ElevesDetails.add(k, Classes.get(i));
                }
            }
        }

        for(int k = 0; k < ElevesDetails.size(); k = k+6)
        {
            ajoutEleves.ajout(ElevesDetails.get(k+2), ElevesDetails.get(k), ElevesDetails.get(k+1), ElevesDetails.get(k+4), ElevesDetails.get(k+5), ElevesDetails.get(k+3),true);
        }
        ajoutEleves.suppression(ElevesDetails);
        JOptionPane.showMessageDialog(null, "Le fichier a été importé avec succès", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }
}
