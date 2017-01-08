import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import com.opencsv.*;
import com.sun.org.apache.xpath.internal.Arg;

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
			   String file_name = "Generation1";
			   CSVtoSQL client_t = new CSVtoSQL("Client", new String[] {"id_client","id_ville", "prenom_client", "nom_client",
					   "email_client","gender_client", "telephone_client", "iban_client", "abonnement_client"});
			   CSVtoSQL fournisseur_t = new CSVtoSQL("Fournisseur", new String[] {"id_fournisseur","id_ville", "nom_fournisseur", "slogan_fournisseur",
					   "devise_fournisseur","email_fournisseur", "iban_fournisseur", "telephone_fournisseur"});
			   CSVtoSQL produit_t = new CSVtoSQL("Produit", new String[] {"id_produit","id_fournisseur","prix_produit", "label_produit"});
			   CSVtoSQL localisation_t = new CSVtoSQL("Localisation", new String[] {"id_ville","nom_ville", "pays"});
			   CSVtoSQL commande_t = new CSVtoSQL("Commande", new String[] {"id_commande", "id_produit", "id_client","date_commande"});
			   
			   Vector<CSVtoSQL> vect_table = new Vector<CSVtoSQL>();
			   vect_table.add(client_t);
			   vect_table.add(fournisseur_t);
			   vect_table.add(produit_t);
			   vect_table.add(localisation_t);
			   vect_table.add(commande_t);
			   convertCSVtoSQL(file_name, vect_table);
					 
		}

		private static void convertCSVtoSQL(String nom_fic, Vector<CSVtoSQL> v_table) throws Exception{
			// TODO Auto-generated method stub
			String filePath = "Fichier/" + nom_fic + ".csv";
			CSVReader reader = new CSVReader(new FileReader(filePath));
		     try{
	    		String [] nextLine;
	    		int nb_attribut;
	    		//String id_four,id_prod, id_cli;
	    		Vector<String> vector_attributs = new Vector<String>(); 
	    		Vector<String> vector_valeurs = new Vector<String>(); 
	    		//Iterator<String > it_attribut;
	    		Iterator<CSVtoSQL> it_table;
	    		Iterator<String > it_valeur;
	    		Object element = new Object();
	    		Object t_name = new Object();
	    		File sqlfile = new File("FichierSQL.sql");
	    		FileWriter fw = new FileWriter(sqlfile);
	    		nextLine = reader.readNext();
	    		for(String valeur:nextLine){
	    			vector_attributs.add(valeur);
	    		}
	    		while ((nextLine = reader.readNext()) != null){
	    				//it_attribut = vector_attributs.iterator();
	    				it_table = v_table.iterator();
	    				it_valeur = vector_valeurs.iterator();
	                   for(String valeur:nextLine){
	                	   vector_valeurs.add(valeur);
	                   }
	                   it_valeur = vector_valeurs.iterator();
	                   for(int i=0; i<v_table.size(); i++){
	                	   if(it_table.hasNext()){
	                		   //t_name = it_table.next();
	                		   element = it_table.next().getNom_table();
	                		  // element = t_name.
	                	   }
	                	   
	                	   fw.write("insert into " + element + " (");
	                	   if(it_table.hasNext()){
	                	   nb_attribut = it_table.next().getNb_attributs(); 
	                	   for(int j=0; j<nb_attribut; j++){
	                		   if(it_table.hasNext()){
	                		   element = it_table.next().getVector().get(j);}
	                		   fw.write("'"+element + "', ");
	                		   
	                	   }
	                	   fw.write(element + ") values ( ");
	                	   for(int k =0; k<nb_attribut;k++){
	                		   if(it_valeur.hasNext()){
	                		   element = it_valeur.next();}
	                		   fw.write(element + ", ");
	                	   }
	                	   }
	                	
	                   
	                   fw.write(" \n"); 
     
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



