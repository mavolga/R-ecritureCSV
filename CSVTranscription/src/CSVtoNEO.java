import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import com.opencsv.CSVReader;

public class CSVtoNEO {


	

	String nom_table;
	Vector<String> vect_attributs;
	
	public CSVtoNEO(String name, String [] args){
		nom_table = name;
		vect_attributs = new Vector<String>();
		for(int i=0;i<args.length;++i)
		  {
		     vect_attributs.add(args[i]);
		  }
	}
	
	public CSVtoNEO(){
		vect_attributs = new Vector<String>();
	}
	
	public int getNb_attributs(){
		return vect_attributs.size();
	}
	
	public String getNom_table(){
		return nom_table;
	}
	public Vector<String> getVector(){
		return vect_attributs;
	}
	

		
		public static void main (String[] args) throws Exception{	 
			   String file_name = "Generation2";
			   //mise en place des tables et de leurs attributs
			   CSVtoSQL client_t = new CSVtoSQL("Client", new String[] {"id_client","ville_client", "prenom_client", "nom_client",
					   "email_client","gender_client", "telephone_client", "iban_client", "abonnement_client"});
			   CSVtoSQL fournisseur_t = new CSVtoSQL("Fournisseur", new String[] {"id_fournisseur","ville_fournisseur", "nom_fournisseur", "slogan_fournisseur",
					   "devise_fournisseur","email_fournisseur", "iban_fournisseur", "telephone_fournisseur"});
			   CSVtoSQL produit_t = new CSVtoSQL("Produit", new String[] {"id_produit","couleur_produit","prix_produit", "label_produit"});
			   //CSVtoSQL localisation_t = new CSVtoSQL("Localisation", new String[] {"id_ville","nom_ville", "pays"});
			   CSVtoSQL commande_t = new CSVtoSQL("Commande", new String[] {"id_commande","date_commande"});
			   
			   Vector<CSVtoSQL> vect_table = new Vector<CSVtoSQL>();
			   vect_table.add(client_t);
			   vect_table.add(fournisseur_t);
			   vect_table.add(produit_t);
			   //vect_table.add(localisation_t);
			   vect_table.add(commande_t);
			   convertCSVtoSQL(file_name, vect_table);
					 
		}

		private static void convertCSVtoSQL(String nom_fic, Vector<CSVtoSQL> v_table) throws Exception{
			//récuperation du CSV
			String filePath = "Fichier/" + nom_fic + ".csv";
			CSVReader reader = new CSVReader(new FileReader(filePath));
		     try{
	    		String [] nextLine;
	    		int nb_attribut;
	    		String id_four,id_prod, id_cli, id_commande;
	    		Vector<String> vector_attributs = new Vector<String>(); 
	    		Vector<String> vector_valeurs = new Vector<String>();
	    		Iterator<CSVtoSQL> it_table;
	    		Iterator<String > it_valeur;
	    		Iterator<String > it_attribut;
	    		String element = new String();
	    		String att = new String();
	    		CSVtoSQL table = new CSVtoSQL();
	    		//Creation du fichier SQL
	    		File neofile = new File("FichierNEO.txt");
	    		FileWriter fw = new FileWriter(neofile);
	    		//lecture de la 1ere ligne
	    		nextLine = reader.readNext();
	    		//Sauvegarde de la ligne dans un vecteur (attributs)
	    		for(String valeur:nextLine){
	    			vector_attributs.add(valeur);
	    		}
	    		//tant que la lecture n'est pas finie
	    		while ((nextLine = reader.readNext()) != null){
	    				it_table = v_table.iterator();
	    				it_attribut = vector_attributs.iterator();
	    				 vector_valeurs.clear();
	    				//récuperer les champs de la ligne dans un vecteur
	                   for(String valeur:nextLine){
	                	   vector_valeurs.add(valeur);
	                   }
	                   it_valeur = vector_valeurs.iterator();
	                   //sauvegarde de l'id_client, id_founisseur et id_produit
	                   id_four = vector_valeurs.get(9);
	                   id_cli = vector_valeurs.get(0);
	                   id_prod = vector_valeurs.get(17);
	                   id_commande = vector_valeurs.get(21);
	                   //pour chaque table
	                   for(int i=0; i<v_table.size(); i++){
	                	   if(it_table.hasNext()){
	                		   table = it_table.next();
	                		   //récupérer le nom de la table
	                		   element = table.getNom_table();
	                	   	   if(element == "Client"){
	                	   		fw.write("create (cl_"+ id_cli + ":" + element + "{");
	                	   	   }else if(element == "Fournisseur"){
	                	   		fw.write("create (f_"+ id_four + ":" + element + "{");
	                	   	   }else if(element == "Produit"){
	                	   		   fw.write("create (p_"+ id_prod + ":" + element + "{");
	                	   	   }else{
	                	   		 fw.write("create (co_"+ id_commande + ":" + element + "{");
	                	   	   }
	                	   
		                	   nb_attribut = table.getNb_attributs(); 
		                	   for(int j=0; j<nb_attribut; j++){
		                		   //récuperer les noms des attributs
		                		   if(it_valeur.hasNext()){
		                			   element = it_valeur.next();
		                			   if(element.length() != 0){
		                				   if((j!= 0)){
	                				   			fw.write(", ");
	                				   		}
		                				   if(it_attribut.hasNext()){
		                					   att = it_attribut.next();
		                				   		fw.write(""+att+ ":'" + element + "'");
		                				   		
		                				   }
		                			   }else if(it_attribut.hasNext()){
				                			   att = it_attribut.next();
		                				   
		                			   }
		                				  
		                		   }
	                	   }
	                	   fw.write("})");
	                	   fw.write("\n\n");
	                	 
	                	  
	                	   }
	                 
	                   }
	                   fw.write("\n");
	                   fw.write("create (co_"+ id_commande+")-[:REALISE_PAR]->(cl_" + id_cli+"), ");
	                   fw.write("(co_" +id_commande+")-[:CONCERNE]->(p_" + id_prod+"), "); 
	                   fw.write("(p_"+id_prod+")-[:DELIVRE_PAR]->(f_" + id_four+")"); 
	                   fw.write(" \n\n"); 
	    		}
	            reader.close();
	            fw.close();
	            System.out.println("Text file generated");
		     }catch (IOException e){
		    	 e.printStackTrace();
		     }
		}
}
