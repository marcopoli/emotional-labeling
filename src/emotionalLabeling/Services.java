package emotionalLabeling;


import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.core.*;

@Path("analyze")
public class Services{

	@POST
	@Path("emotionalLabeling")
	@Produces(MediaType.APPLICATION_JSON)
	public Response emotionalLabeling(@DefaultValue("") @FormParam("text") String text, @DefaultValue("en") @FormParam("lang") String lang) throws Exception {
        if(lang.equalsIgnoreCase("it")){
        	text = Translator.translateToEn(text);
        }
        System.out.println(text);
		int maxPos = 0;
		double maxVal = 0;
		
		for(int i = 1; i<7;i++){
			double score = Core.getSimil(i, text);
			System.out.println(score);
			if(score > maxVal){
				maxVal = score;
				maxPos = i;
			}
		}
		
		if(maxPos == 1){
			return Response.status(200).entity("joy").build();
		}
		if(maxPos == 2){
			return Response.status(200).entity("sad").build();
		}
		if(maxPos == 3){
			return Response.status(200).entity("fear").build();
		}
		if(maxPos == 4){
			return Response.status(200).entity("disgust").build();
		}
		if(maxPos == 5){
			return Response.status(200).entity("anger").build();
		}
		if(maxPos == 6){
			return Response.status(200).entity("surprise").build();
		}
		
		
		return Response.status(200).entity("{\"error\":\"not found\"}").build();
	}
	
	
	@POST
	@Path("moodValues")
	@Produces(MediaType.APPLICATION_JSON)
	public Response moodValues(@DefaultValue("") @FormParam("text") String text, @DefaultValue("en") @FormParam("lang") String lang) throws Exception {
		if(lang.equalsIgnoreCase("it")){
        	text = Translator.translateToEn(text);
        }
		int maxPos = 0;
		double maxVal = 0;
		double sum = 0;
		
		double valjoy = 0;
		double valsad = 0;
		double valfear = 0;
		double valdisgust = 0;
		double valanger = 0;
		double vasurprise = 0;
		
		for(int i = 1; i<7;i++){
			double score = Core.getSimil(i, text);
			if(i == 1){
				valjoy = score;
			}
			if(i == 2){
				valsad = score;
			}
			if(i == 3){
				valfear = score;
			}
			if(i == 4){
				valdisgust = score;
			}
			if(i == 5){
				valanger = score;
			}
			if(i == 6){
				vasurprise = score;
			}
			sum = sum + score;
		}
		
		 String res ="{";
		 DecimalFormat df = new DecimalFormat("0.###");
		 res = res+"\"joy\":"+df.format(valjoy/sum).replaceAll(",", ".")+",";
		 res = res+"\"sad\":"+df.format(valsad/sum).replaceAll(",", ".")+",";
		 res = res+"\"fear\":"+df.format(valfear/sum).replaceAll(",", ".")+",";
		 res = res+"\"disgust\":"+df.format(valdisgust/sum).replaceAll(",", ".")+",";
		 res = res+"\"anger\":"+df.format(valanger/sum).replaceAll(",", ".")+",";
		 res = res+"\"surprise\":"+df.format(vasurprise/sum).replaceAll(",", ".")+"";
		 res = res+"}";
		
		return Response.status(200).entity(res).build();
	}


}
