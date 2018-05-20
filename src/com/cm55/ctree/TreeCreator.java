package com.cm55.ctree;

import java.util.*;
import java.util.stream.*;

/** 
 * パッケージノードのマップ
 * 
 * @author ysugimura
 */
public class TreeCreator<P, C> {

  TreeAdapter<P, C>adapter;
  
  /** パッケージのフルパス/ノードのマップ */
  Map<String, P>map = new HashMap<>();
  
  public TreeCreator(TreeAdapter<P, C>adapter) {
    this.adapter = adapter;
    map.put("",  adapter.createPackage(""));
  }
  
  public P getRoot() {
    return map.get("");
  }
  
  /** スラッシュ区切りのパッケージパスを指定する。 */
  public P ensurePackage(String fullPath) {
    if (fullPath.startsWith(".") || fullPath.endsWith(".")) throw new IllegalArgumentException();
    
    // このパスのノードがある
    P node = map.get(fullPath);
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
    map.put(fullPath, child);
    return child;
  }
  
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
  
  @Override
  public String toString() {
    return 
    map.entrySet().stream()
          .sorted((a, b)->a.getKey().compareTo(b.getKey()))
          .map(e->e.getKey() + "=" + e.getValue())
          .collect(Collectors.joining("\n"));
  }

}