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

	public static void CleanThisLicense(HashMap<Statement, Object> StatementsWithLicense, Statement CurrentStatement,
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
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(34);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
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
						TotalNumberOfHetrogeneousLicenseinModel++;
						System.out.println("lol1: " + license.toString());
						System.out.println("lol2: " + StatementsWithLicense.get(key).toString());
						isNewLicense = false;
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(33);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
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
						TotalNumberOfHetrogeneousLicenseinModel++;
						isNewLicense = false;
						break;
					}
					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(30);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
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
						TotalNumberOfHetrogeneousLicenseinModel++;
						isNewLicense = false;
						break;
					}

					if (license.toString().contains("europeandataportal")) {
						String licenseToChange = "file:///" + license.toString().toString().substring(29);
						if (licenseToChange.equals(StatementsWithLicense.get(key).toString())) {
							// System.out.println("Change -> " + licenseToChange);
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

	public static boolean AreLicensesHomogeneous(Model model) {

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
					CleanThisLicense(StatementsWithLicense, DataSetSentence,
							DataSet.getProperty(DCTerms.license).getObject());
				}
				if (DataSet.hasProperty(DCTerms.rights)
						&& !(DataSet.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {
					CleanThisLicense(StatementsWithLicense, DataSetSentence,
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
					CleanThisLicense(StatementsWithLicense, DistributionSentence,
							Distribution.getProperty(DCTerms.license).getObject());

				}
				if (Distribution.hasProperty(DCTerms.rights)
						&& !(Distribution.getProperty(DCTerms.rights).getObject().toString().isEmpty())) {
					CleanThisLicense(StatementsWithLicense, DistributionSentence,
							Distribution.getProperty(DCTerms.rights).getObject());

				}
			}
		}

		if(TotalNumberOfHetrogeneousLicenseinModel > 0)
			return false;
		else 
			return true;
	}
}
