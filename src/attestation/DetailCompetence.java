package attestation;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Nathan
 */
public class DetailCompetence {

    private Database database;

    public DetailCompetence()
    {
        database = new Database();
    }

    /**
     * methode permettant d'afficher les details des validations suivant les categories
     * @param choix
     * @param CompetenceResultat
     * @param jTable1
     */
    public void afficherDetails(String choix,ArrayList<String> CompetenceResultat,javax.swing.JTable jTable1)
    {
        ArrayList<String> Categorie = new ArrayList<String>();
        ArrayList<String> Donnees = new ArrayList<String>();
        ArrayList<String> Colonne = new ArrayList<String>();

        // reinitialiser les cellules du tableau
        for(int j = 0; j < jTable1.getRowCount(); j++)
            {
                jTable1.setValueAt(null,j,0);
                jTable1.setValueAt(null,j,1);
            }

        for(int i = 0; i < CompetenceResultat.size(); i++)
        {
            if(CompetenceResultat.get(i).equals(choix))
            {
                try
                {
                    int j = i;
                    while((!CompetenceResultat.get(j-1).equals("Validé") || !CompetenceResultat.get(j-1).equals("Refusé")) && !CompetenceResultat.get(j).equals("Compétence "+(Integer.parseInt(choix.substring(11,12))+1)))
                    {
                        Categorie.add(CompetenceResultat.get(j+1));
                        Categorie.add(CompetenceResultat.get(j+2));
                        j = j + 2;
                    }
                    Categorie.remove(Categorie.size()-1);
                    Categorie.remove(Categorie.size()-1);

                    break;
                }
                catch(java.lang.ArrayIndexOutOfBoundsException e)
                {
                    int j = i;
                    while(!CompetenceResultat.get(j).equals("Compétence "+(Integer.parseInt(choix.substring(11,12))+1)))
                    {
                        Categorie.add(CompetenceResultat.get(j+1));
                        Categorie.add(CompetenceResultat.get(j+2));
                        j = j + 2;
                    }
                    Categorie.remove(Categorie.size()-1);
                    Categorie.remove(Categorie.size()-1);
                    break;
                }
                catch(java.lang.IndexOutOfBoundsException e) {}
            }
            else if(choix.equals("Compétence 2 LV1"))
            {
                for(int j = CompetenceResultat.indexOf("5"); j <= CompetenceResultat.indexOf("9")+3; j = j+3)
                {
                    Categorie.add(CompetenceResultat.get(j));
                    Categorie.add(CompetenceResultat.get(j+1));
                
                }
                Categorie.remove(Categorie.size()-1);
                Categorie.remove(Categorie.size()-1);
                break;
            }
            else if(choix.equals("Compétence 2 LV2"))
            {
                for(int j = CompetenceResultat.indexOf("5"); j <= CompetenceResultat.indexOf("9")+3; j = j+3)
                {
                    Categorie.add(CompetenceResultat.get(j));
                    Categorie.add(CompetenceResultat.get(j+2));

                }
                Categorie.remove(Categorie.size()-1);
                Categorie.remove(Categorie.size()-1);
                break;
            }
        }

        database.open();
        Colonne.add("idCategorie");
        Colonne.add("nom");

        Donnees = database.executeQuery("SELECT idCategorie,nom FROM categorie;", Colonne);
        database.close();

        int row = 0;
        for(int i = 0; i < Donnees.size(); i = i+2)
        {
            for(int j = 0; j < Categorie.size(); j = j+2)
            {
                if(Donnees.get(i).equals(Categorie.get(j)))
                {
                    jTable1.setValueAt(Donnees.get(i+1),row,0);
                    jTable1.setValueAt(Categorie.get(j+1),row,1);
                    row ++;
                }
            }
        }

        // centrer le contenu des cellules des colonnes 1 et 2
        DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
        custom.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        for (int i = 0 ; i < jTable1.getRowCount() ; i++)
        {
            jTable1.getColumnModel().getColumn(1).setCellRenderer(custom);
        }
    }
}
