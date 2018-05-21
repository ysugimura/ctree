package com.cm55.ctree;

import java.util.*;

/** 
 * Javaクラス名一覧をツリー構造に変換する。
 * ツリーノードの作成には{@link TreeAdapter}を用いる。
 * @author ysugimura
 */
public class TreeCreator<P, C> {

  /** ツリー構築アダプタ */
  TreeAdapter<P, C>adapter;
  
  /** パッケージのフルパス/ノードのマップ */
  Map<String, P>packageMap = new HashMap<>();
  
  /** アダプタを指定する */
  public TreeCreator(TreeAdapter<P, C>adapter) {
    this.adapter = adapter;
    packageMap.put("",  adapter.createPackage(""));
  }

  /** パッケージルートを取得する */
  public P getRoot() {
    return packageMap.get("");
  }
  
  /** 
   * 指定されたパッケージのノードを取得する。なければ作成する。
   * スラッシュ区切りのパッケージパスを指定する。 */
  public P ensurePackage(String fullPath) {
    if (fullPath.startsWith(".") || fullPath.endsWith(".")) throw new IllegalArgumentException();
    
    // このパスのノードがある
    P node = packageMap.get(fullPath);
    if (node != null) return node;
    
    // 無い。一つ上のノードを探す
    P parent;
    P child;
    int index = fullPath.lastIndexOf('.');
    String parentPath;
    String childName;
    if (index < 0) {
      parentPath = "";
      childName = fullPath;
    } else {
      parentPath = fullPath.substring(0, index);
      childName = fullPath.substring(index + 1);
    }
    parent = ensurePackage(parentPath);
    child = adapter.createPackage(childName);    

    adapter.addPackageChild(parent, child);
    packageMap.put(fullPath, child);
    return child;
  }

  /** 指定されたクラスノードを作成する */
  public C createClassNode(String className) {
    int index = className.lastIndexOf('.');
    String packageName = "";
    if (index > 0) {
      packageName = className.substring(0, index);
      className = className.substring(index + 1);
    }
    C classNode = adapter.createClass(className);
    adapter.addClassChild(ensurePackage(packageName), classNode);
    return classNode;
  }
  
  /** {@link ClassNames}からツリー構造を作成し、ルートノードを返す */
  public static <P, C>P createTree(ClassNames classNames, TreeAdapter<P, C>adapter) {
    TreeCreator<P, C> creator = new TreeCreator<P, C>(adapter);
    classNames.getNameSet().forEach(n->creator.createClassNode(n));
    return creator.getRoot();
  }
}