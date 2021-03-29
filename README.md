# Transitive Performance Miner

A tool for deriving software performance knowledge by event log analysis. Implementation is
going to be a ProM Framework plug-in.

## Prerequisites

ProM plug-in development requires custom third-party jars usage, and the recommended way
to contribute is to setup an Eclipse environment. Necessary dependencies are meant to be
managed via Ivy. Preferred option is to install an IvyDE Eclipse addon and manipulate the
dependencies by XML configs. Please refer to the following sources as how to configure Ivy
and adjust the environment:

- [Basic Ivy + IvyDE usage](http://runningexample.blogspot.com/2014/02/Using-Ivy-for-dependency-management.html)
- [ProM discussion on packages 1](https://www.win.tue.nl/promforum/discussion/1128/compile-a-package)
- [ProM discussion on packages 2](https://www.win.tue.nl/promforum/discussion/1257/which-jar-should-i-import-manually)

Although IvyDE should be enough for retrieving all the required packages, it came out
that a manual Ant + Ivy JAR installation was needed in order to build the project. For more
information, check out [link](https://stackoverflow.com/questions/12677816/ant-target-failing-antlib-or-ivy-issue).

## Compilation and build

By default, `compileSources` target is being run, which includes resolving Ivy dependencies,
compilation and JAR generation. Therefore, executing

```
ant
```

is enough to build. 

## Troubleshooting

For any questions, suggestions, or bug reporting please refer to the
[issues](https://github.com/kostmetallist/transitive-performance-miner/issues) page.
