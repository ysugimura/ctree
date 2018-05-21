package com.cm55.ctree;

/**
 * {@link CreateTree}のツリー構築用アダプタ
 * @author ysugimura
 *
 * @param <P>　パッケージノード
 * @param <C> クラスノード
 */
public interface TreeAdapter<P, C> {

  /** 
   * 指定名称のパッケージノードを作成する。
   * "com.cm55.sample"などではなく、"com", "cm55"等が指定される
   * @param name
   * @return
   */
  P createPackage(String name);
  
  /** 
   * 指定名称のクラスノードを作成する。
   * "com.cm55.sample.FooBar"等ではなく、"FooBar"が指定される。
   * @param name
   * @return
   */
  C createClass(String name);
  
  /** 指定されたパッケージノード下にクラスノードを追加する */
  void addClassChild(P parent, C child);
  
  /** 指定されたパッケージノード下にパッケージノードを追加する */
  void addPackageChild(P parent, P child);
}
