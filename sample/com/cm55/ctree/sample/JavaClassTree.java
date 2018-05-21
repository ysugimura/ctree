package com.cm55.ctree.sample;

import java.util.*;

public class JavaClassTree {
  
  /** ノード。パッケージあるいはクラスを下位で定義する */
  public abstract static class Node {
    protected List<Node>children;
    
    /** ノード名称 */
    private String name;

    /** 名称を指定してノードを作成する */
    private Node(String name) {
      this.name = name;
      children = new ArrayList<Node>();
    }
   
    public void addChild(Node child) {
      children.add(child);
    }
    
    public String getName() {
      return name;
    }
  }

  /** パッケージを表すノード */
  public static class PackageNode extends Node {
    /** パッケージノードを名前付で作成する */
    PackageNode(String name) {
      super(name);
    }
  }

  /** クラスを表すノード */
  public static class ClassNode extends Node {    
    ClassNode(String name) {
      super(name);
    }
  }
  
  /** ツリー構造をインデント付で文字列化する */
  public static String toString(PackageNode root) {
    StringBuilder s = new StringBuilder();
    new Object() {
      void show(String indent, PackageNode pkg) {
        pkg.children.stream().forEach(n-> {
          s.append(indent + n.getName() + "\n");
          if (n instanceof PackageNode) {
            show(indent + " ", (PackageNode)n);
          }
        });
      }
    }.show("", root);
    return s.toString();
  }
}
