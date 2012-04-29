ROOT		= .
SONAR_DIR	= $(shell brew --prefix sonar)
INSTALL_DIR	= $(SONAR_DIR)/libexec/extensions/plugins
PLUGIN		= sonar-cxx-plugin-*-SNAPSHOT.jar

clean:
	mvn clean

build:
	mvn package

install:
	rm -f $(INSTALL_DIR)/$(PLUGIN)
	cp $(ROOT)/target/$(PLUGIN) $(INSTALL_DIR)
	sonar restart

