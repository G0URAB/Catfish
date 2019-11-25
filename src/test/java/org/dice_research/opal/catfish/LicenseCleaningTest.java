package org.dice_research.opal.catfish;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.dice_research.opal.catfish.Catfish;
// import org.junit.Assert;
import org.junit.Test;

public class LicenseCleaningTest {
	
	
	/**
	 * Tests if Licenses are heterogeneous.
	 */
	@Test
	public void test2() throws Exception {
		
		String datasetUri = "https://example.org/dataset1";
		Catfish catfish1 = new Catfish();
		
		//Current model has heterogeneous licenses
		Model old_model = ModelFactory.createDefaultModel();
		old_model.read(getClass().getClassLoader().getResource("TestCaseForCleaningLicense2DatasetsEachwith5distributions.ttl").getFile(), "TURTLE");
        
		assertFalse("A Model with heterogeneous licenses", catfish1.AreLicensesHomogeneous(old_model, datasetUri));		
	}

	
	/**
	 * Clean Licenses and check model's license homogeneousness again.
	 */
	
	@Test
	public void test1() throws Exception {
		
		String datasetUri = "https://example.org/dataset2";
		Catfish catfish2 = new Catfish();
		
		//Current model has heterogeneous licenses
		Model model = ModelFactory.createDefaultModel();
		model.read(getClass().getClassLoader().getResource("TestCaseForCleaningLicense2DatasetsEachwith5distributions.ttl").getFile(), "TURTLE");
		
		//This model will be cleaned by Catfish License cleaner component
		Model new_model = ModelFactory.createDefaultModel();
        new_model = catfish2.CleanLicenses(model, datasetUri);
		
		assertTrue("New Cleaned Model with homogeneous licenses", catfish2.AreLicensesHomogeneous(new_model, datasetUri));	
		
	}


	
}
