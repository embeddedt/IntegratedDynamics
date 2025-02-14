package org.cyclops.integrateddynamics.core.evaluate.variable;

import com.google.common.collect.Lists;
import lombok.ToString;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.integrateddynamics.api.advancement.criterion.ValuePredicate;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.operator.IOperator;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeNamed;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueTypeUniquelyNamed;
import org.cyclops.integrateddynamics.api.evaluate.variable.ValueDeseralizationContext;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.helper.L10NValues;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeLPElementBase;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeOperatorLPElement;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Value type with operator values.
 * @author rubensworks
 */
public class ValueTypeOperator extends ValueTypeBase<ValueTypeOperator.ValueOperator> implements
        IValueTypeNamed<ValueTypeOperator.ValueOperator>, IValueTypeUniquelyNamed<ValueTypeOperator.ValueOperator> {

    private static final String SIGNATURE_LINK = "->";

    public ValueTypeOperator() {
        super("operator", Helpers.RGBToInt(43, 231, 47), ChatFormatting.DARK_GREEN, ValueTypeOperator.ValueOperator.class);
    }

    @Override
    public ValueOperator getDefault() {
        return ValueOperator.of(Operators.GENERAL_IDENTITY);
    }

    @Override
    public MutableComponent toCompactString(ValueOperator value) {
        return value.getRawValue().getLocalizedNameFull();
    }

    @Override
    public Tag serialize(ValueDeseralizationContext valueDeseralizationContext, ValueOperator value) {
        return Operators.REGISTRY.serialize(valueDeseralizationContext, value.getRawValue());
    }

    @Override
    public ValueOperator deserialize(ValueDeseralizationContext valueDeseralizationContext, Tag value) {
        IOperator operator;
        try {
            operator = Operators.REGISTRY.deserialize(valueDeseralizationContext, value);
        } catch (EvaluationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        if (operator != null) {
            return ValueOperator.of(operator);
        }
        throw new IllegalArgumentException(String.format("Value \"%s\" could not be parsed to an operator.", value));
    }

    @Override
    public void loadTooltip(List<Component> lines, boolean appendOptionalInfo, @Nullable ValueOperator value) {
        super.loadTooltip(lines, appendOptionalInfo, value);
        if (value != null) {
            lines.add(Component.translatable(L10NValues.VALUETYPEOPERATOR_TOOLTIP_SIGNATURE)
                    .append(getSignature(value.getRawValue())));
        }
    }

    @Override
    public ValueTypeLPElementBase createLogicProgrammerElement() {
        return new ValueTypeOperatorLPElement();
    }

    @Override
    public ValueOperator materialize(ValueOperator value) throws EvaluationException {
        return ValueOperator.of(value.getRawValue().materialize());
    }

    /**
     * Pretty formatted signature of an operator.
     * @param operator The operator.
     * @return The signature.
     */
    public static MutableComponent getSignature(IOperator operator) {
        return getSignatureLines(operator, false)
                .stream()
                .reduce(Component.literal(""), (a, b) -> a.append(" ").append(b));
    }

    /**
     * Pretty formatted signature of an operator.
     * @param inputTypes The input types.
     * @param outputType The output types.
     * @return The signature.
     */
    public static Component getSignature(IValueType[] inputTypes, IValueType outputType) {
        return getSignatureLines(inputTypes, outputType, false)
                .stream()
                .reduce((prev, next) -> prev.append(" ").append(next))
                .orElseGet(() -> Component.literal(""));
    }

    protected static MutableComponent switchSignatureLineContext(List<MutableComponent> lines, MutableComponent sb) {
        lines.add(sb);
        return Component.literal("");
    }

    /**
     * Pretty formatted signature of an operator.
     * @param inputTypes The input types.
     * @param outputType The output types.
     * @param indent If the lines should be indented.
     * @return The signature.
     */
    public static List<MutableComponent> getSignatureLines(IValueType[] inputTypes, IValueType outputType, boolean indent) {
        List<MutableComponent> lines = Lists.newArrayList();
        MutableComponent sb = Component.literal("");
        boolean first = true;
        for (IValueType inputType : inputTypes) {
            if (first) {
                first = false;
            } else {
                sb = switchSignatureLineContext(lines, sb);
                sb.append((indent ? "  " : "") + SIGNATURE_LINK + " ");
            }
            sb = sb.append(Component.translatable(inputType.getTranslationKey())
                    .withStyle(inputType.getDisplayColorFormat()));
        }

        sb = switchSignatureLineContext(lines, sb);
        sb = sb.append((indent ? "  " : "") + SIGNATURE_LINK + " ")
                .append(Component.translatable(outputType.getTranslationKey())
                        .withStyle(outputType.getDisplayColorFormat()));
        switchSignatureLineContext(lines, sb);
        return lines;
    }

    /**
     * Pretty formatted signature of an operator.
     * @param operator The operator.
     * @param indent If the lines should be indented.
     * @return The signature.
     */
    public static List<MutableComponent> getSignatureLines(IOperator operator, boolean indent) {
        return getSignatureLines(operator.getInputTypes(), operator.getOutputType(), indent);
    }

    @Override
    public String getName(ValueTypeOperator.ValueOperator a) {
        return a.getRawValue().getLocalizedNameFull().getString();
    }

    @Override
    public String getUniqueName(ValueOperator a) {
        return a.getRawValue().getUniqueName().toString();
    }

    @ToString
    public static class ValueOperator extends ValueBase {

        private final IOperator value;

        private ValueOperator(IOperator value) {
            super(ValueTypes.OPERATOR);
            this.value = value;
        }

        public static ValueOperator of(IOperator value) {
            return new ValueOperator(value);
        }

        public IOperator getRawValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof ValueOperator && value.equals(((ValueOperator) o).value));
        }

        @Override
        public int hashCode() {
            return 37 + value.hashCode();
        }
    }

    public static class ValueOperatorPredicate extends ValuePredicate<ValueOperator> {

        private final Optional<IOperator> operator;

        public ValueOperatorPredicate(Optional<IOperator> operator) {
            super(Optional.of(ValueTypes.OPERATOR), Optional.empty(), Optional.empty());
            this.operator = operator;
        }

        public Optional<IOperator> getOperator() {
            return operator;
        }

        @Override
        protected boolean testTyped(ValueOperator value) {
            return super.testTyped(value)
                    && (operator.isEmpty() || (value.getRawValue() == operator.get()));
        }
    }

}
