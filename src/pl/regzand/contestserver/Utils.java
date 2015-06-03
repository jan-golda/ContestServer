package pl.regzand.contestserver;

import java.util.Random;

public class Utils {

	/**
	 * Generates random integer from range of < min , max >
	 * @param min - minimal integer value
	 * @param max - maximal integer value
	 * @return integer form range of < min , max >
	 */
	public static int randomInt(int min, int max){
		return (new Random()).nextInt(max+1-min)+min;
	}
	
}
