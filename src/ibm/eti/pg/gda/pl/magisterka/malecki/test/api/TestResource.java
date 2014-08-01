/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibm.eti.pg.gda.pl.magisterka.malecki.test.api;

import ibm.eti.pg.gda.pl.magisterka.malecki.test.core.data.DataSaver;
import ibm.eti.pg.gda.pl.magisterka.malecki.test.gui.Main;

/**
 *
 * @author SebaTab
 */
public class TestResource {
    private final Main main;
    private DataSaver dataSaver;
    
    public TestResource(Main main) {
        this.main = main;
    }
    public void startTest() {
        dataSaver = new DataSaver(main);
    }
}
