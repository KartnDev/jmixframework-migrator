package io.kartondev.util;

import static org.junit.jupiter.api.Assertions.*;

import io.kartondev.outdated.util.FlowUiTermsStringExtensions;
import org.junit.jupiter.api.Test;

class FlowUiTermsStringExtensionsTest {

    @Test
    void toRefactoredFlowUiControllerName_SimpleControllerName() {
        String controllerNameClassic = "User.browse";

        String controllerNameFlowUi = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerNameClassic);

        assertEquals("User.list", controllerNameFlowUi);
    }

    @Test
    void toRefactoredFlowUiControllerName_AdvancedControllerName() {
        String controllerNameClassic = "UserBrowser.browse";

        String controllerNameFlowUi = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerNameClassic);

        assertEquals("UserListView.list", controllerNameFlowUi);
    }

    @Test
    void toRefactoredFlowUiControllerName_SimpleControllerName_Reversed() {
        String controllerNameClassic = "User.browser";

        String controllerNameFlowUi = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerNameClassic);

        assertEquals("User.list", controllerNameFlowUi);
    }

    @Test
    void toRefactoredFlowUiControllerName_AdvancedControllerName_Reversed() {
        String controllerNameClassic = "UserBrowse.browser";

        String controllerNameFlowUi = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerNameClassic);

        assertEquals("UserListView.list", controllerNameFlowUi);
    }

    @Test
    void toRefactoredFlowUiControllerName_RandomName() {
        String controllerNameClassic = "aws_CategoryBrowser.browse";

        String controllerNameFlowUi = FlowUiTermsStringExtensions.toRefactoredFlowUiControllerName(controllerNameClassic);

        assertEquals("aws_CategoryListView.list", controllerNameFlowUi);
    }
}
