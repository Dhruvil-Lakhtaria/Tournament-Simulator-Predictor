/**
 * To use colors when printing output, concatenate the color BEFORE the text
 * to be coloured.
 * 
 * Example-
 *      System.out.println(Color.ANSI_RED + "Hello World" + "\nHow are you ?");
 *
 * The entire output will be in red.
 *      
 * All the print statements after the above example print statement will be in
 * red color unless ANSI_RESET is concatenated to the output to return it back to
 * its default color.
 * 
 * Examples-
 *      (i) System.out.println(Color.ANSI_RED + "I'm red!" + "\nI'm red as well!");
 *          System.out.println("\nI'm red too!");
 *          System.out.println("\nRed in color here.");
 *          System.out.println("\n" + Color.ANSI_RESET + "I'm in the default color");
 *
 *      (ii) System.out.println(Color.ANSI_BLUE + "I'm blue!" + Color.ANSI_RESET + "\nI'm Default");
 *
 * Note:
 * Be consistent in the colors used.
 *
 */
 package simulator.ui;
  
public class Color {
    public static final String ANSI_UNDERLINE = "\u001B[4m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001b[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
}
