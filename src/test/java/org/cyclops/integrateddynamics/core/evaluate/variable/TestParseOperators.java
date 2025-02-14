package org.cyclops.integrateddynamics.core.evaluate.variable;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagParser;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.integrateddynamics.ModBaseMocked;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.evaluate.variable.IVariable;
import org.junit.Before;
import org.junit.Test;

import static org.cyclops.integrateddynamics.core.evaluate.operator.Operators.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test the different parse operators.
 * @author LostOfThought
 */
public class TestParseOperators {

    static { CyclopsCoreInstance.MOD = new ModBaseMocked(); }
    private static IVariable[] s(String v){
        IVariable[] ret = new IVariable[1];
        ret[0] = new DummyVariableString(ValueTypeString.ValueString.of(v));
        return ret;
    }

    @Before
    public void before() {

    }

    /**
     * ----------------------------------- INTEGER -----------------------------------
     */
    @Test
    public void testParseInt_IsInt() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("0"));
        assertThat("parse_Integer(\"0\") is an integer", res1, instanceOf(ValueTypeInteger.ValueInteger.class));
    }
    @Test(expected = EvaluationException.class)
    public void testParseIntEmpty() throws EvaluationException {
        PARSE_INTEGER.evaluate(s(""));
    }
    @Test(expected = EvaluationException.class)
    public void testParseIntGarbage() throws EvaluationException {
        PARSE_INTEGER.evaluate(s("garbage"));
    }
    @Test
    public void testParseInt0() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("0"));
        assertThat("parse_Integer(0)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(0));
    }
    @Test
    public void testParseInt1() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("1"));
        assertThat("parse_Integer(1)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(1));
    }
    @Test
    public void testParseIntN1() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("-1"));
        assertThat("parse_Integer(-1)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(-1));
    }
    @Test
    public void testParseIntP1() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("+1"));
        assertThat("parse_Integer(+1)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(1));
    }
    @Test
    public void testParseIntHex_x() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("0xFF"));
        assertThat("parse_Integer(0xFF)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(0xFF));
    }
    @Test
    public void testParseIntHex_X() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("0XFF"));
        assertThat("parse_Integer(0XFF)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(0xFF));
    }
    @Test
    public void testParseIntHex_H() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("#FF"));
        assertThat("parse_Integer(#FF)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(0xFF));
    }
    @Test
    public void testParseIntNHex() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("-0xFF"));
        assertThat("parse_Integer(0xFF)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(-0xFF));
    }
    @Test
    public void testParseIntOctal() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("01"));
        assertThat("parse_Integer(01)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(1));
    }
    @Test
    public void testParseIntNOctal() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s("-01"));
        assertThat("parse_Integer(-01)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(-1));
    }
    @Test
    public void testParseIntMax() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s(Long.toString((long) Integer.MAX_VALUE)));
        assertThat("parse_Integer(<Integer.MAX_VALUE>)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(Integer.MAX_VALUE));
    }
    @Test(expected = EvaluationException.class)
    public void testParseIntMaxP1() throws EvaluationException {
        PARSE_INTEGER.evaluate(s(Long.toString((long) Integer.MAX_VALUE + 1)));
    }
    @Test
    public void testParseIntMin() throws EvaluationException {
        IValue res1 = PARSE_INTEGER.evaluate(s(Long.toString((long) Integer.MIN_VALUE)));
        assertThat("parse_Integer(<Integer.MIN_VALUE>)", ((ValueTypeInteger.ValueInteger) res1).getRawValue(), is(Integer.MIN_VALUE));
    }
    @Test(expected = EvaluationException.class)
    public void testParseIntMinM1() throws EvaluationException {
        PARSE_INTEGER.evaluate(s(Long.toString((long) Integer.MIN_VALUE - 1)));
    }

    /**
     * ----------------------------------- LONG -----------------------------------
     */

    @Test
    public void testParseLong_IsLong() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("0"));
        assertThat("parse_Integer(\"0\") is an integer", res1, instanceOf(ValueTypeLong.ValueLong.class));
    }
    @Test(expected = EvaluationException.class)
    public void testParseLongEmpty() throws EvaluationException {
        PARSE_LONG.evaluate(s(""));
    }
    @Test(expected = EvaluationException.class)
    public void testParseLongGarbage() throws EvaluationException {
        PARSE_LONG.evaluate(s("garbage"));
    }
    @Test
    public void testParseLong0() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("0"));
        assertThat("parse_Long(0)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0L));
    }
    @Test
    public void testParseLong1() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("1"));
        assertThat("parse_Long(1)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(1L));
    }
    @Test
    public void testParseLongN1() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-1"));
        assertThat("parse_Long(-1)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(-1L));
    }
    @Test
    public void testParseLongP1() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("+1"));
        assertThat("parse_Long(+1)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(1L));
    }
    @Test
    public void testParseLongHex_x() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("0xFF"));
        assertThat("parse_Long(0xFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongHex_X() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("0XFF"));
        assertThat("parse_Long(0XFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongHex_H() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("#FF"));
        assertThat("parse_Long(#FF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongNHex() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-0xFF"));
        assertThat("parse_Long(-0xFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(-0xFFL));
    }
    @Test
    public void testParseLongNHex_X() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-0XFF"));
        assertThat("parse_Long(-0XFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(-0xFFL));
    }
    @Test
    public void testParseLongNHex_H() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-#FF"));
        assertThat("parse_Long(-#FF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(-0xFFL));
    }
    @Test
    public void testParseLongPHex() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("+0xFF"));
        assertThat("parse_Long(+0xFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongPHex_X() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("+0XFF"));
        assertThat("parse_Long(+0XFF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongPHex_H() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("+#FF"));
        assertThat("parse_Long(+#FF)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(0xFFL));
    }
    @Test
    public void testParseLongOctal() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("01"));
        assertThat("parse_Long(01)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(1L));
    }
    @Test
    public void testParseLongNOctal() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-01"));
        assertThat("parse_Long(-01)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(-1L));
    }
    @Test
    public void testParseLongPOctal() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("+01"));
        assertThat("parse_Long(+01)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(1L));
    }
    @Test
    public void testParseLongMax() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s(Long.toString(Long.MAX_VALUE)));
        assertThat("parse_Long(<Long.MAX_VALUE>)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(Long.MAX_VALUE));
    }
    @Test(expected = EvaluationException.class)
    public void testParseLongMaxP1() throws EvaluationException {
        PARSE_LONG.evaluate(s("9223372036854775808"));
    }
    @Test
    public void testParseLongMin() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s(Long.toString(Long.MIN_VALUE)));
        assertThat("parse_Long(<Long.MIN_VALUE>)", ((ValueTypeLong.ValueLong) res1).getRawValue(), is(Long.MIN_VALUE));
    }
    @Test(expected = EvaluationException.class)
    public void testParseLongMinM1() throws EvaluationException {
        IValue res1 = PARSE_LONG.evaluate(s("-9223372036854775809"));
    }

    /**
     * ----------------------------------- DOUBLE -----------------------------------
     */

    @Test
    public void testParseDouble_IsDouble() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("0.0"));
        assertThat("parse_Double(\"0.0\") is an double", res1, instanceOf(ValueTypeDouble.ValueDouble.class));
    }
    @Test(expected = EvaluationException.class)
    public void testParseDoubleEmpty() throws EvaluationException {
        PARSE_DOUBLE.evaluate(s(""));
    }
    @Test(expected = EvaluationException.class)
    public void testParseDoubleGarbage() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("garbage"));
    }
    @Test
    public void testParseDouble0() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("0"));
        assertThat("parse_Double(0)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(0.0));
    }
    @Test
    public void testParseDouble1() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("1"));
        assertThat("parse_Double(1)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(1.0));
    }
    @Test
    public void testParseDoubleN0() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-0.0"));
        assertThat("parse_Double(-0.0)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(-0.0));
    }
    @Test
    public void testParseDoubleN1() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-1.0"));
        assertThat("parse_Double(-1.0)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(-1.0));
    }
    @Test
    public void testParseDoubleHex_x() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("0xFF"));
        assertThat("parse_Double(0xFF)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(255.0));
    }
    @Test
    public void testParseDoubleHex_X() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("0XFF"));
        assertThat("parse_Double(0XFF)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(255.0));
    }
    @Test
    public void testParseDoubleHex_H() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("#FF"));
        assertThat("parse_Double(#FF)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(255.0));
    }
    @Test
    public void testParseDoubleNHex() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-0xFF"));
        assertThat("parse_Double(-0xFF)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(-255.0));
    }
    @Test
    public void testParseDoubleOctal() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("01"));
        assertThat("parse_Double(01)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(1.0));
    }
    @Test
    public void testParseDoubleNOctal() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-01"));
        assertThat("parse_Double(-01)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(-1.0));
    }
    @Test
    public void testParseDoubleDOctal() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("01.1"));
        assertThat("parse_Double(01)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(1.1));
    }
    @Test
    public void testParseDoubleNDOctal() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-01.1"));
        assertThat("parse_Double(-01)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(-1.1));
    }
    @Test
    public void testParseDoubleMax() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s(Double.toString(Double.MAX_VALUE)));
        assertThat("parse_Double(<Double.MAX_VALUE>)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.MAX_VALUE));
    }
    @Test
    public void testParseDoubleMaxP1() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("1.7976931348623157e+309"));
        assertThat("parse_Double(<Double.MAX_VALUE * 10>)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleMin() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s(Double.toString(Double.MIN_VALUE)));
        assertThat("parse_Double(<Double.MIN_VALUE>)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.MIN_VALUE));
    }
    @Test
    public void testParseDoubleMinM1() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-1.7976931348623157e+309"));
        assertThat("parse_Double(\"-1.7976931348623157e+309\")", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }
    @Test
    public void testParseDoubleInf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("Inf"));
        assertThat("parse_Double(Inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoublePInf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("+Inf"));
        assertThat("parse_Double(+Inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleNInf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-Inf"));
        assertThat("parse_Double(-Inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }    @Test
    public void testParseDoubleInfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("Infinity"));
        assertThat("parse_Double(Infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoublePInfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("+Infinity"));
        assertThat("parse_Double(+Infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleNInfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-Infinity"));
        assertThat("parse_Double(-Infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }
    @Test
    public void testParseDoubleinf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("inf"));
        assertThat("parse_Double(inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoublePinf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("+inf"));
        assertThat("parse_Double(+inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleNinf() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-inf"));
        assertThat("parse_Double(-inf)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }
    @Test
    public void testParseDoubleinfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("infinity"));
        assertThat("parse_Double(infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoublePinfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("+infinity"));
        assertThat("parse_Double(+infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleNinfinity() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-infinity"));
        assertThat("parse_Double(-infinity)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }
    @Test
    public void testParseDoubleInfSym() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("\u221E"));
        assertThat("parse_Double(infinitysym)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoublePInfSym() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("+\u221E"));
        assertThat("parse_Double(+infinitysym)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.POSITIVE_INFINITY));
    }
    @Test
    public void testParseDoubleNInfSym() throws EvaluationException {
        IValue res1 = PARSE_DOUBLE.evaluate(s("-\u221E"));
        assertThat("parse_Double(-infinitysym)", ((ValueTypeDouble.ValueDouble) res1).getRawValue(), is(Double.NEGATIVE_INFINITY));
    }

    /**
     * ----------------------------------- BOOLEAN -----------------------------------
     */
    @Test
    public void testParseBoolean_IsBoolean() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("T"));
        assertThat("parse_Boolean(\"T\") is Boolean", res1, instanceOf(ValueTypeBoolean.ValueBoolean.class));
    }
    @Test
    public void testParseBooleanEmpty() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s(""));
        assertThat("parse_Boolean(\"\")", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }
    @Test
    public void testParseBooleanNotEmpty() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("garbage"));
        assertThat("parse_Boolean(\"garbage\")", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBoolean0() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("0"));
        assertThat("parse_Boolean(0xFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }
    @Test
    public void testParseBoolean1() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("1"));
        assertThat("parse_Boolean(0xFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanN1() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("-1"));
        assertThat("parse_Boolean(0xFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanHex_x() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("0xFF"));
        assertThat("parse_Boolean(0xFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanHex_X() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("0XFF"));
        assertThat("parse_Boolean(0XFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanHex_H() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("#FF"));
        assertThat("parse_Boolean(#FF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanNHex() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("-0xFF"));
        assertThat("parse_Boolean(0xFF)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanOctal() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("01"));
        assertThat("parse_Boolean(01)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanNOctal() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("-01"));
        assertThat("parse_Boolean(-01)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanT() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("T"));
        assertThat("parse_Boolean(T)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleant() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("t"));
        assertThat("parse_Boolean(t)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanTrue() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("True"));
        assertThat("parse_Boolean(True)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleantrue() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("true"));
        assertThat("parse_Boolean(true)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(true));
    }
    @Test
    public void testParseBooleanF() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("F"));
        assertThat("parse_Boolean(F)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }
    @Test
    public void testParseBooleanf() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("f"));
        assertThat("parse_Boolean(f)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }
    @Test
    public void testParseBooleanFalse() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("False"));
        assertThat("parse_Boolean(False)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }
    @Test
    public void testParseBooleanfalse() throws EvaluationException {
        IValue res1 = PARSE_BOOLEAN.evaluate(s("false"));
        assertThat("parse_Boolean(false)", ((ValueTypeBoolean.ValueBoolean) res1).getRawValue(), is(false));
    }

    /**
     * ----------------------------------- NBT -----------------------------------
     */
    @Test(expected = EvaluationException.class)
    public void testNBTGarbage () throws EvaluationException {
        IValue res1 = PARSE_NBT.evaluate(s("}garbage{"));
    }
    @Test(expected = EvaluationException.class)
    public void testNBTEmpty () throws EvaluationException {
        IValue res1 = PARSE_NBT.evaluate(s(""));
    }
    @Test
    public void testNBTFurnace () throws EvaluationException {
        String NBT = "{CookTime:0,x:2005,BurnTime:0,y:56,ForgeCaps:{},z:-81,Items:[],id:\"minecraft:furnace\",CookTimeTotal:0,Lock:\"\"}";
        IValue res1 = PARSE_NBT.evaluate(s(NBT));
        assertThat("parse_NBT(\"" + NBT + "\")", ((CompoundTag) ((ValueTypeNbt.ValueNbt) res1).getRawValue().get()).getString("id"), instanceOf(String.class));
    }
    @Test
    public void testNBTFurnaceSpaces () throws EvaluationException, CommandSyntaxException {
        String NBT = "{\rCookTime:\n0,\tx:2005, BurnTime:0,  y:56,ForgeCaps:{},z:-81,Items:[],id:\r\n\t \"minecraft:furnace\",CookTimeTotal  :0,Lock:\"\"}";
        IValue res1 = PARSE_NBT.evaluate(s(NBT));
        CompoundTag nbt = (CompoundTag) ValueTypeNbt.ValueNbt.of(TagParser.parseTag(NBT)).getRawValue().get();
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("CookTime"), is(0));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("x"), is(2005));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("BurnTime"), is(0));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("y"), is(56));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getCompound("ForgeCaps").isEmpty(), is(true));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("z"), is(-81));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.get("Items"), instanceOf(ListTag.class));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getString("id"), is("minecraft:furnace"));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getInt("CookTimeTotal"), is(0));
        assertThat("parse_NBT(\"" + NBT + "\")", nbt.getString("Lock"), is(""));
    }
}
