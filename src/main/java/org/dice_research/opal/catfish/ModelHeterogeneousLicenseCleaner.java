package org.dice_research.opal.catfish;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ModelHeterogeneousLicenseCleaner {

	public static Object CreativeCommonsDcatOpenDefinition_LicenseCleaner(Object old_license) {

		Object new_license = old_license;
		Model CurrentModel = (Model) (((RDFNode) old_license).getModel());

		/*
		 * As per creative commons, deed keyword means “Human Readable”. If a creative
		 * commons license contains the keyword “legalcode” then that reference is for
		 * lawyers only. For regular people who are not lawyer they should refer the
		 * license without the keyword “legalcode” i.e instead of
		 * “https://creativecommons.org/licenses/by/4.0/legalcode” regular users should
		 * refer “https://creativecommons.org/licenses/by/4.0/”
		 * 
		 * Which is also same as all other licenses as follows: 1.
		 * http://creativecommons.org/licenses/by/4.0/deed.no 2.
		 * https://creativecommons.org/licenses/by/4.0/deed.de 3.
		 * https://creativecommons.org/licenses/by/4.0/deed.es_ES where .de, .no and
		 * .es_Es are language code for German, Norway and Spain respectively, but they
		 * refer to the same international license
		 * “https://creativecommons.org/licenses/by/4.0/”.
		 */
		if (old_license.toString().contains("legalcode")) {
			new_license = CurrentModel.createResource(old_license.toString().replace("legalcode", ""));
		}

		if (old_license.toString().contains("deed"))
			new_license = CurrentModel.createResource(old_license.toString().replaceAll("(deed\\.\\D+)", ""));

		/*
		 * Open Definition group http://www.opendefinition.org/licenses/cc-zero points
		 * to https://creativecommons.org/publicdomain/zero/1.0
		 */
		if (old_license.toString().contains("opendefinition.org/licenses/cc-zero"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("opendefinition.org/licenses/cc-zero", "creativecommons.org/publicdomain/zero/1.0"));

		// dcat-ap.de/def/licenses/dl-by-de/2.0 ---> govdata.de/dl-de/by-2-0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/dl-by-de/2.0"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-by-de/2.0", "govdata.de/dl-de/by-2-0"));

		// dcat-ap.de/def/licenses/dl-zero-de/2.0 ---> govdata.de/dl-de/zero-2-0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/dl-zero-de/2.0"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-zero-de/2.0", "govdata.de/dl-de/zero-2-0"));

		// dcat-ap.de/def/licenses/cc-zero --->
		// creativecommons.org/publicdomain/zero/1.0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/cc-zero"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/cc-zero", "creativecommons.org/publicdomain/zero/1.0"));

		// dcat-ap.de/def/licenses/cc-by/4.0 ---> creativecommons.org/licenses/by/4.0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/cc-by/4.0"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/cc-by/4.0", "creativecommons.org/licenses/by/4.0"));

		// dcat-ap.de/def/licenses/dl-by-nc-de/1.0 ---> govdata.de/dl-de/by-nc-1-0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/dl-by-nc-de/1.0"))
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-by-nc-de/1.0", "govdata.de/dl-de/by-nc-1-0"));

		// dcat-ap.de/def/licenses/odbl ---> opendefinition.org/licenses/odc-odbl
		if (old_license.toString().contains("dcat-ap.de/def/licenses/odbl"))
			new_license = CurrentModel.createResource(old_license.toString().replaceAll("dcat-ap.de/def/licenses/odbl",
					"opendefinition.org/licenses/odc-odbl"));

		// dcat-ap.de/def/licenses/dl-by-de/1.0 ---> govdata.de/dl-de/by-1-0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/dl-by-de/1.0"))
			new_license = CurrentModel.createResource(
					old_license.toString().replace("dcat-ap.de/def/licenses/dl-by-de/1.0", "govdata.de/dl-de/by-1-0"));

		// dcat-ap.de/def/licenses/cc-by-nd/4.0 --->
		// creativecommons.org/licenses/by-nd/4.0
		if (old_license.toString().contains("dcat-ap.de/def/licenses/cc-by-nd/4.0"))
			new_license = old_license.toString().replaceAll("dcat-ap.de/def/licenses/cc-by-nd/4.0",
					"creativecommons.org/licenses/by-nd/4.0");

		return new_license;
	}

	public static void CleanThisLicense(HashMap<Statement, Object> StatementsWithLicense,
			HashMap<Statement, RDFNode> TheUpdater, Statement CurrentStatement, Object old_license) {

		// System.out.println("License Entered:" + license);

		// Its used to check if a new license or not
		boolean isNewLicense = true;

		/*
		 * Process the old_license URI to see if they are CC or Dcat or OD which express
		 * other existing licenses. If they express the same meaning as other existing
		 * license then make the old_license URI homogeneous.
		 */
		Object license = CreativeCommonsDcatOpenDefinition_LicenseCleaner(old_license);

		/*
		 * If the processed license is not same as oldLicense then it means that the
		 * old_license URI expresses a license already expressed by another prominent
		 * URI. So this old_license has been cleaned and we need to update the sentence
		 * with new license URI object.
		 */
		if (!old_license.equals(license))
			TheUpdater.put(CurrentStatement, (RDFNode) license);
		/*
		 * This RegX is used to see if there is a similar license with https:// or
		 * http:// or https://www. or http://www. or file:///
		 */
		String PatternRegX1 = "^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|file:\\/\\/\\/)";

		/*
		 * We have seen from OPAL data that most of the licenses are from
		 * europeanDataPortal and in many cases there is an equivalent
		 * file://content.show license file. We will use this RegX to check for this
		 * case. If we find a match then make the license homogeneous, change it to
		 * europeandataportal type license.
		 */
		String PatternRegX2 = "^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.)(europeandataportal\\.eu\\/)";

		if (!(StatementsWithLicense.containsValue(license))) {

			//System.out.println("Entered license : " + license.toString());

			for (Statement key : StatementsWithLicense.keySet()) {

				/*
				 * Here first check if the new license starts with https://www. and if a similar
				 * license exists then make the new license homogeneous i.e run an update for
				 * the respective statement.
				 */
				if (license.toString().contains("https://www.")) {

					Pattern pattern1 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(12)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher1 = pattern1.matcher(StatementsWithLicense.get(key).toString());
					if (matcher1.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						TheUpdater.put(CurrentStatement, (RDFNode) StatementsWithLicense.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						// License to change://file:///content/show-license ...
						String licenseToChange = "file:///" + license.toString().toString().substring(34);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							TheUpdater.put(key, (RDFNode) license);
							isNewLicense = false;
							break;
						}
					}
				}
				/*
				 * Here first check if the new license starts with http://www. and if a similar
				 * license exists then make the new license homogeneous i.e run an update for
				 * the respective statement.
				 */
				else if (license.toString().contains("http://www.")) {
					Pattern pattern2 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(11)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher2 = pattern2.matcher(StatementsWithLicense.get(key).toString());
					if (matcher2.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						TheUpdater.put(CurrentStatement, (RDFNode) StatementsWithLicense.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(33);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							TheUpdater.put(key, (RDFNode) license);
							isNewLicense = false;
							break;
						}
					}
				}
				/*
				 * Here first check if the new license starts with https://. and if a similar
				 * license exists then make the new license homogeneous i.e run an update for
				 * the respective statement.
				 */
				else if (license.toString().contains("https://")) {
					Pattern pattern3 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(8)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher3 = pattern3.matcher(StatementsWithLicense.get(key).toString());
					if (matcher3.find()) {
						// System.out.println("It matched " + license.toString());
						isNewLicense = false;
						TheUpdater.put(CurrentStatement, (RDFNode) StatementsWithLicense.get(key));
						break;
					}
					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(30);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							TheUpdater.put(key, (RDFNode) license);
							isNewLicense = false;
							break;
						}
					}
				} else if (license.toString().contains("http://")) {
					Pattern pattern4 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(7)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher4 = pattern4.matcher(StatementsWithLicense.get(key).toString());
					if (matcher4.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						TheUpdater.put(CurrentStatement, (RDFNode) StatementsWithLicense.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(29);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							TheUpdater.put(key, (RDFNode) license);
							isNewLicense = false;
							break;
						}
					}
					/*
					 * Here we will do two checks. First we check if a similar file is there or not.
					 * Secondly we check if an equivalent europeandataportal license is present, if
					 * yes then make it homogeneous.
					 */
				} else if (license.toString().contains("file:///")) {
					Pattern pattern5 = Pattern.compile(
							PatternRegX1 + Pattern.quote(license.toString().toString().substring(8)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher5 = pattern5.matcher(StatementsWithLicense.get(key).toString());
					Pattern pattern6 = Pattern.compile(
							PatternRegX2 + Pattern.quote(license.toString().toString().substring(8)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher6 = pattern6.matcher(StatementsWithLicense.get(key).toString());
					if (matcher5.find()) {
						// System.out.println("File matched "+license.toString());
						isNewLicense = false;
					}
					if (matcher6.find()) {
						// System.out.println("Change -> " + license.toString());
						isNewLicense = false;
						TheUpdater.put(CurrentStatement, (RDFNode) StatementsWithLicense.get(key));
						break;
					}
				}

			}

			/*
			 * If the loop does not BREAK in the middle that means this license is a unique
			 * URI. So add it to the unique license container. This container will be used
			 * to check for homogeneousness.
			 */
			if (isNewLicense)
				StatementsWithLicense.put(CurrentStatement, license);

		}
	}

	public static Model ModelLicenCleaner(Model model, String DatasetUri) {

		Resource dataset = model.createResource(DatasetUri);

		// Collect statements with license/rights
		HashMap<Statement, Object> StatementsWithLicense = new HashMap<Statement, Object>();

		// This map will be used to update the statements for license cleaning.
		HashMap<Statement, RDFNode> TheStatementUpdater = new HashMap<Statement, RDFNode>();

		/*
		 * First Check License and Rights info in all datasets
		 */

		NodeIterator LicenseIterator = model.listObjectsOfProperty(dataset, DCTerms.license);
		NodeIterator RightsIterator = model.listObjectsOfProperty(dataset, DCTerms.rights);
		if (LicenseIterator.hasNext()) {
			while (LicenseIterator.hasNext()) {
				RDFNode LicenseNode = LicenseIterator.nextNode();
				// System.out.println(DataSet.toString());
				if (!(LicenseNode.toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(LicenseNode.toString().replace("http:", "https:"));
					Statement CurrentStatement = dataset.getProperty(DCTerms.license);
					dataset.getProperty(DCTerms.license).changeObject(new_license);

					CleanThisLicense(StatementsWithLicense, TheStatementUpdater, CurrentStatement, LicenseNode);

				}
			}
		}
		if (RightsIterator.hasNext()) {
			while (RightsIterator.hasNext()) {
				RDFNode RightsNode = RightsIterator.nextNode();
				// System.out.println(DataSet.toString());
				if (!(RightsNode.toString().isEmpty())) {

					// Change http to https
					Resource new_Rights = model.createResource(RightsNode.toString().replace("http:", "https:"));
					Statement CurrentStatement = dataset.getProperty(DCTerms.rights);
					dataset.getProperty(DCTerms.rights).changeObject(new_Rights);

					CleanThisLicense(StatementsWithLicense, TheStatementUpdater, CurrentStatement, RightsNode);

				}
			}
		}

		/*
		 * Now check total number of licenses and rights in distributions and collect
		 * them
		 */
		NodeIterator DistributionsIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		if (DistributionsIterator.hasNext()) {
			while (DistributionsIterator.hasNext()) {
				RDFNode DistributionNode = DistributionsIterator.nextNode();
				Resource Distribution = (Resource) DistributionNode;

				// System.out.println(DataSet.toString());
				if (Distribution.hasProperty(DCTerms.license)
						&& !(Distribution.getProperty(DCTerms.license).getObject().toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(Distribution.getProperty(DCTerms.license).getObject()
							.toString().replace("http:", "https:"));
					Distribution.getProperty(DCTerms.license).changeObject(new_license);

					CleanThisLicense(StatementsWithLicense, TheStatementUpdater,
							Distribution.getProperty(DCTerms.license),
							Distribution.getProperty(DCTerms.license).getObject());

				}
				if (Distribution.hasProperty(DCTerms.rights)
						&& !(Distribution.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(
							Distribution.getProperty(DCTerms.rights).getObject().toString().replace("http:", "https:"));
					Distribution.getProperty(DCTerms.rights).changeObject(new_license);

					CleanThisLicense(StatementsWithLicense, TheStatementUpdater,
							Distribution.getProperty(DCTerms.rights),
							Distribution.getProperty(DCTerms.rights).getObject());

				}
			}
		}

		// Let the Model Updater ROLL !!!
		if (TheStatementUpdater.size() > 0) {
			for (Statement key : TheStatementUpdater.keySet()) {
				StatementsWithLicense.remove(key);
				StatementsWithLicense.put(key, TheStatementUpdater.get(key));
				if (key.getSubject().hasProperty(DCTerms.license)) {

					key.getSubject().getProperty(DCTerms.license).changeObject(TheStatementUpdater.get(key));
				} else

					key.getSubject().getProperty(DCTerms.rights).changeObject(TheStatementUpdater.get(key));
			}
		}

		return model;
	}
}
