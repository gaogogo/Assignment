package main;

import preprocess.Preprocess;
import process.Process;

public class TriangleCount {
    public static void main(String []args) throws Exception{
        if(args.length < 3) {
            throw new Exception("Insufficient Parameters");
        }

        String[] forPre = {args[0], args[1]};
        Preprocess.main(forPre);
        String[] forPro = {args[1], args[2]};
        Process.main(forPro);
    }
}
