package Convert;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.VectorWritable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DenseToSequenceFileConverter {
    public static void main(String[] args) throws IOException {
        String inputFilePath = "/part-r-00000";
        String sequenceFilePath = "/";

        Configuration conf = new Configuration();
        Path path = new Path(sequenceFilePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             SequenceFile.Writer writer = SequenceFile.createWriter(conf,
                     SequenceFile.Writer.file(path),
                     SequenceFile.Writer.keyClass(Text.class),
                     SequenceFile.Writer.valueClass(VectorWritable.class))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Parse each line to extract the key and vector values
                String[] parts = line.split(" -> ");
                String key = parts[0].trim();
                String vectorStr = parts[1].replaceAll("[\\[\\]]", ""); // Remove brackets

                // Convert the vector to Mahout DenseVector
                String[] values = vectorStr.split(",");
                DenseVector denseVector = new DenseVector(values.length);
                for (int i = 0; i < values.length; i++) {
                    denseVector.set(i, Double.parseDouble(values[i].trim()));
                }

                // Write to SequenceFile
                writer.append(new Text(key), new VectorWritable(denseVector));
            }
            System.out.println("Dense vectors converted to SequenceFile format at: " + sequenceFilePath);
        }
    }
}
