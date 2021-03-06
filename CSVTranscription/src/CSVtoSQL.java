import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import com.opencsv.*;


public class CSVtoSQL {

	String nom_table;
	Vector<String> vect_attributs;
	
	public CSVtoSQL(String name, String [] args){
		nom_table = name;
		vect_attributs = new Vector<String>();
		for(int i=0;i<args.length;++i)
		  {
		     vect_attributs.add(args[i]);
		  }
	}
	
	public CSVtoSQL(){
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
			   String file_name = "Generation2projet";
			   //mise en place des tables et de leurs attributs
			   CSVtoSQL client_t = new CSVtoSQL("Client", new String[] {"id_client","ville_client", "prenom_client", "nom_client",
					   "email_client","gender_client", "telephone_client", "iban_client", "abonnement_client"});
			   CSVtoSQL fournisseur_t = new CSVtoSQL("Fournisseur", new String[] {"id_fournisseur","ville_fournisseur", "nom_fournisseur", "slogan_fournisseur",
					   "devise_fournisseur","email_fournisseur", "iban_fournisseur", "telephone_fournisseur"});
			   CSVtoSQL produit_t = new CSVtoSQL("Produit", new String[] {"id_produit","id_fournisseur","couleur_produit","prix_produit", "label_produit"});
			   //CSVtoSQL localisation_t = new CSVtoSQL("Localisation", new String[] {"id_ville","nom_ville", "pays"});
			   CSVtoSQL commande_t = new CSVtoSQL("Commande", new String[] {"id_commande", "id_produit", "id_client","date_commande"});
			   
			   Vector<CSVtoSQL> vect_table = new Vector<CSVtoSQL>();
			   vect_table.add(client_t);
			   vect_table.add(fournisseur_t);
			   vect_table.add(produit_t);
			   //vect_table.add(localisation_t);
			   vect_table.add(commande_t);
			   convertCSVtoSQL(file_name, vect_table);
					 
		}

		private static void convertCSVtoSQL(String nom_fic, Vector<CSVtoSQL> v_table) throws Exception{
			//r�cuperation du CSV
			String filePath = "Fichier/" + nom_fic + ".csv";
			CSVReader reader = new CSVReader(new FileReader(filePath));
		     try{
	    		String [] nextLine;
	    		int nb_attribut;
	    		String id_four,id_prod, id_cli;
	    		Vector<String> vector_attributs = new Vector<String>(); 
	    		Vector<String> vector_valeurs = new Vector<String>();
	    		Iterator<CSVtoSQL> it_table;
	    		Iterator<String > it_valeur;
	    		Object element = new Object();
	    		CSVtoSQL table = new CSVtoSQL();
	    		//Creation du fichier SQL
	    		File sqlfile = new File("FichierSQL.sql");
	    		FileWriter fw = new FileWriter(sqlfile);
	    		//lecture de la 1ere ligne
	    		nextLine = reader.readNext();
	    		//Sauvegarde de la ligne dans un vecteur (attributs)
	    		for(String valeur:nextLine){
	    			vector_attributs.add(valeur);
	    		}
	    		//tant que la lecture n'est pas finie
	    		while ((nextLine = reader.readNext()) != null){
	    				it_table = v_table.iterator();
	    				 vector_valeurs.clear();
	    				//r�cuperer les champs de la ligne dans un vecteur
	                   for(String valeur:nextLine){
	                	   vector_valeurs.add(valeur);
	                   }
	                   it_valeur = vector_valeurs.iterator();
	                   //sauvegarde de l'id_client, id_founisseur et id_produit
	                   id_four = vector_valeurs.get(9);
	                   id_cli = vector_valeurs.get(0);
	                   id_prod = vector_valeurs.get(17);
	                   //pour chaque table
	                   for(int i=0; i<v_table.size(); i++){
	                	   if(it_table.hasNext()){
	                		   table = it_table.next();
	                		   //r�cup�rer le nom de la table
	                		   element = table.getNom_table();
	                	   	                	   
	                		   fw.write("insert into " + element + " (");
	                	   
		                	   nb_attribut = table.getNb_attributs(); 
		                	   for(int j=0; j<nb_attribut; j++){
		                		   //r�cuperer les noms des attributs
		                		   element = table.getVector().get(j);
		                		   fw.write(""+element);
		                		   if(j!= (nb_attribut-1)){
	                				   fw.write(", ");
	                			   }
	                		   
	                	   }
	                	   fw.write(") values (");
	                	   for(int k =0; k<nb_attribut;k++){
	                		   if(it_valeur.hasNext()){
	                			 //Si on rempli le 2eme champ de la table produit alors rajouter le champ id_fournisseur
	    	                	   if((table.getNom_table() == "Produit") && (k==1)){
	    	                		   fw.write("'"+id_four + "'");
	    	                	   }else
	    	                	 //Si on rempli le 2eme champ de la table commande alors rajouter le champ id_produit
	   	                	    if((table.getNom_table() == "Commande") && (k==1)){
	   	                		   fw.write("'"+ id_prod+ "'");
	   	                	    	}else //Si on rempli le 3eme champ de la table commande alors rajouter le champ id_produit
	   	                	    		if((table.getNom_table() == "Commande") && (k==2)){
	   	                	    		fw.write("'"+ id_cli+ "'");
	   	                	    	}else{ 
	                			   //Sinon recuperer les valeurs des attributs 'normaux' (ie non cl�-�trangere)
	                			   element = it_valeur.next();
	                			   fw.write("'"+element + "'");
	   	                	    	}
	                			   if(k!= (nb_attribut-1)){
	                				   fw.write(",");
	                			   }
	                		   }
	                	   }
	                	   
	                	 
	                	   fw.write(")\n"); 
	                	  
	                	   }
	                   fw.write(" \n\n"); 
     
	                   }
	    		}
	            reader.close();
	            fw.close();
	            System.out.println("SQL file generated");
		     }catch (IOException e){
		    	 e.printStackTrace();
		     }
		}
		
}



