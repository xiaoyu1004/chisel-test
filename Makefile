project = ChiselTest

RTL_DIR = genarated_rtl_dir
MAIN = counter.Main

MILL = ./mill

compile:
	$(MILL) $(project).compile

verilog:
	$(MILL) -i $(project).runMain $(MAIN)

test:
	$(MILL) $(project).test

clean:
	./mill clean