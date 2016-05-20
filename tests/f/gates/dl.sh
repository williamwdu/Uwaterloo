#!/bin/bash

pre=http://upload.wikimedia.org/wikipedia/commons/thumb/6/64/
mid=_ANSI.svg/200px-
end=_ANSI.svg.png

for g in AND OR NOT NAND NOR XOR XNOR
do
    u="$pre$g$mid$g$end"
    wget "$u"
done
