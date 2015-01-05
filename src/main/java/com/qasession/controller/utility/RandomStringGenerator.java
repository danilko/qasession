package com.qasession.controller.utility;

import java.security.SecureRandom;

public class RandomStringGenerator {
	private static final String TEMPLATE = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static final SecureRandom RANDOM = new SecureRandom();

	public static final int ID_LENTH=60;
	
	public static String generator(int pLength) {
		StringBuilder lSB = new StringBuilder(pLength);

		for (int index = 0; index < pLength; index++) {
			lSB.append(TEMPLATE.charAt(RANDOM.nextInt(TEMPLATE.length())));
		} // for

		return lSB.toString();

	} // String generator
} // class RandomStringGeneraotor
