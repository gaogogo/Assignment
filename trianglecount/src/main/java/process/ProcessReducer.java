package process;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ProcessReducer extends Reducer<Text, IntWritable, NullWritable, NullWritable> {

    private final static int GOAL = 3;
    private long count;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        count = 0;
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;

        for(IntWritable value : values){
            sum += value.get();
        }
        if(GOAL == sum){
            count++;
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.getCounter(TriangleCounter.COUNTER).increment(count);
    }
}
