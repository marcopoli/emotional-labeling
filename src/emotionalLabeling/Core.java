package emotionalLabeling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;


public class Core {
	public static double getSimil(int emotion, String line) throws ClassNotFoundException, SQLException{
		//Prendo il centroide
		Connection con0 = DAO.conn();
		
		
		Vector<Double> fullcentr = new Vector<Double>();
		PreparedStatement pr0 = con0.prepareStatement("Select * from emofullcentr");
		ResultSet rs0 = pr0.executeQuery();
		if (rs0.next()) {
			for (int j = 1; j < 301; j++) {
				Double v = rs0.getDouble("f" + j);
				Double val = 0.0;

				try {
					val = fullcentr.get(j - 1);
				} catch (Exception e) {
				}

				double newVal = (val + v);

				
				//System.out.println(newVal);
				try {
					fullcentr.remove(j - 1);
					 
				} catch (Exception e) {
				}
				;

				fullcentr.add(j - 1, newVal);
				//instanceValue[j-1] = newVal;
			}
		}
		
		
		Twokenize tw = new Twokenize(line);
		
		List<String> tokens = tw.tokenizeRawTweetText(line);
		Vector<Double> messages = new Vector<Double>();

		// ADD values from db
		Connection con = DAO.conn();
		Statement st = con.createStatement();
				
		//Creo il vettore della stringa
		int count = 0;
		for (int k = 0; k < tokens.size(); k++) {
			
			if(tokens.get(k).length() > 2 ){
			//System.out.println(tokens.get(i));
			try {
				PreparedStatement pr = con.prepareStatement("Select * from emofull where f0=?");
				pr.setString(1, tokens.get(k));

				ResultSet rs3 = pr.executeQuery();

				if (rs3.next()) {
					for (int j = 1; j < 301; j++) {
						Double v = rs3.getDouble("f" + j);// - fullcentr.get(j-1);
						Double val = 0.0;

						try {
							val = messages.get(j - 1);
						} catch (Exception e) {
						}

						double newVal = (val + v);

						
						//System.out.println(newVal);
						try {
							messages.remove(j - 1);
							 
						} catch (Exception e) {
						}
						;

						messages.add(j - 1, newVal);
						//instanceValue[j-1] = newVal;
					}
					
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		
		if(messages.size() != 0){
			for(int j= 0; j < 300; j++){
				Double val = messages.get(j);
				Double newVal = val/count;
				 messages.remove(j);
				 messages.add(j, newVal);
			}
			
			//Se abbiamo un vettore valido estraiamo il centroide e calcoliamo la similarità
			Connection con1 = DAO.conn();
			Statement st1 = con1.createStatement();
			PreparedStatement pr = con1.prepareStatement("Select * from emo"+(emotion)+"_e6_centr LIMIT 1 OFFSET 6");
			
			
			ResultSet rs4 = pr.executeQuery();
			Vector<Double>  centroide = new Vector<Double> ();
			int count2 = 0;
			while(rs4.next()){
				for (int j = 1; j < 301; j++) {
					Double v = rs4.getDouble("f" + j);
					//v = v * peso;
					Double val = 0.0;

					try {
						val = centroide.get(j - 1);
					} catch (Exception e) {
					}

					double newVal = (val + v);

					try {
						centroide.remove(j - 1);
						 
					} catch (Exception e) {
					}

					centroide.add(j - 1, newVal);
				}
				count2++;
			}

			SimilaritaCoseno sim = new SimilaritaCoseno();
			
			double simil = sim.calculateTanimoto(messages, centroide);
		
			return simil;
			
		}
		
		return -1;
	}
}
