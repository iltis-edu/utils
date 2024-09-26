package de.tudortmund.cs.iltis.utils.general.viewcopier;

import static de.tudortmund.cs.iltis.utils.general.viewcopier.CloneableList.clist;
import static de.tudortmund.cs.iltis.utils.general.viewcopier.CoordinatesInt.coordsBasic;
import static de.tudortmund.cs.iltis.utils.general.viewcopier.CoordinatesInteger.coords;
import static de.tudortmund.cs.iltis.utils.general.viewcopier.ReferenceList.rlist;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import org.junit.Test;

public class ViewCopierTest extends AdvancedTest {
    // CoordinatesInteger
    //////////////////////////////////////////////////////////////////
    @Test
    public void testCopyReferencesCoordinatesIntegerViewPlane() {
        CoordinatesInteger orig = coords(10, 20, 30);
        CoordinatesInteger view = ViewCopier.copy(orig, "plane");
        assertEquals(coords(10, 20, null), view);

        // change x in copy only:
        view.setX(100);
        assertEquals(coords(10, 20, 30), orig);
        assertEquals(coords(100, 20, null), view);
    }

    @Test
    public void testCopyReferencesCoordinatesIntegerViewDefault() {
        CoordinatesInteger orig = coords(10, 20, 30);
        orig.setHidden(444);
        CoordinatesInteger view = ViewCopier.copy(orig);
        assertEquals(coords(10, 20, 30), view);

        // change x in copy only:
        view.setX(100);
        assertEquals(444, (int) orig.getHidden());
        assertEquals(coords(100, 20, 30), view);
        assertNull(view.getHidden());
    }

    // CoordinatesInt
    //////////////////////////////////////////////////////////////////
    @Test
    public void testCopyReferencesCoordinatesIntViewPlane() {
        CoordinatesInt orig = coordsBasic(10, 20, 30);
        CoordinatesInt view = ViewCopier.copy(orig, "plane");
        assertEquals(coordsBasic(10, 20, 0), view);

        // change x in copy only:
        view.setX(100);
        assertEquals(coordsBasic(10, 20, 30), orig);
        assertEquals(coordsBasic(100, 20, 0), view);
    }

    @Test
    public void testCopyReferencesCoordinatesIntViewDefault() {
        CoordinatesInt orig = coordsBasic(10, 20, 30);
        CoordinatesInt view = ViewCopier.copy(orig);
        assertEquals(coordsBasic(10, 20, 30), view);

        // change x in copy only:
        view.setX(100);
        assertEquals(coordsBasic(10, 20, 30), orig);
        assertEquals(coordsBasic(100, 20, 30), view);
    }

    // ReferenceList
    //////////////////////////////////////////////////////////////////
    @Test
    public void testCopyReferenceListViewDefault() {
        ReferenceList orig = rlist(10, 20);
        ReferenceList view = ViewCopier.copy(orig);
        assertEquals(orig, view);

        // change list in view (and also in orig):
        view.addValue(30);
        assertEquals(rlist(10, 20, 30), view);
        assertEquals(rlist(10, 20, 30), orig);
    }

    // CloneableList
    //////////////////////////////////////////////////////////////////
    @Test
    public void testCopyCloneCloneableListViewDefault() {
        CloneableList orig = clist(10, 20);
        CloneableList view = ViewCopier.copy(orig);
        assertEquals(orig, view);

        // change list in view but not in orig
        view.addValue(30);
        assertEquals(clist(10, 20, 30), view);
        assertEquals(clist(10, 20), orig);
    }

    // Exceptions
    //////////////////////////////////////////////////////////////////
    @Test
    public void testExceptionOnMissingDefaultConstructor() {
        assertThrowsVerbose(
                IllegalArgumentException.class,
                () -> ViewCopier.copy(new MissingDefaultConstructor("foo"), "FAIL"));
    }
}
