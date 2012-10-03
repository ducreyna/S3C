package attestation;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author nathan
 */
public class Database {

 private Connection cnt;
 private DatabaseMetaData dma;
 private String productName;
 private String productVersion;

 public Database()
 {
    try{
        Class.forName("com.mysql.jdbc.Driver");
       }
    catch (Exception e){ JOptionPane.showMessageDialog(null, "Erreur lors du chargement du driver :"+e.getMessage(), "Exception SQL", JOptionPane.ERROR_MESSAGE); }
 }

 /**
  * methode permettant l'ouverture de la base de donnees
  */
 public void open()
 {
    try{
        cnt = DriverManager.getConnection("jdbc:mysql:///soclecommun","root","");
        //cnt = DriverManager.getConnection("jdbc:mysql://clg-degaulle-jeumont.no-ip.com:3306/soclecommun","soclecommun","Lermit2008");
        dma = cnt.getMetaData();
        productName = dma.getDatabaseProductName();
        productVersion = dma.getDatabaseProductVersion();
       }
    catch (SQLException e){ JOptionPane.showMessageDialog(null, "Echec d'ouverture :"+e.getMessage(), "Exception SQL", JOptionPane.ERROR_MESSAGE); }
 }

 /**
  * methode permettant de fermer la base de donnees après consultation
  */
 public void close()
 {
    try{
        cnt.close();
       }
    catch (SQLException e){ JOptionPane.showMessageDialog(null, "Echec lors de la fermeture :"+e.getMessage(), "Exception SQL", JOptionPane.ERROR_MESSAGE); }
 }

 /**
  * methode permettant d'executer une requete SQL sur la base de donnees comme SELECT,INSERT,DELETE
  * @param sql  requete SQL sous forme d'une chaine de caractere(String)
  * @param column  liste des colonnes qui vont etre consultees lors de la requete SQL
  * @return ArrayList<String> cela correspond aux donnees recuperees apres l'execution de la requete
  */
 public ArrayList<String> executeQuery(String sql, ArrayList<String> column)
 {
    ArrayList<String> rqt = new ArrayList<String>();
    try{
        Statement stmt = cnt.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        while(res.next())
        {
            for(int i =0; i < column.size(); i++)
            {
              rqt.add(res.getString(column.get(i)));
            }
        }
        res.close();
      }
    catch (SQLException e){ JOptionPane.showMessageDialog(null,"Une erreur est survenue lors de l'exécution de la requête SQL: ", "Exception SQL", JOptionPane.ERROR_MESSAGE); }

 return rqt;
 }

 /**
  * methode permettant de faire une mise à jour sur la base de donnees a l'aide de UPDATE
  * @param sql  requete SQL sous forme d'une chaine de caractere(String)
  */
 public boolean executeUpdate(String sql)
 {
    try{
        Statement stmt = cnt.createStatement();
        stmt.executeUpdate(sql);
        return true;
        }
    catch (SQLException e){ JOptionPane.showMessageDialog(null,"Une erreur est survenue lors de la mise à jour de la base de données: "+e.getMessage(), "Exception SQL", JOptionPane.ERROR_MESSAGE); return false;}
 }

 /**
  * methode permettant de savoir sous quel type de produit on travaille, ici MySQL
  * @return String correspondant au nom du produit
  */
 public String getProductName(){ return productName; }

 /**
  * methode permettant de savoir sous quelle version du produit(MySQL) on travaille
  * @return String correspondant a la version du produit
  */
 public String getProductVersion(){ return productVersion; }

}