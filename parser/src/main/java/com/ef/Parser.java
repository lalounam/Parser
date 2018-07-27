package com.ef;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ef.db.Persistence;

public class Parser {

	private static Date startDate = null;
	private static String duration = null;
	private static Integer threshold = null;

	private static File accesslog = null;

	private static int paramsIncluded = 0;

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");

	public static void main(String[] args) {

		if (args.length < 3) {
			usage();
		} else {
			for (String param : args) {
				if (param.startsWith("--startDate=")) {
					try {
						startDate = df.parse(param.split("--startDate=")[1]);
						paramsIncluded++;
					} catch (ParseException pe) {
						pe.printStackTrace();
						return;
					}
				} else if (param.startsWith("--duration=")) {
					try {
						duration = param.split("--duration=")[1];
						if (!duration.equals("hourly") && !duration.equals("daily")) {
							System.out.println(param + " no valid value");
							return;
						}
						paramsIncluded++;
					} catch (Exception e) {
						System.out.println("--duration is malformed");
						return;
					}
				} else if (param.startsWith("--threshold=")) {
					try {
						threshold = Integer.parseInt(param.split("--threshold=")[1]);
						if (threshold <= 0) {
							System.out.println(param + " no valid value");
							return;
						}
						paramsIncluded++;
					} catch (Exception e) {
						System.out.println("--threshold is malformed");
						return;
					}
				} else if (param.startsWith("--accesslog=")) {
					try {
						accesslog = new File(param.split("--accesslog=")[1]);
						new Persistence().insertFile(accesslog);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (paramsIncluded < 3) {
				usage();
			} else {
				List<String> ips = new Persistence().searchViolations(df.format(startDate), threshold, duration);
				for (String ip : ips) {
					System.out.println(ip);
				}
			}
		}
	}

	private static void usage() {
		System.out.println("\n\nUsage:");
		System.out.println("(minimum parameters needed)");
		System.out.println("--startDate=yyyy-MM-dd.HH:mm:ss");
		System.out.println("--duration=[hourly/daily]");
		System.out.println("--threshold=[int]");
		System.out.println("(optional parameters)");
		System.out.println("--accesslog=path/log/file\n\n");
	}

}
