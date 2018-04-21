#!/bin/bash
javac Ld41.java
width = 10
height = 10
if [$1 != ""]; then
	width = $1
fi

if [$2 != ""]; then
	height = $2
fi

java Ld41 $1 $2
