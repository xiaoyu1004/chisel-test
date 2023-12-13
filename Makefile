project = ChiselTest

MAIN = pc_control.PCcontrolTopMain

MILL = ./mill

compile:
	$(MILL) $(project).compile

verilog:
	$(MILL) -i $(project).runMain $(MAIN)

test:
	$(MILL) $(project).test 

clean:
	./mill clean