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
		
			
			public static void convertCSVtoJSON(String nom_fic)  throws Exception{
				String filePath = "Fichier/" + nom_fic + ".csv";
				CSVReader reader = new CSVReader(new FileReader(filePath));
			     try{
		    		String [] nextLine;
		    		Vector<String> vector = new Vector<String>(); 
		    		Iterator<String > it;
		    		Object element = new Object();
		    		//Creation fichier Json
		    		File jsonfile = new File("FichierJson.json");
		    		FileWriter fw = new FileWriter(jsonfile);
		    		//Lecture premiere ligne du CSV
		    		nextLine = reader.readNext();
		    		//Sauvegarde de la ligne dans un vecteur (attributs)
		    		for(String valeur:nextLine){
		    			vector.add(valeur);
		    		}
		    		//Tant que la lecture n'est pas finie, lire la ligne
		    		while ((nextLine = reader.readNext()) != null){
		    				it = vector.iterator();
		    	    		fw.write("{ \n");
		    	    		//pour chaque champs de la ligne
		                   for(String valeur:nextLine){
		                	   //si la valeur n'est pas nulle l'écrire dans le fichier 
		                	   if(valeur.length() != 0){
		                		   if(it.hasNext()){
		                		   element = it.next();}
		                		   fw.write("\"" + element + ":\"" + valeur+ "\"");
		                		   if(it.hasNext() == true){
		                			   fw.write(", \n");	   
		                		   }
		                	   }else{ //sinon avancer uniquement le pointeur d'attribut
		                		   if(it.hasNext())
		                		   it.next();
		                	   }
		                   }
		                   fw.write("}, \n");
         
		           }
		            reader.close();
		            fw.close();
		            System.out.println("JSON file generated");
			     }catch (IOException e){
			    	 e.printStackTrace();
			     }
				
			}
			
			public static void main (String[] args) throws Exception{	 
				   String file_name = "Generation2";
				   convertCSVtoJSON(file_name);
						 
			}
			
}



