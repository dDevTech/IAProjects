package utils;

public class ConsoleUtils {
	public static void progressPercentage(int remain, int total) {
	    if (remain > total) {
	        throw new IllegalArgumentException();
	    }
	    int maxBareSize = 50; // 10unit for 100%
	    int remainProcent =(int)( ((remain) /(double) total) * maxBareSize);
	    char defaultChar = ' ';
	    String icon = "=";
	    String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
	    StringBuilder bareDone = new StringBuilder();
	    bareDone.append("[");
	    for (int i = 0; i < remainProcent; i++) {
	        bareDone.append(icon);
	    }
	    String bareRemain = bare.substring(remainProcent, bare.length());
	    System.out.print("\r" + bareDone + bareRemain + " " +(int)( (remainProcent / (double) maxBareSize)*100)+"%");
	    if (remain == total) {
	        System.out.print("\n");
	    }
	}
}
