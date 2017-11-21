package process;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class ProcessMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private final static IntWritable ONE = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        Text outputKey = new Text();

        StringTokenizer itr = new StringTokenizer(value.toString(),"\n");
        while(itr.hasMoreTokens()){
            outputKey.set(itr.nextToken());
            context.write(outputKey, ONE);
        }
    }
}
