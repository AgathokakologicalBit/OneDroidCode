package com.timewarp.games.onedroidcode.vsl;

import com.timewarp.games.onedroidcode.vsl.nodes.RootNode;
import com.timewarp.games.onedroidcode.vsl.nodes.flow.IfNode;
import com.timewarp.games.onedroidcode.vsl.nodes.variables.ValueHolderNode;

import junit.framework.TestCase;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CodeRunnerTest extends TestCase {

    private CodeRunner runner;


    private class TriggerNode extends Node {

        private boolean isTriggered = false;
        private String name;

        public TriggerNode(String name) {
            this.name = name;
        }

        @Override
        public Node execute(CodeRunner runner) {
            Logger.getAnonymousLogger().log(Level.INFO, "TRIGGER NODE(" + name + ")");
            this.isTriggered = true;
            return next;
        }

        @Override
        public void reset() {
            this.isTriggered = false;
        }
    }

    public TriggerNode runUntilTriggerAny(int maxSteps) {
        int step = 0;
        while (step < maxSteps
                && !(runner.getActiveNode() instanceof TriggerNode)
                && runner.step()
                ) {
            ++step;
        }

        TriggerNode trigger = null;
        if (runner.getActiveNode() instanceof TriggerNode) {
            trigger = (TriggerNode) runner.getActiveNode();
        }

        runner.step();
        return trigger;
    }


    public void setUp() throws Exception {
        super.setUp();

        runner = new CodeRunner();
    }

    public void testInitialization() throws Exception {
        // Right code with entry point
        final Node[] codeCorrect = new Node[]{
                new RootNode()
        };

        // Wrong code(No entry point)
        final Node[] codeWrong = new Node[]{
                new ValueHolderNode(null, Value.TYPE_ANY)
        };

        TestCase.assertTrue("Should load basic code(return true)", runner.load(codeCorrect));
        TestCase.assertFalse("Should not return true on wrong code", runner.load(codeWrong));
    }

    public void testIfCondition() throws Exception {
        // ===---   CREATE NODES   ---===
        final Node rootNode = new RootNode();
        final ValueHolderNode booleanValueHolder = new ValueHolderNode(Value.TYPE_BOOLEAN);
        final IfNode ifConditionNode = new IfNode();

        final TriggerNode triggerTrue = new TriggerNode("True condition");
        final TriggerNode triggerFalse = new TriggerNode("False condition");


        // ===---   SET UP CONDITIONS   ---===
        ifConditionNode.inCondition = booleanValueHolder.outValue;


        // ===---   BUILD CODE STRUCTURE   ---===
        rootNode
                .append(booleanValueHolder)
                .append(ifConditionNode);

        ifConditionNode.onTrue = triggerTrue;
        ifConditionNode.onFalse = triggerFalse;

        // ===---   LOAD CODE INTO RUNNER   ---===
        final Node[] code = new Node[]{
                rootNode,

                booleanValueHolder,
                ifConditionNode,

                triggerTrue, triggerFalse
        };
        runner.load(code);


        // ===---   TEST TRIGGERING FALSE NODE   ---===
        TestCase.assertNotNull("Should run up to breakpoint", runUntilTriggerAny(5));
        TestCase.assertFalse("Should not trigger 'True' node", triggerTrue.isTriggered);
        TestCase.assertTrue("Should trigger 'False' node", triggerFalse.isTriggered);

        // ===---   TEST TRIGGERING TRUE NODE   ---===
        runner.reset();
        booleanValueHolder.outValue.set(true);

        TestCase.assertNotNull("Should run up to breakpoint", runUntilTriggerAny(5));
        TestCase.assertTrue("Should trigger 'True' node", triggerTrue.isTriggered);
        TestCase.assertFalse("Should not trigger 'False' node", triggerFalse.isTriggered);
    }
}