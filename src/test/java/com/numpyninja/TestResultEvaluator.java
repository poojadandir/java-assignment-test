package com.numpyninja;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TestResultEvaluator {

	public static class TestResult {
		String fileName;
		int totalTests;
		int passedTests;
		int failedTests;
		int errorTests;
		double score;

		public TestResult(String fileName, int totalTests, int passedTests, int failedTests, int errorTests,
				double score) {
			this.fileName = fileName;
			this.totalTests = totalTests;
			this.passedTests = passedTests;
			this.failedTests = failedTests;
			this.errorTests = errorTests;
			this.score = score;
		}

		@Override
		public String toString() {
			return "TestResult{" + "fileName='" + fileName + '\'' + ", totalTests=" + totalTests + ", passedTests="
					+ passedTests + ", failedTests=" + failedTests + ", errorTests=" + errorTests + ", score=" + score
					+ '}';
		}
	}

	public static List<TestResult> parseTestResults(String directoryPath) {
		List<TestResult> results = new ArrayList<>();
		try {
			File dir = new File(directoryPath);
			File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));

			if (files != null) {
				for (File file : files) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(file);

					NodeList testsuites = doc.getElementsByTagName("testsuite");

					for (int i = 0; i < testsuites.getLength(); i++) {
						int totalTests = Integer
								.parseInt(testsuites.item(i).getAttributes().getNamedItem("tests").getNodeValue());
						int failedTests = Integer
								.parseInt(testsuites.item(i).getAttributes().getNamedItem("failures").getNodeValue());
						int errorTests = Integer
								.parseInt(testsuites.item(i).getAttributes().getNamedItem("errors").getNodeValue());

						int passedTests = totalTests - (failedTests + errorTests);
						double score = ((double) passedTests / totalTests) * 100;

						TestResult result = new TestResult(file.getName(), totalTests, passedTests, failedTests,
								errorTests, score);
						results.add(result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;
	}

	public static void main(String[] args) {
		String reportsDir = "target/surefire-reports/"; // The directory where test XML files are stored
		List<TestResult> results = parseTestResults(reportsDir);

		System.out.println("Test Results:");
		for (TestResult result : results) {
			System.out.println(result);
		}

		// Filter candidates who scored more than 80%
		System.out.println("\nCandidates scoring more than 80%:");
		for (TestResult result : results) {
			if (result.score > 80.0) {
				System.out.println(result.fileName + " - Score: " + result.score + "%");
			}
		}
	}
}
