package preprocess;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 *  预处理map阶段
 */
public class PreprocessMapper extends Mapper<LongWritable, Text, Text, Text>{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        Text from = new Text();
        Text to = new Text();

        String buffer[] ;
        StringTokenizer itr = new StringTokenizer(value.toString(),"\n");

        while(itr.hasMoreTokens()){

            buffer = itr.nextToken().split(" ");

            from.set(buffer[0]);
            to.set(buffer[1]);

            if(buffer[0].compareTo(buffer[1]) == 0) //过滤A->A的边
                continue;

            context.write(from, to);
            context.write(to, from);
        }

    }
}
