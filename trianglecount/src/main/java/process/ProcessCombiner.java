package process;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ProcessCombiner extends Reducer<Text, IntWritable, Text, IntWritable>{

    private final static int GOAL = 3;
    private final static long ONE = 1l;

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        IntWritable result = new IntWritable();
        int sum = 0;

        for(IntWritable value : values){
            sum += value.get();
        }

        if(sum == GOAL){
            context.getCounter(TriangleCounter.COUNTER).increment(ONE);
        }else{
            result.set(sum);
            context.write(key, result);
        }
    }
}
