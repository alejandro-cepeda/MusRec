error id: 92157CB02BAC79BF93BEED54F82BA2B6
file://<WORKSPACE>/Main.scala
### java.lang.NullPointerException: Cannot read the array length because "a" is null

occurred in the presentation compiler.



action parameters:
offset: 9
uri: file://<WORKSPACE>/Main.scala
text:
```scala
object Ma@@

```


presentation compiler configuration:
Scala version: 2.13.13
Classpath:
<WORKSPACE>/.bloop/musrec/bloop-bsp-clients-classes/classes-Metals-P8_yYKFbTCaAxOYf-CbSBQ== [missing ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/com/sourcegraph/semanticdb-javac/0.10.4/semanticdb-javac-0.10.4.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.13/scala-library-2.13.13.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb




#### Error stacktrace:

```
java.base/java.util.Arrays.sort(Arrays.java:1234)
	scala.tools.nsc.classpath.JFileDirectoryLookup.listChildren(DirectoryClassPath.scala:125)
	scala.tools.nsc.classpath.JFileDirectoryLookup.listChildren$(DirectoryClassPath.scala:109)
	scala.tools.nsc.classpath.DirectoryClassPath.listChildren(DirectoryClassPath.scala:322)
	scala.tools.nsc.classpath.DirectoryClassPath.listChildren(DirectoryClassPath.scala:322)
	scala.tools.nsc.classpath.DirectoryLookup.list(DirectoryClassPath.scala:90)
	scala.tools.nsc.classpath.DirectoryLookup.list$(DirectoryClassPath.scala:84)
	scala.tools.nsc.classpath.DirectoryClassPath.list(DirectoryClassPath.scala:322)
	scala.tools.nsc.classpath.AggregateClassPath.$anonfun$list$3(AggregateClassPath.scala:106)
	scala.collection.immutable.Vector.foreach(Vector.scala:2124)
	scala.tools.nsc.classpath.AggregateClassPath.list(AggregateClassPath.scala:102)
	scala.tools.nsc.util.ClassPath.list(ClassPath.scala:34)
	scala.tools.nsc.util.ClassPath.list$(ClassPath.scala:34)
	scala.tools.nsc.classpath.AggregateClassPath.list(AggregateClassPath.scala:31)
	scala.tools.nsc.symtab.SymbolLoaders$PackageLoader.doComplete(SymbolLoaders.scala:297)
	scala.tools.nsc.symtab.SymbolLoaders$SymbolLoader.$anonfun$complete$2(SymbolLoaders.scala:249)
	scala.tools.nsc.symtab.SymbolLoaders$SymbolLoader.complete(SymbolLoaders.scala:247)
	scala.reflect.internal.Symbols$Symbol.completeInfo(Symbols.scala:1566)
	scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1538)
	scala.reflect.internal.Mirrors$RootsBase.init(Mirrors.scala:258)
	scala.tools.nsc.Global.rootMirror$lzycompute(Global.scala:75)
	scala.tools.nsc.Global.rootMirror(Global.scala:73)
	scala.tools.nsc.Global.rootMirror(Global.scala:45)
	scala.reflect.internal.Definitions$DefinitionsClass.ObjectClass$lzycompute(Definitions.scala:295)
	scala.reflect.internal.Definitions$DefinitionsClass.ObjectClass(Definitions.scala:295)
	scala.reflect.internal.Definitions$DefinitionsClass.init(Definitions.scala:1667)
	scala.tools.nsc.Global$Run.<init>(Global.scala:1252)
	scala.tools.nsc.interactive.Global$TyperRun.<init>(Global.scala:1351)
	scala.tools.nsc.interactive.Global.newTyperRun(Global.scala:1374)
	scala.tools.nsc.interactive.Global.<init>(Global.scala:294)
	scala.meta.internal.pc.MetalsGlobal.<init>(MetalsGlobal.scala:44)
	scala.meta.internal.pc.ScalaPresentationCompiler.newCompiler(ScalaPresentationCompiler.scala:608)
```
#### Short summary: 

java.lang.NullPointerException: Cannot read the array length because "a" is null