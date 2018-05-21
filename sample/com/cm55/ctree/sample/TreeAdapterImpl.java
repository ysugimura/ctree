package com.cm55.ctree.sample;

import com.cm55.ctree.*;
import com.cm55.ctree.sample.JavaClassTree.*;


public class TreeAdapterImpl implements TreeAdapter<PackageNode, ClassNode> {

  @Override
  public PackageNode createPackage(String name) {
    return new PackageNode(name);
  }

  @Override
  public void addClassChild(PackageNode parent, ClassNode child) {
    parent.addChild(child);
  }      
  @Override
  public void addPackageChild(PackageNode parent, PackageNode child) {
    parent.addChild(child);
  }

  @Override
  public ClassNode createClass(String name) {
    return new ClassNode(name);
  }      
  
}
