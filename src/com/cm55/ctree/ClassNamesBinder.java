package com.cm55.ctree;

import java.io.*;
import java.util.*;

/**
 * 複数の｛@link ClassNames}を一つにまとめる。あるいは重複の有無をチェックする。
 * @author ysugimura
 */
public class ClassNamesBinder {

  /**
   * 複数の{@link ClassNames}を一つにまとめる。ここでは重複はチェックされない。
   * @param classNamesList
   * @return
   */
  public static ClassNames bind(Collection<ClassNames>classNamesList) {
    Set<String>result = new HashSet<>();
    classNamesList.stream().forEach(classNames->result.addAll(classNames.nameSet));
    return new ClassNames(null, result);
  }

  /**
   * 複数の{@link ClassNames}の重複をチェックする。結果を文字列として返す
   * @param classNamesList
   * @return
   */
  public static String checkConflict(Collection<ClassNames>classNamesList) {
    StringBuilder conflict = new StringBuilder();
    Map<String, File>nameToFile = new HashMap<>();
    classNamesList.stream().forEach(classNames-> {
      classNames.nameSet.stream().forEach(name-> {
        File previous = nameToFile.get(name);
        if (previous != null) {
          conflict.append(name + " exists in \n  " + previous + "\n  " + classNames.file + "\n");
        }
        nameToFile.put(name, classNames.file);
      });
    });
    return conflict.toString();
  }
}
