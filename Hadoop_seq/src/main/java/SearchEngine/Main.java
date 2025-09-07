package SearchEngine;

import org.apache.hadoop.util.ToolRunner;
// import SearchEngine.WordCount;

public class Main {
    public static final String help = "Wrong arguments";
    public static void main(String[] args) throws Exception {
        if(args[0].equals("Seq")) {
            ToolRunner.run(new VectorWritableToSequenceFile(), args);
            System.out.println("Successfully Exiting");
        }
        else {
            System.out.println(help);
            System.exit(-1);
        }
    }
}
