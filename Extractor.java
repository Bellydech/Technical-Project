import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.*;
import java.io.File;
import java.io.FileInputStream;

public class Extractor {

	public static void main(String[] args) throws IOException {

		File f=new File("Excel/idfilms.xls");

        try 
        {
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
                    //System.out.println(cellule);
                    String id1=cellule.substring(0,Virg1);
                    String id2=cellule.substring(Virg1+1,Virg2);
                    String id3=cellule.substring(Virg2+1,cellule.length());
                    //System.out.println(id1);
                    //System.out.println(id2);
                    //System.out.println(id3);

                    //Synopsis IMDB

                    Document d=Jsoup.connect("https://www.imdb.com/title/tt"+id2).timeout(6000).get(); 
                    String synopsis=d.select("div#title-overview-widget div.summary_text").text();
					//String title=d.select("div#title-overview-widget div.title_wrapper").text();
					//System.out.println("Information sur le film :\n" + title);
					System.out.println("Synopsis du film :\n" + synopsis +"\n\n\n");
                }
            }
        
        }

        catch(Exception e) 
        {
          e.printStackTrace();
        }

	}

}
