package de.tudortmund.cs.iltis.utils.io.parsable;

import static org.junit.Assert.assertEquals;

import de.tudortmund.cs.iltis.utils.test.AdvancedTest;
import java.util.*;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TaskInputTest extends AdvancedTest {
    @Test
    public void testCorrectTaskInputs() {
        Task actual = readFromFile("io/parsable/taskInputs-correct.xml");
        assertEquals(
                new Task(
                        new TaskInputFoo("one", "foo", 100),
                        new TaskInputBar("two", "bar", 2000),
                        new TaskInputFoo("three", "foo", 121)),
                actual);
    }

    @Test
    public void testIncompleteTaskInputFoo() {
        assertThrowsVerbose(
                ParsableMissingRequired.class,
                () -> readFromFile("io/parsable/taskInputs-incompleteFoo.xml"));
    }

    static Task readFromFile(String path) {
        return new XmlParsableReader<>(TaskSpec.class).read(path);
    }
}

@XmlRootElement(name = "Task")
class TaskSpec extends Parsable<Task, Object> {
    @XmlAttribute public ParsableString name = new ParsableString();

    public ParsableInteger value = new ParsableInteger();

    @XmlElement(name = "TaskInput")
    public List<ParsableElement> taskInputs;

    @Override
    protected Task parse(Optional<Object> context) {
        if (taskInputs == null) {
            taskInputs = Collections.emptyList();
        }
        return new Task(
                taskInputs.stream()
                        .map(this::createTaskInputFromElement)
                        .collect(Collectors.toList()));
    }

    private TaskInput createTaskInputFromElement(ParsableElement pelement) {
        Element element = pelement.value();
        if (!element.hasAttribute("type")) {
            // TODO: improve
            throw new ParsableMissingRequired(null);
        }
        String type = element.getAttribute("type");
        switch (type) {
            case "foo":
                return readTaskInputFooFromNode(element);
            case "bar":
                return readTaskInputBarFromNode(element);
                // TODO: improve
            default:
                throw new ParsableInvalidValue(pelement, "Unknown type '" + type + "'");
        }
    }

    private TaskInputFoo readTaskInputFooFromNode(Node node) {
        return new XmlParsableReader<>(TaskInputFooSpec.class).read(node, this);
    }

    private TaskInputBar readTaskInputBarFromNode(Node node) {
        return new XmlParsableReader<>(TaskInputBarSpec.class).read(node, this);
    }
}

// ====================================================================
// SPECIFICATION
// ====================================================================
abstract class TaskInputSpec<T extends TaskInput> extends Parsable<T, Object> {
    @XmlAttribute
    // TODO: throw InvalidSpec if ParsableField is null
    public ParsableString name = new ParsableString();

    @XmlAttribute public ParsableString type = new ParsableString();
}

@XmlRootElement(name = "TaskInput")
class TaskInputFooSpec extends TaskInputSpec<TaskInputFoo> {
    @XmlAttribute public ParsableInteger fooValue = new ParsableInteger();

    @Override
    protected TaskInputFoo parse(Optional<Object> context) {
        return new TaskInputFoo(
                name.nonempty().value(), type.nonempty().value(), fooValue.required().value());
    }
}

@XmlRootElement(name = "TaskInput")
class TaskInputBarSpec extends TaskInputSpec<TaskInputBar> {
    @XmlAttribute public ParsableInteger barValue = new ParsableInteger();

    @Override
    protected TaskInputBar parse(Optional<Object> context) {
        return new TaskInputBar(
                name.nonempty().value(), type.nonempty().value(), barValue.required().value());
    }
}

// ====================================================================
// IMPLEMENTATION
// ====================================================================
class Task {
    private final List<TaskInput> inputs;

    public Task(List<TaskInput> inputs) {
        this.inputs = inputs;
    }

    public Task(TaskInput... inputs) {
        this.inputs = new ArrayList<TaskInput>();
        Collections.addAll(this.inputs, inputs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task other = (Task) o;
        return Objects.equals(this.inputs, other.inputs);
    }
}

class TaskInput {
    protected final String name;
    protected final String type;

    public TaskInput(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInput taskInput = (TaskInput) o;
        return Objects.equals(name, taskInput.name) && Objects.equals(type, taskInput.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}

class TaskInputFoo extends TaskInput {
    protected final int fooValue;

    public TaskInputFoo(String name, String type, int fooValue) {
        super(name, type);
        this.fooValue = fooValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInputFoo that = (TaskInputFoo) o;
        return fooValue == that.fooValue && super.equals(that);
    }
}

class TaskInputBar extends TaskInput {
    protected final int barValue;

    public TaskInputBar(String name, String type, int barValue) {
        super(name, type);
        this.barValue = barValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInputBar that = (TaskInputBar) o;
        return this.barValue == that.barValue && super.equals(that);
    }
}
