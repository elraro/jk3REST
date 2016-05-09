package eu.elraro.jk3rest.query;

public class Utilities {
	/**
	 * ^0black ^1red ^2green ^3yellow ^4blue ^5cyan ^6purple ^7white ^8orange
	 * ^9gray
	 *
	 * @param coloredString
	 * @return uncolored string
	 */
	public static String removeColorCode(String coloredString) {
		return coloredString.replaceAll("(\\^\\d)|(\\$\\d)", "");
	}
}
