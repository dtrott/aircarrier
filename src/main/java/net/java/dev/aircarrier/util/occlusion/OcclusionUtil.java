package net.java.dev.aircarrier.util.occlusion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Quick util to work out the "unique" occlusions of a cube face
 * @author shingoki
 *
 */
public class OcclusionUtil {

	public final static void main(String[] args) {

		SortedSet<Occlusion> uniqueOcclusions = new TreeSet<Occlusion>();
		Transform[] allTransforms = Transform.allTransforms();
		
		for (int i = 0; i < 256; i++) {
			Occlusion o = new Occlusion(i);
			
			//Check that the set does not already include any of the
			//transformed versions of this occlusion
			boolean alreadyDone = false;
			for (Transform t : allTransforms) {
				Occlusion tO = o.transform(t);
				if (uniqueOcclusions.contains(tO)) alreadyDone = true;
			}
			
			//If the occlusion is not already covered (in some transformed
			//state at least) then add it
			if (!alreadyDone) uniqueOcclusions.add(o);
		}
		
		for (Occlusion o : uniqueOcclusions) {
			System.out.println(o);
			System.out.println();
		}
		
		//See how many unique occlusions we have
		System.out.println(uniqueOcclusions.size() + " unique occlusions");

		//Write out the occlusions to a file
		try {
			Writer writer = new FileWriter(new File(System.getProperty("user.home"), "occlusions.txt"));
			
			for (Occlusion o : uniqueOcclusions) {
				writer.write(o.toLine() + "\n");
			}			
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}

