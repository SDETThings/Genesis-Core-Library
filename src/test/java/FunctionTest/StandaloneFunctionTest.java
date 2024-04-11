package FunctionTest;

import APIUtils.APIAuthentication;
import BasicUtils.CommonMethods;

import java.io.File;

public class StandaloneFunctionTest {
    public static void main(String[] args)
    {
        CommonMethods commonMethods = new CommonMethods();
        //commonMethods.getFutureTimeStamp("yyyy-MM-dd HH:mm:ss",-10);
        System.out.println(commonMethods.getFutureTimeStamp("yyyy-MM-dd HH:mm:ss",10,"2024-04-15"));
        //System.out.println(commonMethods.findFileFromNestedFolderStructure(new File("./src/test/resources/asd"),"abc",".txt"));

    }
}
