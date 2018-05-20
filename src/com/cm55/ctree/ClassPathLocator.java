package com.cm55.ctree;

import java.io.*;
import java.net.*;

/**
 * 実行中のクラスのクラスパスを取得する。
 * 開発環境の場合は、そのクラスパス（フォルダ）、jarでの実行の場合はjarファイルを返す。
 * @author ysugimura
 */
class ClassPathLocator {

  /**
   * 実行中のjarファイルを返す。jarでない場合（開発環境の場合）はクラスパスを返す。
   * @return jarファイル、あるいはクラスパス
   */
  static File getLocation() {    
    return getLocation(ClassPathLocator.class);
  }
  
  /**
   * 実行中のjarファイルを返す。jarでない場合（開発環境の場合）はクラスパスを返す。
   * ただし、指定された基準クラスに基づく。{@link ClassPathLocator}と呼び出し側のクラスが、別jarや別クラスパスの場合は
   * こちらを使用しなければならない。
   * @param clazz　基準とするクラス
   * @return jarファイル、あるいはクラスパス
   */
  static File getLocation(Class<?>clazz) {
    // このクラスのリソースURLを求める
    URL classUrl = clazz.getResource(clazz.getSimpleName() + ".class");
    
    // ドットの数を数えて、このクラスのパッケージ階層数を取得する。
    int pkgHier =  (int)ClassPathLocator.class.getName().chars().filter(c->c == '.').count();
    
    // このクラスの格納されているjarファイルを取得する。jarでない場合（開発環境の場合）はnullを返す。
    return getLocationFromClassUrl(classUrl, pkgHier);
  }

  /** 
   * .classのリソースURLからjarファイルを取得する。
   * jarでの実行でない場合（開発環境の場合）はクラスパスを返す。
   * @param url .classのリソースURL
   * @param pkgHier このクラスのパッケージ階層数
   * @return .classがjar内にある場合、そのjarファイル。jar出ない場合はそのクラスパス
   */
  static File getLocationFromClassUrl(URL url, int pkgHier) {   
    // jar:の場合
    if (url.getProtocol().equals("jar")) {
      try {
        JarURLConnection conn = (JarURLConnection)url.openConnection();
        return new File(conn.getJarFileURL().getFile());
      } catch (Exception ex) {
        return null;
      }
    } 
    
    // file:の場合
    if (url.getProtocol().equals("file")) {
      File file = new File(url.getFile()).getParentFile();
      for (int i = 0; i < pkgHier; i++) file = file.getParentFile();      
      return file;
    }
    
    // ありえない
    throw new RuntimeException("Could not determine jar or file");
  }
}
