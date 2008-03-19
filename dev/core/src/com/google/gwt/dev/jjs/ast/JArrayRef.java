/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.dev.jjs.ast;

import com.google.gwt.dev.jjs.SourceInfo;

/**
 * Java array reference expression.
 */
public class JArrayRef extends JExpression {

  private JExpression instance;
  private JExpression indexExpr;

  public JArrayRef(JProgram program, SourceInfo info, JExpression instance,
      JExpression indexExpr) {
    super(program, info);
    this.instance = instance;
    this.indexExpr = indexExpr;
  }

  public JExpression getIndexExpr() {
    return indexExpr;
  }

  public JExpression getInstance() {
    return instance;
  }

  public JType getType() {
    JType type = instance.getType();
    JNullType typeNull = program.getTypeNull();
    if (type == typeNull) {
      return typeNull;
    }
    JArrayType arrayType = (JArrayType) type;
    JType elementType = arrayType.getElementType();
    if (elementType instanceof JReferenceType
        && !program.typeOracle.isInstantiatedType((JReferenceType) elementType)) {
      return typeNull;
    }
    return elementType;
  }

  public boolean hasSideEffects() {
    // TODO: make the last test better when we have null tracking.
    return instance.hasSideEffects() || indexExpr.hasSideEffects()
        || instance.getType() == program.getTypeNull();
  }

  public void traverse(JVisitor visitor, Context ctx) {
    if (visitor.visit(this, ctx)) {
      instance = visitor.accept(instance);
      indexExpr = visitor.accept(indexExpr);
    }
    visitor.endVisit(this, ctx);
  }

}
