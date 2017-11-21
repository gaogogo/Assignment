package preprocess;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Preprocess {
    public static void main(String []args){
        try{
            Configuration conf = new Configuration();
            Job job = new Job(conf,"TC-preprocess");
            job.setJarByClass(Preprocess.class);
            job.setMapperClass(PreprocessMapper.class);
            job.setReducerClass(PreprocessReducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.waitForCompletion(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
