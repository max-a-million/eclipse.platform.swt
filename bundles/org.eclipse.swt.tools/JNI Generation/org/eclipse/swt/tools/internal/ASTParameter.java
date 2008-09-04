/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.File;

public class ASTParameter extends ASTItem implements JNIParameter {
	ASTMethod method;
	int parameter;
	String data;

public ASTParameter(ASTMethod method, int parameter) {
	this.method = method;
	this.parameter = parameter;
}

public String getCast() {
	String cast = ((String)getParam("cast")).trim();
	if (cast.length() > 0) {
		if (!cast.startsWith("(")) cast = "(" + cast;
		if (!cast.endsWith(")")) cast = cast + ")";
	}
	return cast;
}

public String getMetaData() {
	if (data != null) return data;
	String className = method.getDeclaringClass().getSimpleName();
	String key = className + "_" + JNIGenerator.getFunctionName(method) + "_" + parameter;
	MetaData metaData = method.declaringClass.metaData;
	String value = metaData.getMetaData(key, null);
	if (value == null) {
		key = className + "_" + method.getName() + "_" + parameter;
		value = metaData.getMetaData(key, null);
	}
	/*
	* Support for lock.
	*/
	if (value == null && method.getName().startsWith("_")) {
		key = className + "_" + JNIGenerator.getFunctionName(method).substring(2) + "_" + parameter;
		value = metaData.getMetaData(key, null);
		if (value == null) {
			key = className + "_" + method.getName().substring(1) + "_" + parameter;
			value = metaData.getMetaData(key, null);
		}
	}
	if (value == null) value = "";	
	return value;
}

public JNIMethod getMethod() {
	return method;
}

public JNIClass getTypeClass() {
	ASTType type = (ASTType)getType();
	//TODO different package
	ASTClass declaringClass = method.declaringClass;
	String sourcePath  = declaringClass.sourcePath;
	sourcePath = new File(sourcePath).getParent() + "/" + type.getSimpleName() + ".java";
	return new ASTClass(sourcePath, declaringClass.metaData);
}

public JNIType getType() {
	return method.getParameterTypes()[parameter];
}

public JNIType getType64() {
	return method.getParameterTypes64()[parameter];
}

public int getParameter() {
	return parameter;
}

public void setCast(String str) {
	setParam("cast", str);
}

public void setMetaData(String value) {
	data = value;
}
}
