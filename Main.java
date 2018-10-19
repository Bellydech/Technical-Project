import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import jxl.*;
import java.io.File;
import java.io.FileInputStream;


public class Main
{

	public static void main(String[] args) 
	{
		
		
		File f=new File("Excel/idfilms.xls");

	    try 
	    {
			Class.forName("org.postgresql.Driver");
		    System.out.println("Driver O.K.");
		    String url = "jdbc:postgresql://localhost:5454/cinema";
		    String user = "cinephile"; // Utili
		    String passwd = "";

		    Connection connection = DriverManager.getConnection(url, user, passwd);
		    System.out.println("Connexion à la BDD cinephile"); 

		    Workbook wb=Workbook.getWorkbook(f);
		    Sheet s=wb.getSheet(0);
		    int row=s.getRows()/5000;
		    int col=s.getColumns();

		        for (int i=1; i<row;i++)
		        {
		            for (int j=0;j < col ;j++)
		            {
		                Cell c=s.getCell(j,i);
		                String cellule= c.getContents();
		                int Virg1=cellule.indexOf(",");
		                int Virg2=cellule.lastIndexOf(",");

		                String id1=cellule.substring(0,Virg1);
		                String id2=cellule.substring(Virg1+1,Virg2);
		                String id3=cellule.substring(Virg2+1,cellule.length());

		                //Synopsis IMDB

		               	Document d=Jsoup.connect("https://www.imdb.com/title/tt"+id2).timeout(6000).get(); 
		               	String synopsis=d.select("div#title-overview-widget div.summary_text").text();
		               	

		               	//Synopsis TMBD

		               		

		               	//System.out.println("BEFORE : ");
						//System.out.println("Synopsis du film :\n" + synopsis +"\n\n\n");

		               	//Découpage du synopsis en enlevant les apostrophes qui créent des problèmes à l'insertion dans la base de données.
		               	
						int curseur=synopsis.indexOf("'");

						while(curseur != -1)
						{
							String synopsis_part1=synopsis.substring(0,curseur);
							String synopsis_part2=synopsis.substring(curseur+1,synopsis.length());
							
							synopsis=synopsis_part1+" "+synopsis_part2;
							curseur=synopsis.indexOf("'");	
						}

		               	//System.out.println("AFTER : ");
		               	//System.out.println("Synopsis du film :\n" + synopsis +"\n\n\n");




						String sqlQuery = "INSERT INTO synopsis (id_film, synopsis_film) VALUES("+i+","+"'"+synopsis+"'"+")";
		        		PreparedStatement statement = connection.prepareStatement(sqlQuery);
		        		Statement stmt = connection.createStatement();
		        		stmt.executeUpdate(sqlQuery);
		            }
		        }

			System.out.println("Requête envoyée"); 
	    }

	    catch (Exception e) 
	    {
	      e.printStackTrace();
	    }
	}
}
