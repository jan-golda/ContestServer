package pl.regzand.contestserver;

import java.util.Random;

public class Utils {
	
	public static int randomInt(int min, int max){
		return (new Random()).nextInt(max+1-min)+min;
	}
	
}
