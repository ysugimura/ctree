package com.cm55.ctree;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

public class ClassNamesBinderTest {

  public static void main(String[]args) throws IOException {
    URLClassLoader cl = (URLClassLoader)ClassNamesBinderTest.class.getClassLoader();
    List<String>classPathFiles = getClassPathFiles(cl);
    List<ClassNames>classNamesList = new ArrayList<>();
    for (String classPathFile: classPathFiles) {
      classNamesList.add(ClassNames.load(new File(classPathFile)));
    }
    System.out.println("" + ClassNamesBinder.checkConflict(classNamesList));
  }
  
  public static List<String>getClassPathFiles(URLClassLoader classLoader) {
    
    return new ArrayList<String>(
      Arrays.stream(classLoader.getURLs())
        .map(url->URLDecoder.decode(url.getFile()))
        .collect(Collectors.toList())
   );
  }
}
