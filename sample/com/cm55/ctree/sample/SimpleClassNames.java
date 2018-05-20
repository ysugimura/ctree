package com.cm55.ctree.sample;

import java.io.*;

import com.cm55.ctree.*;

public class SimpleClassNames {

  public static void main(String[]args) throws IOException {
    ClassNames classNames = ClassNames.loadFor(SimpleClassNames.class);
    classNames.getNameSet().forEach(System.out::println);
  }

}
