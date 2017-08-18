# uml-io
## uml-io-core
* provides a basic set of tools to create/read/write Eclipse UML2 models in standalone applications
* using standard Eclipse EMF and Eclipse UML2 maven central dependencies
* with a basic set of builders to programmatically build Eclipse UML2 models and profiles 
* focused on class diagram elements

read example:
```java
org.eclipse.uml2.uml.Model model = Uml2Utils.readFile(new File(filename));
System.out.printf("model %s has %d nested packages%n", model.getName(), model.getNestedPackages().size());		
```
write example:
org.eclipse.uml2.uml.Model model = ...
```java
org.eclipse.uml2.uml.Model model = ...
Uml2Utils.writeModel(model, new File("/home/user/test.uml"));
```
