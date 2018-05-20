package com.cm55.ctree;

public interface TreeAdapter<P, C> {

  P createPackage(String name);
  C createClass(String name);
  void addClassChild(P parent, C child);
  void addPackageChild(P parent, P child);
}
