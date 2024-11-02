package com.gogreen;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class CatalogPlacementsAssignment {

	public static void main(String[] args) throws IOException {
		executeTestCase("input1.json");
		executeTestCase("input2.json");
	}

	private static void executeTestCase(String testfile) throws IOException {
		// Read and parse JSON input from a file
		String content = new String(Files.readAllBytes(Paths.get(testfile)));
		JSONObject jsonInput = new JSONObject(content);

		int n = jsonInput.getJSONObject("keys").getInt("n");
		int k = jsonInput.getJSONObject("keys").getInt("k");

		Map<Integer, BigInteger> points = new HashMap<>();

		for (String key : jsonInput.keySet()) {
			if (!key.equals("keys")) {
				JSONObject point = jsonInput.getJSONObject(key);
				int x = Integer.parseInt(key);
				int base = point.getInt("base");
				BigInteger y = new BigInteger(point.getString("value"), base);
				points.put(x, y);
			}
		}

		BigInteger constantTerm = calculateConstantTerm(points, k);
		System.out.println("Constant term (c) of the polynomial: " + constantTerm);

	}

	private static BigInteger calculateConstantTerm(Map<Integer, BigInteger> points, int k) {
		BigInteger result = BigInteger.ZERO;

		for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
			int xj = entry.getKey();
			BigInteger yj = entry.getValue();

			BigInteger lj = BigInteger.ONE;
			for (Map.Entry<Integer, BigInteger> other : points.entrySet()) {
				int xm = other.getKey();
				if (xm != xj) {
					lj = lj.multiply(BigInteger.valueOf(-xm)).divide(BigInteger.valueOf(xj - xm));
				}
			}

			result = result.add(yj.multiply(lj));
		}

		return result;
	}

}
