package SearchEngine;

import org.apache.hadoop.util.ToolRunner;
// import SearchEngine.WordCount;

public class Main {
    public static final String help = "Wrong arguments";
    public static void main(String[] args) throws Exception {
        if(args[0].equals("Main")) {
            if (args.length < 1){
                System.out.println(help);
                System.exit(-1);
            }
            ToolRunner.run(new WordCount(), args);
            System.out.println("Successfully Exiting");
        }
        else {
            System.out.println(help);
            System.exit(-1);
        }
    }
}
