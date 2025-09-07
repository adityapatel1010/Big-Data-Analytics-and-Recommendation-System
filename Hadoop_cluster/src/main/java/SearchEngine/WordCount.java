package SearchEngine;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.ArrayWritable;
// import org.apache.hadoop.io.IntArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.VarIntWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.apache.hadoop.util.Tool;
import org.apache.mahout.math.RandomAccessSparseVector;
// import org.apache.mahout.math.SparseVector;
import org.apache.hadoop.util.ToolRunner;

import utils.Paths;
import org.json.JSONObject;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.Vector;

public class WordCount  extends Configured implements Tool{

  // public static class TokenizerMapper
  //      extends Mapper<Object, Text, Text, IntWritable>{

  //   private final static IntWritable one = new IntWritable(1);
  //   private Text word = new Text();

  //   public void map(Object key, Text value, Context context
  //                   ) throws IOException, InterruptedException {
  //     StringTokenizer itr = new StringTokenizer(value.toString());
  //     while (itr.hasMoreTokens()) {
  //       word.set(itr.nextToken());
  //       context.write(word, one);
  //     }
  //   }
  // }

  public static class TokenizerMapper
            extends Mapper<Object, Text, Text, VectorWritable>{

        // private final DoubleWritable one = new DoubleWritable(1.0);
        
        public static volatile HashMap<String, Integer> wordFrequency = new HashMap<>();
        public static volatile int ctr=0;

        public void map(Object key, Text document, Context context) throws IOException, InterruptedException {
            JSONObject json = new JSONObject(document.toString());
            Text content = new Text(json.get("text").toString());
            String movie_title = json.get("title") .toString().replaceAll(","," ");
            // String doc_id = json.get("id").toString() + " " +str;
            // Vector vec = new Vector();
            DenseVector vec = new DenseVector(100000);
            // RandomAccessSparseVector vec = new RandomAccessSparseVector(100000);
            // ArrayList<Integer> vec = new ArrayList<Integer>(new Integer[500]);
            // Integer[] vec = new Integer[100000];
            // IntWritable[] vec= new  IntWritable[100000];
            StringTokenizer words = new StringTokenizer(content.toString(), " \'\n.,!?:()[]{};\\/\"*");
            while (words.hasMoreTokens()) {
                String word = words.nextToken().toLowerCase();
                if (word.equals("")) {
                    continue;
                }
                if(wordFrequency.containsKey(word)){
                  // vec[wordFrequency.get(word)]=new IntWritable(vec[wordFrequency.get(word)].get()+1);
                  vec.set(wordFrequency.get(word),vec.get(wordFrequency.get(word))+1);
                }
                else{
                  wordFrequency.put(word,ctr);
                  // vec[ctr]=1;
                  // vec.set(ctr,1);
                  // vec[ctr].set(1);
                  ctr+=1;
                }
              }
            // context.write(new Text(movie_title),new ArrayWriteable(vec));
            context.write(new Text(movie_title),new VectorWritable(vec))   ;
        }
    }


  public int run (String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(getConf(), "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    // job.setCombinerClass(IntSumReducer.class);
    // job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    // job.setOutputValueClass(org.apache.mahout.math.Vector.class);
    // job.setOutputValueClass(RandomAccessSparseVector.class);
    job.setOutputValueClass(VectorWritable.class);
    // job.setOutputValueClass(Integer[].class);
    // job.setOutputValueClass(ArrayList.class);
    FileInputFormat.addInputPath(job, new Path(args[1]));
    FileOutputFormat.setOutputPath(job, new Path(Paths.CTR_PATH));
    job.waitForCompletion(true);
    System.out.println("Mapper Complete");
    // KMeansDriver.run(conf, new Path(Paths.CTR_PATH), clusters, clusterOutput,0.01, 10, true, 0.01, true);
    return 0;
  }

  
  public static void main(String[] args) throws Exception {
    System.exit(0);
  }
} 