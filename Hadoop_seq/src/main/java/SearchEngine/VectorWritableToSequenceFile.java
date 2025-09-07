package SearchEngine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.mahout.math.VectorWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.hadoop.util.Tool;

import utils.Paths;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VectorWritableToSequenceFile extends Configured implements Tool{

    public static class DoubleArrayWritable extends ArrayWritable {
        public DoubleArrayWritable() {
            super(DoubleWritable.class);
        }
        
        public DoubleArrayWritable(DoubleWritable[] values) {
            super(DoubleWritable.class, values);
        }
    }

    public int run(String[] args) throws Exception {
        // Path to the input file and output SequenceFile
        String inputFilePath = "output/count/part-r-00000";
        String outputFilePath = "output/count/output-sequence-file.seq";
        
        // Hadoop Configuration and SequenceFile Writer
        Configuration conf = new Configuration();
        Path outputPath = new Path(outputFilePath);
        // Path clusters = new Path("output/");
        Path clusterOutput = new Path("output/clusterout");
        FileSystem fs=FileSystem.get(conf);
        SequenceFile.Writer writer = SequenceFile.createWriter(fs,conf,outputPath, IntWritable.class, VectorWritable.class);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line into key and value (tab-separated)
                String[] parts = line.split("\t");
                if (parts.length < 2) continue; // Skip invalid lines

                String key = parts[0];
                String vectorString = parts[1].replaceAll("[\\[\\]]", ""); // Remove brackets

                // Parse the vector values as doubles
                String[] vectorValues = vectorString.split(",");
                DoubleWritable[] writableVector = new DoubleWritable[vectorValues.length];
                
                for (int i = 0; i < vectorValues.length; i++) {
                    writableVector[i] = new DoubleWritable(Double.parseDouble(vectorValues[i].trim()));
                }

                // Write the key and vector to the SequenceFile
                writer.append(new Text(key), new DoubleArrayWritable(writableVector));
            }
        } finally {
            writer.close();
        }
        
        CanopyDriver.run(conf, outputPath, clusterOutput,new EuclideanDistanceMeasure(),3,1.5,true,0.01,false);

        System.out.println("Clustering completed. Check output in HDFS at: " + clusterOutput.toString());
        System.out.println("Data successfully written to SequenceFile: " + outputFilePath);
        return 1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(0);
    }
}