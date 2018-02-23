package utils;

import freemarker.core.*;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateNumberModel;

import java.util.Locale;

public class NumberToDateFormatFactory extends TemplateNumberFormatFactory {
    public static final NumberToDateFormatFactory INSTANCE = new NumberToDateFormatFactory();

    private NumberToDateFormatFactory() {
    }

    @Override
    public TemplateNumberFormat get(String params, Locale locale, Environment env) throws TemplateValueFormatException {
        return NumberToDateFormat.INSTANCE;
    }

    private static final class NumberToDateFormat extends TemplateNumberFormat {
        private static final NumberToDateFormat INSTANCE = new NumberToDateFormat();

        private NumberToDateFormat() {
        }

        @Override
        public String formatToPlainText(TemplateNumberModel numberModel) throws TemplateValueFormatException, TemplateModelException {
            Number num = TemplateFormatUtil.getNonNullNumber(numberModel);
            long date = num.longValue();
            return Utils.defaultDateFormat(date);
        }

        @Override
        public boolean isLocaleBound() {
            return false;
        }

        @Override
        public String getDescription() {
            return null;
        }
    }
}
