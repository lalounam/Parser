package com.ef;

import com.ef.db.Persistence;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParserTest extends TestCase {

	public ParserTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ParserTest.class);
	}

	/**
	 * Verifying data base
	 */
	public void testApp() {
		Persistence p = new Persistence();
		assertNotNull(p.executeSelect("SELECT * FROM requests;"));
	}

}
