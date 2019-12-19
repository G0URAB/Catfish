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
		 * https://creativecommons.org/licenses/by/4.0/deed.es_ES where .de, .no
		 * and.es_Es are language code for German, Norway and Spain respectively, but
		 * they refer to the same international license
		 * “https://creativecommons.org/licenses/by/4.0/”.
		 */
		/*
		 * Create prefixes for safety so that in future homogeneous licenses will not be
		 * affected. For example think about these two probable licenses. 1.
		 * www.upb.legalcode.cs/3 2.
		 * https://creativecommons.org/licenses/by/4.0/legalcode
		 */

//--------prefix_based_approach: For replacing heterogeneous licenses---------//

		// Prefix for "creativecommons.org/licenses/by/anydigit.0/legalcode"
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/licenses\\/by\\/)(\\d+(\\.\\d+)?\\/)(legalcode)")) {
			new_license = CurrentModel.createResource(old_license.toString().replace("legalcode", ""));
		}

		// Prefix for "creativecommons.org/licenses/by/anydigit.0/deed.anylanguage"
		// Prefix for
		// "creativecommons.org/publicdomain/zero/anydigit.0/deed.anylanguage"
		// Prefix for "creativecommons.org/publicdomain/mark/anydigit.0/deed.anylanguage
		// and so on
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/)(.+)(\\/\\d+(\\.\\d+)?\\/)(deed\\.\\D+)")) {
			new_license = CurrentModel.createResource(old_license.toString().replaceAll("(deed\\.\\D+)", ""));
		}

		// Prefix for "opendefinition.org/licenses/cc-zero"
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(opendefinition\\.org\\/licenses\\/cc-zero)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("opendefinition.org/licenses/cc-zero", "creativecommons.org/publicdomain/zero/1.0"));
		}

		// dcat-ap.de/def/licenses/dl-by-de/2.0 ---> govdata.de/dl-de/by-2-0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-by-de\\/2\\.0)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-by-de/2.0", "govdata.de/dl-de/by-2-0"));
		}

		// dcat-ap.de/def/licenses/dl-zero-de/2.0 ---> govdata.de/dl-de/zero-2-0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-zero-de\\/2\\.0)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-zero-de/2.0", "govdata.de/dl-de/zero-2-0"));
		}

		// dcat-ap.de/def/licenses/cc-zero --->creativecommons.org/publicdomain/zero/1.0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/cc-zero)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/cc-zero", "creativecommons.org/publicdomain/zero/1.0"));
		}

		// dcat-ap.de/def/licenses/cc-by/4.0 ---> creativecommons.org/licenses/by/4.0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/cc-by\\/4\\.0)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/cc-by/4.0", "creativecommons.org/licenses/by/4.0"));
		}

		// dcat-ap.de/def/licenses/dl-by-nc-de/1.0 ---> govdata.de/dl-de/by-nc-1-0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-by-nc-de\\/1\\.0)")) {
			new_license = CurrentModel.createResource(old_license.toString()
					.replaceAll("dcat-ap.de/def/licenses/dl-by-nc-de/1.0", "govdata.de/dl-de/by-nc-1-0"));
		}

		// dcat-ap.de/def/licenses/odbl ---> opendefinition.org/licenses/odc-odbl
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/odbl)")) {
			new_license = CurrentModel.createResource(old_license.toString().replaceAll("dcat-ap.de/def/licenses/odbl",
					"opendefinition.org/licenses/odc-odbl"));
		}

		// dcat-ap.de/def/licenses/dl-by-de/1.0 ---> govdata.de/dl-de/by-1-0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-by-de\\/1\\.0)")) {
			new_license = CurrentModel.createResource(
					old_license.toString().replace("dcat-ap.de/def/licenses/dl-by-de/1.0", "govdata.de/dl-de/by-1-0"));
		}

		// dcat-ap.de/def/licenses/cc-by-nd/4.0 --->
		// creativecommons.org/licenses/by-nd/4.0
		if (old_license.toString().matches(
				"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/cc-by-nd\\/4\\.0)")) {
			new_license = old_license.toString().replaceAll("dcat-ap.de/def/licenses/cc-by-nd/4.0",
					"creativecommons.org/licenses/by-nd/4.0");
		}

		return new_license;
	}

	public static void CleanThisLicense(HashMap<Statement, Object> statements_with_license,
			HashMap<Statement, RDFNode> the_updater, Statement current_statement, Object old_license) {

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
			the_updater.put(current_statement, (RDFNode) license);
		/*
		 * This RegX is used to see if there is a similar license with https:// or
		 * http:// or https://www. or http://www. or file:///
		 */
		String PatternRegX1 = "^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.|file:\\/\\/\\/)";

		/*
		 * We have seen from OPAL data that most of the licenses are from
		 * europeanDataPortal and in many cases there is an equivalent
		 * file://content.show license file. We will use this RegX to check for this
		 * case. If we find a match then make the license homogeneous, change it to
		 * europeandataportal type license.
		 */
		String PatternRegX2 = "^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.)(europeandataportal\\.eu\\/)";

		if (!(statements_with_license.containsValue(license))) {

			System.out.println("Entered license : " + license.toString());

			for (Statement key : statements_with_license.keySet()) {

				/*
				 * Here first check if the new license starts with https://www. and if a similar
				 * license exists then make the new license homogeneous i.e run an update for
				 * the respective statement.
				 */
				if (license.toString().contains("https://www.")) {

					Pattern pattern1 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(12)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher1 = pattern1.matcher(statements_with_license.get(key).toString());
					if (matcher1.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						the_updater.put(current_statement, (RDFNode) statements_with_license.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						// License to change://file:///content/show-license ...
						String licenseToChange = "file:///" + license.toString().toString().substring(34);
						if (licenseToChange.equals(statements_with_license.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							the_updater.put(key, (RDFNode) license);
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
					Matcher matcher2 = pattern2.matcher(statements_with_license.get(key).toString());
					if (matcher2.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						the_updater.put(current_statement, (RDFNode) statements_with_license.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(33);
						if (licenseToChange.equals(statements_with_license.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							the_updater.put(key, (RDFNode) license);
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
					Matcher matcher3 = pattern3.matcher(statements_with_license.get(key).toString());
					if (matcher3.find()) {
						// System.out.println("It matched " + license.toString());
						isNewLicense = false;
						the_updater.put(current_statement, (RDFNode) statements_with_license.get(key));
						break;
					}
					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(30);
						if (licenseToChange.equals(statements_with_license.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							the_updater.put(key, (RDFNode) license);
							isNewLicense = false;
							break;
						}
					}
				} else if (license.toString().contains("http://")) {
					Pattern pattern4 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(7)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher4 = pattern4.matcher(statements_with_license.get(key).toString());
					if (matcher4.find()) {
						// System.out.println("It matched" + license.toString());
						isNewLicense = false;
						the_updater.put(current_statement, (RDFNode) statements_with_license.get(key));
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(29);
						if (licenseToChange.equals(statements_with_license.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							the_updater.put(key, (RDFNode) license);
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
					Matcher matcher5 = pattern5.matcher(statements_with_license.get(key).toString());
					Pattern pattern6 = Pattern.compile(
							PatternRegX2 + Pattern.quote(license.toString().toString().substring(8)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher6 = pattern6.matcher(statements_with_license.get(key).toString());
					if (matcher5.find()) {
						// System.out.println("File matched "+license.toString());
						isNewLicense = false;
					}
					if (matcher6.find()) {
						// System.out.println("Change -> " + license.toString());
						isNewLicense = false;
						the_updater.put(current_statement, (RDFNode) statements_with_license.get(key));
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
				statements_with_license.put(current_statement, license);

		}
	}

	public static Model ModelLicenCleaner(Model model, String DatasetUri) {

		Resource dataset = model.createResource(DatasetUri);

		// Collect statements with license/rights
		HashMap<Statement, Object> statements_with_license = new HashMap<Statement, Object>();

		// This map will be used to update the statements for license cleaning.
		HashMap<Statement, RDFNode> the_statement_updater = new HashMap<Statement, RDFNode>();

		/*
		 * First Check License and Rights info in all datasets
		 */

		NodeIterator license_iterator = model.listObjectsOfProperty(dataset, DCTerms.license);
		NodeIterator rights_iterator = model.listObjectsOfProperty(dataset, DCTerms.rights);
		if (license_iterator.hasNext()) {
			while (license_iterator.hasNext()) {
				RDFNode LicenseNode = license_iterator.nextNode();
				// System.out.println(DataSet.toString());
				if (!(LicenseNode.toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(LicenseNode.toString().replace("http:", "https:"));
					Statement current_statement = dataset.getProperty(DCTerms.license);
					dataset.getProperty(DCTerms.license).changeObject(new_license);

					CleanThisLicense(statements_with_license, the_statement_updater, current_statement, LicenseNode);

				}
			}
		}
		if (rights_iterator.hasNext()) {
			while (rights_iterator.hasNext()) {
				RDFNode rights_node = rights_iterator.nextNode();
				// System.out.println(DataSet.toString());
				if (!(rights_node.toString().isEmpty())) {

					// Change http to https
					Resource new_rights = model.createResource(rights_node.toString().replace("http:", "https:"));
					Statement current_statement = dataset.getProperty(DCTerms.rights);
					dataset.getProperty(DCTerms.rights).changeObject(new_rights);

					CleanThisLicense(statements_with_license, the_statement_updater, current_statement, rights_node);

				}
			}
		}

		/*
		 * Now check total number of licenses and rights in distributions and collect
		 * them
		 */
		NodeIterator distributionsIterator = model.listObjectsOfProperty(dataset, DCAT.distribution);
		if (distributionsIterator.hasNext()) {
			while (distributionsIterator.hasNext()) {
				RDFNode distributionNode = distributionsIterator.nextNode();
				Resource distribution = (Resource) distributionNode;

				// System.out.println(DataSet.toString());
				if (distribution.hasProperty(DCTerms.license)
						&& !(distribution.getProperty(DCTerms.license).getObject().toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(distribution.getProperty(DCTerms.license).getObject()
							.toString().replace("http:", "https:"));
					distribution.getProperty(DCTerms.license).changeObject(new_license);

					CleanThisLicense(statements_with_license, the_statement_updater,
							distribution.getProperty(DCTerms.license),
							distribution.getProperty(DCTerms.license).getObject());

				}
				if (distribution.hasProperty(DCTerms.rights)
						&& !(distribution.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {

					// Change http to https
					Resource new_license = model.createResource(
							distribution.getProperty(DCTerms.rights).getObject().toString().replace("http:", "https:"));
					distribution.getProperty(DCTerms.rights).changeObject(new_license);

					CleanThisLicense(statements_with_license, the_statement_updater,
							distribution.getProperty(DCTerms.rights),
							distribution.getProperty(DCTerms.rights).getObject());

				}
			}
		}

		// Let the Model Updater ROLL !!!
		if (the_statement_updater.size() > 0) {
			for (Statement key : the_statement_updater.keySet()) {
				statements_with_license.remove(key);
				statements_with_license.put(key, the_statement_updater.get(key));
				if (key.getSubject().hasProperty(DCTerms.license)) {

					key.getSubject().getProperty(DCTerms.license).changeObject(the_statement_updater.get(key));
				} else

					key.getSubject().getProperty(DCTerms.rights).changeObject(the_statement_updater.get(key));
			}
		}
		return model;
	}
}
