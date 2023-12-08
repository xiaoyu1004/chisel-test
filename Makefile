project = ChiselTest

MAIN = counter.CounterMain

MILL = ./mill

compile:
	$(MILL) $(project).compile

verilog:
	$(MILL) -i $(project).runMain $(MAIN)

test:
	$(MILL) $(project).test 

clean:
	./mill clean