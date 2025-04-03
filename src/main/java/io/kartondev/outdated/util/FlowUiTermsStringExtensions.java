package io.kartondev.outdated.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlowUiTermsStringExtensions {

    public static String toRefactoredFlowUiControllerName(String input) {
        Pattern pattern = Pattern.compile("(?i)^(\\w+_)?(\\w+)(\\.\\w+)$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            String prefixAppName = Optional.ofNullable(matcher.group(1)).orElse("");
            String name = Optional.ofNullable(matcher.group(2)).orElse("");
            String postfix = Optional.ofNullable(matcher.group(3)).orElse("");

            name = name.replaceAll("(?i)browser", "ListView")
                    .replaceAll("(?i)browse", "ListView")
                    .replaceAll("(?i)editor", "DetailView")
                    .replaceAll("(?i)edit", "DetailView");

            postfix = postfix.replaceAll("(?i)browser", "list")
                    .replaceAll("(?i)browse", "list")
                    .replaceAll("(?i)editor", "detail")
                    .replaceAll("(?i)edit", "detail");

            return prefixAppName + name + postfix;
        }
        return input;
    }
}
