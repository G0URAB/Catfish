package org.dice_research.opal.catfish;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.regex.Matcher;

public class ModelHomogeneousLicenseChecker {

	static int TotalNumberOfHetrogeneousLicenseinModel = 0;

	/*
	 * Checks if heterogeneous URI links to same license exits or not. If yes, then
	 * increment TotalNumberOfHetrogeneousLicenseinModel
	 */
	public static void CreativeCommonsDcatOpenDefinition_LicenseChecker(
			HashMap<Statement, Object> StatementsWithLicense) {

		for (Statement CurrentStatement : StatementsWithLicense.keySet()) {

			//System.out.println("Unique License to check :" + StatementsWithLicense.get(CurrentStatement));

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches(
							"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/licenses\\/by\\/)(\\d+(\\.\\d+)?\\/)(legalcode)")) {
						TotalNumberOfHetrogeneousLicenseinModel++;
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches(
							"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/)(.+)(\\/\\d+(\\.\\d+)?\\/)(deed\\.\\D+)")) {
						TotalNumberOfHetrogeneousLicenseinModel++;
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(opendefinition\\.org\\/licenses\\/cc-zero)")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString()
							.matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/publicdomain\\/zero\\/1\\.0)"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-by-de\\/2\\.0)")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString().matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(govdata\\.de\\/dl-de\\/by-2-0)"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-zero-de\\/2\\.0)")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString().contains("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(govdata\\.de\\/dl-de\\/zero-2-0)"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString().contains("dcat-ap.de/def/licenses/cc-zero")) {

				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString()
							.contains("creativecommons.org/publicdomain/zero/1.0"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString().contains("dcat-ap.de/def/licenses/cc-by/4.0")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString()
							.contains("creativecommons.org/licenses/by/4.0"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.contains("dcat-ap.de/def/licenses/dl-by-nc-de/1.0")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString().contains("govdata.de/dl-de/by-nc-1-0"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString().contains("dcat-ap.de/def/licenses/odbl")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString()
							.contains("opendefinition.org/licenses/odc-odbl"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString()
					.matches(
							"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/dl-by-de\\/1\\.0)")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString().contains("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(govdata\\.de\\/dl-de\\/by-1-0)"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}

			if (StatementsWithLicense.get(CurrentStatement).toString().
					matches(
							"^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(dcat-ap\\.de\\/def\\/licenses\\/cc-by-nd\\/4\\.0)")) {
				for (Statement CurrentStatement1 : StatementsWithLicense.keySet()) {
					if (StatementsWithLicense.get(CurrentStatement1).toString()
							.matches("^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.|www\\.)(creativecommons\\.org\\/licenses\\/by-nd\\/4\\.0)"))
						TotalNumberOfHetrogeneousLicenseinModel++;
				}
			}
		}
	}

	public static void CheckThisLicense(HashMap<Statement, Object> StatementsWithLicense, Statement CurrentStatement,
			Object license) {

		// Its used to check if a new license or not
		boolean isNewLicense = true;

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
		String PatternRegX2 = "^(https:\\/\\/|http:\\/\\/|http:\\/\\/www\\.|https:\\/\\/www\\.)(europeandataportal.eu\\/)";

		if (!(StatementsWithLicense.containsValue(license))) {

			System.out.println("Entered license : " + license.toString());

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
						TotalNumberOfHetrogeneousLicenseinModel++;

					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(34);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							isNewLicense = false;
							TotalNumberOfHetrogeneousLicenseinModel++;

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
						TotalNumberOfHetrogeneousLicenseinModel++;
						isNewLicense = false;

					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(33);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							isNewLicense = false;
							TotalNumberOfHetrogeneousLicenseinModel++;

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
						TotalNumberOfHetrogeneousLicenseinModel++;
						isNewLicense = false;

					}
					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(30);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							isNewLicense = false;
							TotalNumberOfHetrogeneousLicenseinModel++;

						}
					}
				} else if (license.toString().contains("http://")) {
					Pattern pattern4 = Pattern.compile(PatternRegX1 + Pattern.quote(license.toString().substring(7)),
							Pattern.CASE_INSENSITIVE);
					Matcher matcher4 = pattern4.matcher(StatementsWithLicense.get(key).toString());
					if (matcher4.find()) {
						// System.out.println("It matched" + license.toString());
						TotalNumberOfHetrogeneousLicenseinModel++;
						isNewLicense = false;

					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(29);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
							isNewLicense = false;
							TotalNumberOfHetrogeneousLicenseinModel++;

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
						break;
					}
				}

			}

			/*
			 * If the loop does not BREAK in the middle that means this license is a unique
			 * URI. So add it to the license container.
			 */
			if (isNewLicense)
				StatementsWithLicense.put(CurrentStatement, license);

		}
	}

	public static boolean AreLicensesHomogeneous(Model model, String DatasetUri) {

		// Collect statements with license/rights
		HashMap<Statement, Object> StatementsWithLicense = new HashMap<Statement, Object>();

		// This map will be used to update the statements for license cleaning.
		HashMap<Statement, RDFNode> TheStatementUpdater = new HashMap<Statement, RDFNode>();

		/*
		 * First Check License and Rights info in all datasets
		 */

		StmtIterator DatasetIterator = model.listStatements(new SimpleSelector(null, RDF.type, DCAT.Dataset));
		if (DatasetIterator.hasNext()) {
			while (DatasetIterator.hasNext()) {
				Statement DataSetSentence = DatasetIterator.nextStatement();
				Resource DataSet = DataSetSentence.getSubject();
				// System.out.println(DataSet.toString());
				if (DataSet.hasProperty(DCTerms.license)
						&& !(DataSet.getProperty(DCTerms.license).getObject().toString().isEmpty())) {
					CheckThisLicense(StatementsWithLicense, DataSetSentence,
							DataSet.getProperty(DCTerms.license).getObject());
				}
				if (DataSet.hasProperty(DCTerms.rights)
						&& !(DataSet.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {
					CheckThisLicense(StatementsWithLicense, DataSetSentence,
							DataSet.getProperty(DCTerms.rights).getObject());
				}
			}
		}

		/*
		 * Now check total number of licenses and rights in distributions and collect
		 * them
		 */
		StmtIterator DistributionsIterator = model
				.listStatements(new SimpleSelector(null, RDF.type, DCAT.Distribution));
		if (DistributionsIterator.hasNext()) {
			while (DistributionsIterator.hasNext()) {
				Statement DistributionSentence = DistributionsIterator.nextStatement();
				Resource Distribution = DistributionSentence.getSubject();
				// System.out.println(DataSet.toString());
				if (Distribution.hasProperty(DCTerms.license)
						&& !(Distribution.getProperty(DCTerms.license).getObject().toString().isEmpty())) {
					CheckThisLicense(StatementsWithLicense, DistributionSentence,
							Distribution.getProperty(DCTerms.license).getObject());

				}
				if (Distribution.hasProperty(DCTerms.rights)
						&& !(Distribution.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {
					CheckThisLicense(StatementsWithLicense, DistributionSentence,
							Distribution.getProperty(DCTerms.rights).getObject());

				}
			}
		}

		/*
		 * check if the model contains heterogeneous URI links that point to the same
		 * license.
		 */
		CreativeCommonsDcatOpenDefinition_LicenseChecker(StatementsWithLicense);

		if (TotalNumberOfHetrogeneousLicenseinModel > 0)
			return false;
		else
			return true;
	}
}
