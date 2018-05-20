package com.cm55.ctree;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.stream.*;

/**
 * 一つのクラスパス（フォルダあるいはjarファイル）から読み出したすべてのクラス名を保持する。ただし、匿名クラスは無視する
 * "com.cm55.foo.bar.Sample$Test"という文字列。
 * 読み出し元のフォルダあるいはJarファイルを保持する。
 * @author ysugimura
 */
public class ClassNames {

  /** 読み出し元のフォルダもしくはjarファイル。ただし一つにまとめられた場合はnull */
  File file;  
  
  /** 読み出したすべてのJavaクラス名 */
  Set<String>nameSet;
  
  private ClassNames() {
    nameSet = new HashSet<String>();
  }
  
  ClassNames(File file, Set<String>names) {
    this.file = file;
    this.nameSet = names; 
  }

  public File getFile() {
    return file;
  }
  
  public Stream<String>getNameSet() {
    return nameSet.stream();
  }

  /** 
   * 指定クラスが所属するURLクラスローダのクラスパスに登録されたすべてのフォルダあるいはjarファイルを読み出し
   * {@link ClassNames}のリストを返す。
   * ※一般にJava9ではシステムクラスローダがURLクラスローダでは無いのでこれは使用できない。代替の方法は現在不明
   * @param clazz
   * @return
   * @throws IOException
   */
  public static List<ClassNames>loadAllFor(Class<?>clazz) throws IOException {
    return load((URLClassLoader)clazz.getClassLoader());
  }
  
  /** 指定クラスが所属するフォルダあるいはjarファイルを読み出し{@link ClassNames}を作成する */
  public static ClassNames loadFor(Class<?>clazz) throws IOException {
    return load(ClassPathLocator.getLocation(clazz));
  }
  
  /**
   * URLクラスローダに登録されたすべてのフォルダあるいはjarファイルを読み出し{@link ClassNames}のリストを作成する
   * @param file
   * @return
   * @throws IOException
   */
  public static List<ClassNames>load(URLClassLoader cl) throws IOException {
    List<ClassNames>classNamesList = new ArrayList<>();
    for (URL url: cl.getURLs()) {
      File file = new File(URLDecoder.decode(url.getFile(), System.getProperty("file.encoding")));
      classNamesList.add(load(file));
    }
    return classNamesList;
  }
  
  /**
   * フォルダあるいはJarファイルを読み出し{@link ClassNames}を作成する
   * @param file
   * @return
   * @throws IOException
   */
  public static ClassNames load(File file) throws IOException {
    if (!file.exists()) 
      throw new IOException("FIle not found:" + file);
    if (file.isDirectory())
      return folderClassNames(file);
    return jarClassNames(file);
  }
  
  /** フォルダを読み出し{@link ClassNames}を作成する */
  static ClassNames folderClassNames(File folder)  {
    Set<String>paths = new HashSet<>();
    new Object() {
      void get(String parentPath, File parent) {
        for (String name: parent.list()) {
          File child = new File(parent, name);
          if (child.isDirectory()) {
            get(parentPath + name + ".", child);
            continue;
          }
          if (name.endsWith(".class")) {
            String className = parentPath + name.substring(0, name.length() - ".class".length());
            if (includes(className))
              paths.add(className);
          }
        }
      }      
    }.get("", folder);
    return new ClassNames(folder, paths);
  }
  
  /** Jarファイルを読み出し{@link ClassNames}を作成する */
  static ClassNames jarClassNames(File jarFile) throws IOException {
    Set<String>result = new HashSet<>();
    try (JarFile jar = new JarFile(jarFile)) {
      Collections.list(jar.entries()).stream().forEach(entry-> {    
        String name = entry.getName();
        if (!name.endsWith(".class")) return;
        String className = (name.substring(0, name.length() - ".class".length())).replaceAll("/",  ".");
        if (includes(className))
          result.add(className);        
      });
    }  
    return new ClassNames(jarFile, result);
  }
  
  /** クラスファイル名から対象クラスであるかを決定 */
  static boolean includes(String classFileName) {    
    
    // $が存在しない場合
    int lastDollerIndex = classFileName.lastIndexOf('$');
    if (lastDollerIndex < 0) return true;
    
    // $がある、 $の後が数字の場合、匿名クラス
    return !Character.isDigit(classFileName.charAt(lastDollerIndex + 1));
  }
}
