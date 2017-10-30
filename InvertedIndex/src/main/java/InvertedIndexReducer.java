import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class InvertedIndexReducer extends Reducer<Text, IntWritable, Text, String>{

    private String prev;
    private StringBuilder result;
    private float frequency;
    private int bookNum;
    private int wordFrequency;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        prev = null;
        result = new StringBuilder();
        frequency = 0;
        bookNum = 0;
        wordFrequency = 0;
    }

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        String current = key.toString().split("#")[0];
        String bookName = key.toString().split("#")[1];
        int sum = 0;

        if(!current.equals(prev)){

            if(prev != null){
                frequency = (float)wordFrequency/bookNum;

                context.write(new Text(prev), String.format("%.2f,%s", frequency, result.toString()));
                bookNum = 0;
                wordFrequency = 0;
                result = new StringBuilder();
            }
            prev = current;
        }

        for(IntWritable value : values){
            sum += value.get();
        }
        result.append(String.format("%s:%d;",bookName,sum));
        bookNum ++;
        wordFrequency += sum;
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        frequency = (float)wordFrequency/bookNum;
        context.write(new Text(prev), String.format("%.2f,%s", frequency, result.toString()));
    }
}
