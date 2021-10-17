GGC_CORE_PATH=./ggc-core
GGC_APP_PATH=./ggc-app
CLASSPATH=$(shell pwd)/po-uilib/po-uilib.jar:$(shell pwd)/ggc-app/ggc-app.jar:$(shell pwd)/ggc-core/ggc-core.jar

# -Dui=newswing to run GUI
all::
	$(MAKE) $(MFLAGS) -C $(GGC_CORE_PATH)
	$(MAKE) $(MFLAGS) -C $(GGC_APP_PATH)
	java -cp $(CLASSPATH) -Dimport="input.import" ggc.app.App 