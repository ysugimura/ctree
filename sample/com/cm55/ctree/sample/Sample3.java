package com.cm55.ctree.sample;

import java.io.*;

import com.cm55.ctree.*;
import com.cm55.ctree.sample.JavaClassTree.*;

public class Sample3 {

  public static void main(String[]args) throws IOException {
    ClassNames classNames = ClassNames.loadFor(Sample1.class);
    PackageNode root = TreeCreator.createTree(classNames, new TreeAdapterImpl());    
    System.out.println(JavaClassTree.toString(root));
  }

}
