import java.io.File;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.util.Iterator;
	import java.util.Vector;
	import com.opencsv.*;

public class CSVtoJSON {


		/*String id_client;
		String ville_client;
		String prenom_client;
		String nom_client;
		String email_client;
		String gender_client;
		String telephone_client;
		String iban_client;
		String abonnement_client;
		String id_fournisseur;
		String id_ville_fournisseur;
		String nom_fournisseur;
		String slogan_fournisseur;
		String devise_fournisseur;
		String email_fournisseur;
		String iban_fournisseur;
		String telephone_fournisseur;
		String id_produit;
		String prix_produit;
		String label_produit;
		String id_ville;
		String nom_ville;
		String pays;
		String id_commande;
		String date_commande;*/
		
		
		
			public static void main (String[] args) throws Exception{	 
			     CSVReader reader = new CSVReader(new FileReader("Fichier/Generation1.csv"));
			     try{
		    		String [] nextLine;
		    		Vector<String> vector = new Vector<String>(); 
		    		Iterator<String > it;
		    		Object element = new Object();
		    		File jsonfile = new File("FichierJson.json");
		    		FileWriter fw = new FileWriter(jsonfile);
		    		nextLine = reader.readNext();
		    		for(String valeur:nextLine){
		    			vector.add(valeur);
		    		}
		    		while ((nextLine = reader.readNext()) != null){
		    				it = vector.iterator();
		    				/*if(it.hasNext()){
		    					it.next();
		    				} */
		    	    		fw.write("{ \n");
		                   for(String valeur:nextLine){
		                	   if(valeur != null){
		                		   if(it.hasNext()){
		                		   element = it.next();}
		                		   fw.write("\"" + element + ":\"" + valeur+ "\"");
		                		   if(it.hasNext() == true){
		                			   fw.write(", \n");
		    	                	   it.next();
		    	                		   
		                		   }
		                		   /*if(it.hasNext())
		                		   it.next();*/
		                	   }else{
		                		   if(it.hasNext())
		                		   it.next();
		                	   }
		                   }
		                   fw.write("}");
		                   if(reader.readNext() != null){
		                	   fw.write(", \n");
		                   }
		           }
		            reader.close();
		            fw.close();
		            System.out.println("good");
			     }catch (IOException e){
			    	 e.printStackTrace();
			     }
		     }
		
	
}
