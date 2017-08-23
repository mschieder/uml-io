# uml-io
[![Build Status](https://travis-ci.org/mschieder/uml-io.svg?branch=master)](https://travis-ci.org/mschieder/uml-io)
[![Coverage Status](https://coveralls.io/repos/github/mschieder/uml-io/badge.svg?branch=master)](https://coveralls.io/github/mschieder/uml-io?branch=master)

## uml-io-core
* provides a basic set of tools to create/read/write Eclipse UML2 models in standalone applications
* using standard Eclipse EMF and Eclipse UML2 maven central dependencies
* with a basic set of builders to programmatically build Eclipse UML2 models and profiles 
* focused on class diagram elements

read example:
```java
org.eclipse.uml2.uml.Model model = Uml2Utils.read(new File("/home/user/test.uml"));
System.out.printf("model %s has %d nested packages%n", model.getName(),
	 model.getNestedPackages().size());		
```
write example:
```java
org.eclipse.uml2.uml.Model model = ...
Uml2Utils.write(model, new File("/home/user/test.uml"));
```
builder example:
```java
Model model = new ModelBuilder()
  .setName("testmodel").add(
    new PackageBuilder().setName("testpackage")
      .add(new ClassBuilder().setName("TestClass")
        .add(new PropertyBuilder().setVisibility(VisibilityKind.PRIVATE_LITERAL)
	  .setType(DefaultPrimitiveTypes.STRING).setName("testAttribute"))))
  .build();
Property property = Uml2Utils.findElement("testmodel::testpackage::TestClass::testAttribute", model);
System.out.println("attribute name: " + property.getName());
```
or even shorter (with static imports):
```java
import static org.batchjob.uml.io.utils.ClassModelUtils.*;
import static org.batchjob.uml.io.utils.DefaultPrimitiveTypes.STRING;
import static org.eclipse.uml2.uml.VisibilityKind.PRIVATE_LITERAL;
...
Model model = model("testmodel").add(package_("testpackage")
    .add(class_("TestClass").add(property(PRIVATE_LITERAL, STRING, "testAttribute")))).build();
Property property = Uml2Utils.findElement("testmodel::testpackage::TestClass::testAttribute", model);
System.out.println("attribute name: " + property.getName());
```
## Notices
Eclipse® is a registered trademark of the Eclipse Foundation, Inc.

UML® is a registered trademark of The Object Management Group
