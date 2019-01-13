package com.cm55.ctree;

import java.util.*;

public class ClassNamesTest {

  public static void main(String[]args) throws Exception {
    
    List<ClassNames>names = ClassNames.loadAllFor9();
    names.forEach(cn-> {
    System.out.println("" + cn.file);
    cn.nameSet.stream().forEach(System.out::println);
    });
    
    /*
    Arrays.stream(System.getProperty("java.class.path").split(File.pathSeparator))
    .forEach(System.out::println);
    */
    
  }
}
