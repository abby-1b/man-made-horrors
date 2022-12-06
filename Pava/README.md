
# Pava

Have you ever wanted to combine the simple yet extremely specific syntax of Python with the object-oriented class-inherited spagetti-like syntax of Java?

No? Too bad.

Let's say you have this Java code:
```java
class TestClass {
	public static void main(String[] args) {
		System.out.println("Hello, World!");
	}
}
```

That's pretty nice, but it has a lot of overhead that seems very clunky. Why do _I_ as a programmer need to both indent my code _and_ use brackets? Can't I just do the one?

Since I'm not going to be writing unindented Java anytime soon, here's what the above class looks like in Pava:
```java
class TestClass
	public static void main(String[] args)
		System.out.println("Hello, World!")
```

Ah, isn't that a lot simpler?

Pava takes care of brackets and semicolons for you, so you can spend more time pulling your hair out and less time looking for a missing semicolon nested eight indents deep in a seven-thousand line class file.

# Why Pava?

Jython was taken.

I wanted to see this idea become a reality.

It doesn't have native syntax support (unless you compile it to Java) or even syntax highlighting.

Regardless, it works well enough for a small proof-of-concept.

[Here's the hellscape this idea came from.](https://www.reddit.com/r/ProgrammerHumor/comments/2wrxyt/a_python_programmer_attempting_java/)

![Java formatted like Python](https://i.imgur.com/wG51k7v.png)
