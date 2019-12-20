package org.dice_research.opal.catfish;


import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.catfish.Catfish;
import org.dice_research.opal.catfish.ModelHomogeneousLicenseChecker;
import org.junit.Assert;
import org.junit.Test;

public class LicenseCleaningTest {
	
	
	/**
	 * Tests if Licenses are heterogeneous.
	 */
	@Test
	public void test1() throws Exception {
		
		String datasetUri = "http://projekt-opal.de/dataset/http___europeandataportal_eu_set_data__3dff988d_59d2_415d_b2da_818e8ef3111701";
		Catfish catfish = new Catfish();
		
		//Current model has heterogeneous licenses
		Model old_model = ModelFactory.createDefaultModel();
		old_model.read(getClass().getClassLoader().getResource("LicenseCleaningTestCases/2HeterogeneousLicenses_indicate_same_license.ttl").getFile(), "TURTLE");
        
		ModelHomogeneousLicenseChecker check_licenses = new ModelHomogeneousLicenseChecker(); 
		
		Assert.assertTrue("A Model with heterogeneous licenses", check_licenses.AreLicensesHomogeneous(catfish.process(old_model, datasetUri), datasetUri));		
	}

	
}
