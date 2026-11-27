package com.beemdevelopment.aegis.ui.dialogs;

import android.content.Context;

import com.beemdevelopment.aegis.R;
import com.beemdevelopment.aegis.helpers.MarkdownHtml;

import java.util.Locale;

public class PrivacyDialog extends SimpleWebViewDialog {
    public PrivacyDialog() {
        super(R.string.privacy_policy);
    }

    public static PrivacyDialog create() {
        return new PrivacyDialog();
    }

    @Override
    protected String getContent(Context context) {
        String asset = "de".equals(Locale.getDefault().getLanguage()) ? "privacy.de.md" : "privacy.en.md";
        return MarkdownHtml.render(readAssetAsString(context, asset), getBackgroundColor(), getTextColor());
    }
}
