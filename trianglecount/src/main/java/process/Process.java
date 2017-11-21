package process;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Process {

    private static final String RESULT_OUTPUT = "result.txt";

    public static void main(String []args){
        try {
            Configuration conf = new Configuration();
            Job job = new Job(conf, "TC-process");
            job.setJarByClass(Process.class);
            job.setMapperClass(ProcessMapper.class);
            job.setReducerClass(ProcessReducer.class);
            job.setCombinerClass(ProcessCombiner.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            if(job.waitForCompletion(true)){
                Counter counter = job.getCounters().findCounter(TriangleCounter.COUNTER);
                FileSystem fs = FileSystem.get(conf);
                FSDataOutputStream out = fs.create( new Path(RESULT_OUTPUT));
                out.writeUTF(" THE COUNT OF TRIANGLE IS: " + counter.getValue()+"\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
