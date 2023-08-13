project = myoperator

RTL_DIR = gen_rtl_dir

MILL = ./mill

verilog:
	$(MILL) $(project).run -td $(RTL_DIR)

test:
	$(MILL) $(project).test

clean:
	rm -rf test_run_dir
	rm -rf out
	rm -rf $(RTL_DIR)