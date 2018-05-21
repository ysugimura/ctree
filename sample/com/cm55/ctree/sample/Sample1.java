package com.cm55.ctree.sample;

import java.io.*;

import com.cm55.ctree.*;

/**
 * このクラスが所属するクラスパスフォルダあるいはjarファイルの中身のクラス名をすべて取得する。
 * ただし、匿名クラスを除く。
 * @author ysugimura
 */
public class Sample1 {
  public static void main(String[]args) throws IOException {
    ClassNames classNames = ClassNames.loadFor(Sample1.class);
    classNames.getNameSet().forEach(System.out::println);
  }
}
