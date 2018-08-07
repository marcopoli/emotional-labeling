package emotionalLabeling;

import java.util.Vector;



public class SimilaritaCoseno 
{
   
    
    public static float calculateTanimoto(Vector<Double> features1, Vector<Double> features2) {

    	
        int n = features1.size();
        double ab = 0.0;
        double a2 = 0.0;
        double b2 = 0.0;

        for (int i = 0; i < n; i++) {
            ab += features1.get(i) * features2.get(i);
            a2 += features1.get(i)*features1.get(i);
            b2 += features2.get(i)*features2.get(i);
        }
        return (float)ab/(float)(a2+b2-ab);
    }

}
