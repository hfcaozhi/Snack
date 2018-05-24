package com.snack.cn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MaxScore {
	public int getMaxScore() throws IOException{  
        File file=new File("maxScore.txt");  
        if(!file.exists()){  
            file.createNewFile();  
  
        }  
        BufferedReader br=new BufferedReader(new FileReader(file));  
        String s=br.readLine();  
        int max=0;  
        if(!"".equals(s)&&s!=null){  
            max=Integer.parseInt(s);  
        }  
        else{  
            max=0;  
        }  
        if(SnackClient.score*100>max){  
            max=SnackClient.score*100;  
            BufferedWriter bw=new BufferedWriter(new FileWriter(file));  
            bw.write(max+"");  
            bw.close();  
        }  
        br.close();  
        return max;  
    }  
}
