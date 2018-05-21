package com.cm55.ctree.sample;

import java.io.*;
import java.util.*;

import com.cm55.ctree.*;

/**
 * 注意：これはJava9では動作しない。
 * {@link ClassNames#loadAllFor(Class)}を参照のこと
 * @author ysugimura
 */
public class Sample2 {
  public static void main(String[]args) throws IOException {
    List<ClassNames>classNamesList = ClassNames.loadAllFor(Sample2.class);
    classNamesList.forEach(classNames-> {
      System.out.println(classNames.getFile());
      classNames.getNameSet().forEach(n-> System.out.println("  " + n));      
    });     
    System.out.println("conflicts....");
    System.out.println(ClassNamesBinder.checkConflict(classNamesList));
  }
}
