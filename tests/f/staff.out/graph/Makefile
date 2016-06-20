# see these links:
# http://geosoft.no/development/javamake.html
# http://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html

DOTS = $(shell find . -type f -name '*.dot')
PDFS = $(DOTS:.dot=.pdf)
PNGS = $(DOTS:.dot=.png)

default: $(PDFS) $(PNGS)

%.pdf : %.dot
	dot -Tpdf -o $@ $<

%.png : %.dot
	dot -Tpng -o $@ $<



